package com.example.server.controllers;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.Form.PlantForm;
import com.example.server.entity.*;
import com.example.server.exception.PoiException;
import com.example.server.service.*;
import com.example.server.service.impl.UserServiceImpl;
import com.example.server.utils.TokenUtils;
import com.example.server.vo.PlantList;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/plant")
public class PlantController {
    @Resource
    private IPlantPowerService plantPowerService;
    @Resource
    private IPlantBasicInfoService plantBasicInfoService;
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
                                PlantPower plantPower = plantPowerService.getById(plantId);
                                PlantBasicInfo plantBasicInfo = plantBasicInfoService.getById(plantId);
                                BeanUtils.copyProperties(plantPower,plantList);
                                BeanUtils.copyProperties(plantBasicInfo,plantList);
                                return plantList;
                            })
                            // 在这里使用 filter 方法对电站列表进行过滤，若当前用户为业主，只返回包含当前用户名的电站列表。
                            .filter(plantList -> userService.ifInstituteUser() || plantList.getOwner().contains(userService.getCurUserName()));
                })
                .collect(Collectors.toList());
        return Result.success(plantLists);
    }

    @GetMapping("/detail/info/{id}") // 获得电站基本信息,包括业主信息（编辑电站）
    public Result detailInfo(@PathVariable int id){
        // 得到基本信息
        PlantBasicInfo plantBasicInfo = plantBasicInfoService.getById(id);
        PlantForm plantForm = new PlantForm();
        BeanUtils.copyProperties(plantBasicInfo,plantForm);
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
        PlantBasicInfo plantBasicInfo = plantBasicInfoService.getById(id);
        PlantPower plantPower = plantPowerService.getById(id);
        if(plantBasicInfo == null || plantPower== null){
            throw PoiException.NotFound(); //查找失败抛出异常
        }
        PlantList plantList = new PlantList();
        BeanUtils.copyProperties(plantBasicInfo,plantList);
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
            PlantBasicInfo plantBasicInfo = new PlantBasicInfo();
            BeanUtils.copyProperties(plantForm,plantBasicInfo);
            plantBasicInfoService.saveOrUpdate(plantBasicInfo);
            // 添加或修改业主信息
            int instituteId = plantForm.getInstituteId();
            int plantId = plantBasicInfo.getId();
            // 首先删除业主原有信息
            plantOwnerTableService.deleteByPlantId(plantId);
            for(Integer userId: plantForm.getUserId()){
                PlantOwnerTable plantOwnerTable = new PlantOwnerTable();
                plantOwnerTable.setPlantId(plantId);
                plantOwnerTable.setUserId(userId);
                plantOwnerTable.setInstituteId(instituteId);
                plantOwnerTableService.save(plantOwnerTable);
            }
            return Result.success(plantId);
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail();
        }
    }

    @PutMapping ("/edit/{id}") // 编辑电站
    public Result edit(@RequestBody PlantBasicInfo poiForm, @PathVariable int id){
        log.info("poi:{}",poiForm);
        plantBasicInfoService.updatePlant(poiForm);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}") //删除电站
    public Result delete(@PathVariable int id){
        plantPowerService.deletePlant(id);
        return Result.success();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<PlantPower> plantPowerList = plantPowerService.list();
        List voList = new ArrayList();
        for(PlantPower poi: plantPowerList){
            PlantList poiVo = new PlantList();
            BeanUtils.copyProperties(poi,poiVo);
            PlantBasicInfo plantBasicInfo = plantBasicInfoService.getById(poi.getId());
            poiVo.setName(plantBasicInfo.getName());
            poiVo.setBuildDate(plantBasicInfo.getBuildDate());
            voList.add(poiVo);
        }
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(voList, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("电站发电信息表", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }
}
