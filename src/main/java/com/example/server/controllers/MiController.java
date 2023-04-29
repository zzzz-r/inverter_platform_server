package com.example.server.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.entity.MiAlarmTable;
import com.example.server.entity.MiDayPower;
import com.example.server.entity.MiInfoTable;
import com.example.server.entity.MiPowerTable;
import com.example.server.exception.PoiException;
import com.example.server.service.MiAlarmTableService;
import com.example.server.service.MiDayPowerService;
import com.example.server.service.MiInfoTableService;
import com.example.server.service.MiPowerTableService;
import com.example.server.vo.MiAlarmVo;
import com.example.server.vo.MiList;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
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
        BeanUtils.copyProperties(miInfoTable,miList);
        BeanUtils.copyProperties(miPowerTable,miList);
        return Result.success(miList);
    }

    @GetMapping("/dayInfo/{miId}")
    public Result getDayPowerInfo(@PathVariable int miId){
        try{
            QueryWrapper<MiDayPower> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", miId)
                    .ge("update_time", LocalDate.now().atStartOfDay()) //返回今天之内的数据
                    .le("update_time", LocalDate.now().atTime(LocalTime.MAX));
            List<MiDayPower> miLists = miDayPowerService.list(queryWrapper);
            return Result.success(miLists);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.success();
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
            QueryWrapper<MiAlarmTable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("if_alarm", 1);
            List<MiAlarmTable> miAlarmTableList = miAlarmTableService.list(queryWrapper);
            List miAlarmVoList = miAlarmTableList.stream().map( item->{
                MiAlarmVo miAlarmVo = new MiAlarmVo();
                BeanUtils.copyProperties(item,miAlarmVo);
                int plantId = miInfoTableService.getPlantId(item.getId());
                miAlarmVo.setPlantId(plantId);
                int state = miPowerTableService.getById((Serializable) item.getId()).getState();
                miAlarmVo.setState(state);
                return miAlarmVo;
            }).collect(Collectors.toList());
            return Result.success(miAlarmVoList);
        }catch (Exception e){
            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }
}
