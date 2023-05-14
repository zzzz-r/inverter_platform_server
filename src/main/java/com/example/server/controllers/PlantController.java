package com.example.server.controllers;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.Form.PlantForm;
import com.example.server.common.Constants;
import com.example.server.entity.*;
import com.example.server.exception.PoiException;
import com.example.server.service.*;
import com.example.server.service.impl.UserServiceImpl;
import com.example.server.utils.RedisUtils;
import com.example.server.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/plant")
public class PlantController {
    @Resource
    private PlantPowerTableService plantPowerTableService;
    @Resource
    private PlantInfoTableService plantInfoTableService;
    @Resource
    private PlantPowerHistoryService plantPowerHistoryService;
    @Resource
    private PlantOwnerTableService plantOwnerTableService;
    @Resource
    private InstituteTableService instituteTableService;
    @Resource
    private UserServiceImpl userService;
    @GetMapping("/list")
    public Result list(){
//        // 先查找缓存
//        String cache = RedisUtils.getRedisCache(Constants.PLANTS);
//        if(cache != null) {
//            List<PlantList> plantLists = JSONUtil.toBean(cache, new TypeReference<List<PlantList>>() {}, true);
//            return Result.success(plantLists);
//        }

        // 列出当前用户下所有机构
        List<InstituteTable> institutes = instituteTableService.listInstitute();

        List<PlantList> plantLists = institutes.stream()
                .flatMap(institute -> { // 使用 flatMap() 方法将所有的 PlantOwnerTable 记录转换为 PlantList 对象。
                    // 对每个机构，找到全部电站所有者业主
                    List<PlantOwnerTable> plantOwners = plantOwnerTableService.getByInstituteId(institute.getId());
                    //使用 Map 来将 PlantOwnerTable 记录按照 plantId 分组，并将每个 userId 对应的用户名添加到同一个列表中。
                    Map<Integer, List<String>> plantOwnerMap = new HashMap<>();
                    for (PlantOwnerTable plantOwner : plantOwners) {
                        plantOwnerMap.computeIfAbsent(plantOwner.getPlantId(), k -> new ArrayList<>())
                                .add(userService.getNameById(plantOwner.getUserId()));
                    }
                    // 对于每个 PlantList 对象，使用 Map.Entry 来获取它的 id 和 Owner 字段，而 Institute 字段则来自于外层的机构对象
                    return plantOwnerMap.entrySet().stream()
                            .map(entry -> {
                                PlantList plantList = new PlantList();
                                int plantId = entry.getKey();
                                plantList.setId(plantId);
                                plantList.setInstitute(institute.getName());
                                plantList.setOwner(entry.getValue());
                                PlantPowerTable plantPower = plantPowerTableService.getById(plantId);
                                PlantInfoTable PlantInfoTable = plantInfoTableService.getById(plantId);
                                BeanUtils.copyProperties(plantPower,plantList);
                                BeanUtils.copyProperties(PlantInfoTable,plantList);
                                return plantList;
                            })
                            // 在这里使用 filter 方法对电站列表进行过滤，若当前用户为业主，只返回包含当前用户名的电站列表。
                            .filter(plantList -> userService.ifInstituteUser() || plantList.getOwner().contains(userService.getCurUserName()));
                })
                .collect(Collectors.toList());
//        RedisUtils.setRedisCache(Constants.PLANTS,plantLists);
        return Result.success(plantLists);
    }

    @GetMapping("/detail/info/{id}") // 获得电站基本信息,包括业主信息（编辑电站）
    public Result detailInfo(@PathVariable int id){
        // 得到基本信息
        PlantInfoTable PlantInfoTable = plantInfoTableService.getById(id);
        PlantForm plantForm = new PlantForm();
        BeanUtils.copyProperties(PlantInfoTable,plantForm);
        // 得到业主信息
        List<PlantOwnerTable> plantOwnerTableList = plantOwnerTableService.getByPlantId(id);

        List<Integer> userIdList = new ArrayList<>();
        Integer instituteId = null;

        for (PlantOwnerTable plantOwnerTable : plantOwnerTableList) {
            if (plantOwnerTable.getUserId() != null) {
                userIdList.add(plantOwnerTable.getUserId());
            }
            if (plantOwnerTable.getInstituteId() != null) {
                instituteId = plantOwnerTable.getInstituteId();
            }
        }

        plantForm.setUserId(userIdList);
        plantForm.setInstituteId(instituteId);

        return Result.success(plantForm);
    }

