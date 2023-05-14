package com.example.server.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.entity.*;
import com.example.server.exception.PoiException;
import com.example.server.service.*;
import com.example.server.vo.MiAlarmVo;
import com.example.server.vo.MiList;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/mi")
public class MiController {
    @Resource
    private MiInfoTableService miInfoTableService;
    @Resource
    private MiPowerTableService miPowerTableService;
    @Resource
    private MiDayPowerService miDayPowerService;
    @Resource
    private MiAlarmTableService miAlarmTableService;
    @Resource
    private InstituteTableService instituteTableService;

    @GetMapping("/list/{plantId}")
    public Result list(@PathVariable int plantId){
        List<MiList> miLists = miInfoTableService.listMiByPlantId(plantId);
        return Result.success(miLists);
    }

    @GetMapping("/detail/{miId}")
    public Result getMiAllInfo(@PathVariable int miId){
        MiList miList = new MiList();
        MiInfoTable miInfoTable= miInfoTableService.getById(miId);
        MiPowerTable miPowerTable= miPowerTableService.getById(miId);
        MiAlarmTable miAlarmTable= miAlarmTableService.getById(miId);
        BeanUtils.copyProperties(miInfoTable,miList);
        BeanUtils.copyProperties(miPowerTable,miList);
        BeanUtils.copyProperties(miAlarmTable,miList);
        return Result.success(miList);
    }

    @GetMapping("/dayInfo/{miId}")
    public Result getDayPowerInfo(@PathVariable int miId){ // 获得微逆24小时数据
        try{
            QueryWrapper<MiDayPower> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", miId)
                    .ge("update_time", LocalDate.now().atStartOfDay()) //返回今天之内的数据
                    .le("update_time", LocalDate.now().atTime(LocalTime.MAX));
            List<MiDayPower> miLists = miDayPowerService.list(queryWrapper);
            return Result.success(miLists);
        }catch (Exception e){
//            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }

    @GetMapping("/detail/power/{miId}")
    public Result getCurrentPowerInfo(@PathVariable int miId){
        MiPowerTable one = miPowerTableService.getById(miId);
        if(one == null){
            throw PoiException.NotFound(); //查找失败抛出异常
        }
        return Result.success(one);
    }

    @GetMapping("/alarm")
    public Result listAlarmMi(){
        try{
            List<MiAlarmVo> miAlarmVoList = instituteTableService.listInstitute().stream()
                    .flatMap(institute -> miAlarmTableService.listAlarmByInstitute(institute.getId()).stream()
                            .map(item -> {
                                MiAlarmVo miAlarmVo = new MiAlarmVo();
                                BeanUtils.copyProperties(item, miAlarmVo);
                                String plantName = miInfoTableService.getPlantNameByMiId(item.getId());
                                miAlarmVo.setPlantName(plantName);
                                int state = miPowerTableService.getById(item.getId()).getState();
                                miAlarmVo.setState(state);
                                return miAlarmVo;
                            }))
                    .collect(Collectors.toList());
            return Result.success(miAlarmVoList);
        }catch (Exception e){
//            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }

    @PostMapping("/add") // 添加
    public Result add(@RequestBody MiInfoTable miInfoTable){
        try{
            miInfoTableService.saveOrUpdate(miInfoTable);
        }catch (Exception e){
            throw PoiException.OperateFail();
        }
        return Result.success();
    }

    @DeleteMapping("/delete/{id}") //删除
    public Result delete(@PathVariable int id){
        try{
            miInfoTableService.removeById(id);
        }catch (Exception e){
            throw PoiException.OperateFail();
        }
        return Result.success();
    }
}