    @GetMapping("/detail/plantList/{id}") // 获得电站List信息
    // 通过访问路径获得参数
    public Result detailPlantList(@PathVariable int id){
        PlantInfoTable PlantInfoTable = plantInfoTableService.getById(id);
        PlantPowerTable plantPower = plantPowerTableService.getById(id);
        if(PlantInfoTable == null || plantPower== null){
            throw PoiException.NotFound(); //查找失败抛出异常
        }
        PlantList plantList = new PlantList();
        BeanUtils.copyProperties(PlantInfoTable,plantList);
        BeanUtils.copyProperties(plantPower,plantList);
        return Result.success(plantList);
    }

    @GetMapping("/detail/history/{id}") // 获得电站历史信息
    // 通过访问路径获得参数
    public Result detailPlantHistory(@PathVariable int id){
        QueryWrapper<PlantPowerHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List<PlantPowerHistory> plantPowerHistoryList = plantPowerHistoryService.list(queryWrapper);
        return Result.success(plantPowerHistoryList);
    }

    @PostMapping("/addOrEdit") // 添加或编辑电站
    public Result addOrEdit(@RequestBody PlantForm plantForm){ //@RequestBody获取前端的json数据给poiForm对象
        try{
            // 添加或修改基本信息
            PlantInfoTable PlantInfoTable = new PlantInfoTable();
            BeanUtils.copyProperties(plantForm,PlantInfoTable);
            plantInfoTableService.saveOrUpdate(PlantInfoTable);
            // 添加或修改业主信息
            int instituteId = plantForm.getInstituteId();
            int plantId = PlantInfoTable.getId();
            // 首先删除业主原有信息
            plantOwnerTableService.deleteByPlantId(plantId);
            for(Integer userId: plantForm.getUserId()){
                PlantOwnerTable plantOwnerTable = new PlantOwnerTable();
                plantOwnerTable.setPlantId(plantId);
                plantOwnerTable.setUserId(userId);
                plantOwnerTable.setInstituteId(instituteId);
                plantOwnerTableService.save(plantOwnerTable);
            }
//            RedisUtils.flushRedisCache(Constants.PLANTS);
            return Result.success(plantId);
        }catch (Exception e){
//            e.printStackTrace();
            return Result.fail();
        }
    }

    @PutMapping ("/edit/{id}") // 编辑电站
    public Result edit(@RequestBody PlantInfoTable poiForm, @PathVariable int id){
        plantInfoTableService.updatePlant(poiForm);
//        RedisUtils.flushRedisCache(Constants.PLANTS);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}") //删除电站
    public Result delete(@PathVariable int id){
        plantPowerTableService.deletePlant(id);
//        RedisUtils.flushRedisCache(Constants.PLANTS);
        return Result.success();
    }

    @GetMapping("/export") // 导出电站列表
    public void export(HttpServletResponse response) throws Exception {
        try{
            // 从数据库查询出所有的数据
            Result result = list(); // list有问题，InstituteTableService中查询不到当前用户
            List<PlantList> plantLists = (List<PlantList>) result.data;
            // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
            // 在内存操作，写出到浏览器
            ExcelWriter writer = ExcelUtil.getWriter(true);

            // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
            writer.write(plantLists, true);

            // 设置浏览器响应的格式
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            String fileName = URLEncoder.encode("电站发电信息表", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

            ServletOutputStream out = response.getOutputStream();
            writer.flush(out, true);
            out.close();
            writer.close();
        }catch (Exception e){
//            e.printStackTrace();
        }
    }

    // 列出当前用户下所有电站id
    public List<Integer> listPlantId(){
        // 列出当前用户下所有机构
        List<InstituteTable> institutes = instituteTableService.listInstitute();

        List<Integer> plantIdLists = institutes.stream()
                .flatMap(institute -> { // 使用 flatMap() 方法将所有的 PlantOwnerTable 记录转换为 PlantList 对象。
                    // 对每个机构，找到全部电站所有者业主
                    List<PlantOwnerTable> plantOwners = plantOwnerTableService.getByInstituteId(institute.getId());
                    //使用 Map 来将 PlantOwnerTable 记录按照 plantId 分组，并将每个 userId 对应的用户名添加到同一个列表中。
                    Map<Integer, List<String>> plantOwnerMap = new HashMap<>();
                    for (PlantOwnerTable plantOwner : plantOwners) {
                        plantOwnerMap.computeIfAbsent(plantOwner.getPlantId(), k -> new ArrayList<>())
                                .add(userService.getNameById(plantOwner.getUserId()));
                    }

                    return plantOwnerMap.entrySet().stream()
                            .map(entry -> {
                                int plantId = entry.getKey();
                                // 如果当前用户为业主，且该电站不属于该业主，返回空
                                if(!userService.ifInstituteUser() && !entry.getValue().contains(userService.getCurUserName())){
                                    return null;
                                }
                                return plantId;
                            });
                })
                .collect(Collectors.toList());
        return plantIdLists;
    }

    @GetMapping("/count")
    public Result count(){
        try{
            CountVo countVo = new CountVo();
            // 获得当前用户的电站id列表
            List<Integer> plantIdList = listPlantId();
            // 计算历史信息
            List<GenHistory> genHistoryList = plantPowerHistoryService.countGenHistory(plantIdList);
            countVo.setGenHistory(genHistoryList);
            // 计算连接设备信息
            int connectedMiNum = 0;
            int connectedDtuNum = 0;
            int onlineNum = 0;
            int offlineNum = 0;
            int buildNum = 0;
            int alarmNum = 0;
            int noAlarmNum = 0;
            for(Integer plantId: plantIdList){
                PlantPowerTable one = plantPowerTableService.getById(plantId);
                if(one != null){
                    switch(one.getState()){
                        case 0:  onlineNum++; break;
                        case 1:  buildNum++; break;
                        case 2:  offlineNum++; break;
                    }
                    if(one.getAlarm())
                        alarmNum++;
                    else
                        noAlarmNum++;
                }
                connectedMiNum += plantInfoTableService.countConnectedMi(plantId);
                connectedDtuNum += plantInfoTableService.countConnectedDtu(plantId);
            }
            countVo.setMiNum(connectedMiNum);
            countVo.setDtuNum(connectedDtuNum);
            countVo.setAlarmNum(alarmNum);
            countVo.setNoAlarmNum(noAlarmNum);
            countVo.setOnlineNum(onlineNum);
            countVo.setOfflineNum(offlineNum);
            countVo.setBuildNum(buildNum);
            return Result.success(countVo);
        }catch (Exception e){
//            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }

    @GetMapping("/genReport/{plantId}")
    public Result genReport(@PathVariable int plantId, @RequestParam String startDate, @RequestParam String endDate) {
        try {
            // 获取电站历史发电数据
            QueryWrapper<PlantPowerHistory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", plantId);
            List<PlantPowerHistory> plantPowerHistoryList = plantPowerHistoryService.list(queryWrapper);

            // 筛选日期在 startDate 和 endDate 之间的历史发电数据
            List<GenHistory> selectedHistories = new ArrayList<>();
            for (PlantPowerHistory plantPowerHistory : plantPowerHistoryList) {
                if (plantPowerHistory.getDayTime().compareTo(Date.valueOf(startDate)) >= 0 &&
                        plantPowerHistory.getDayTime().compareTo(Date.valueOf(endDate)) <= 0) {
                    selectedHistories.add(new GenHistory(
                            plantPowerHistory.getDayTime(), plantPowerHistory.getDayGen(), plantPowerHistory.getCapacity()
                    ));
                }
            }

            // 生成报告数据
            String plantName = plantInfoTableService.getById(plantId).getName();
            List<GenReport> genReports = new ArrayList<>();
            for (GenHistory history : selectedHistories) {
                GenReport report = new GenReport();
                report.setName(plantName);
                report.setDayTime(history.getDayTime());
                report.setDayGen(history.getDayGen());
                genReports.add(report);
            }

            return Result.success(genReports);
        } catch (Exception e) {
//            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }

    @GetMapping("/export/genReport/{plantId}")
    public void genReportExport(HttpServletResponse response, @PathVariable int plantId, @RequestParam String startDate, @RequestParam String endDate)throws Exception{
        List<GenReport> genReports = (List<GenReport>)genReport(plantId,startDate,endDate).data;
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(genReports, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("电站发电报表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
