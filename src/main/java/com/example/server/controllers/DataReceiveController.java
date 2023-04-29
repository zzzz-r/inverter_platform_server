package com.example.server.controllers;

import com.example.server.Form.MiData;
import com.example.server.Form.UploadData;
import com.example.server.entity.*;
import com.example.server.service.*;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/data")
public class DataReceiveController {
    @Resource
    private DtuTableService dtuTableService;
    @Resource
    private MiInfoTableService miInfoTableService;
    @Resource
    private MiPowerTableService miPowerTableService;
    @Resource
    private MiDayPowerService miDayPowerService;
    @Resource
    private MiAlarmTableService miAlarmTableService;
    @PostMapping("/upload")
    // body请求中raw json参数
    public Result upLoadData(@RequestBody UploadData uploadData){
        log.info("{}",uploadData);
        // 更新DTU最后更新时间，或插入新DTU
        DtuTable dtuTable = new DtuTable();
        dtuTable.setId(uploadData.getDtuId());
        dtuTable.setState(0);
        dtuTable.setUpdateTime(uploadData.getUpdateTime());
        dtuTableService.saveOrUpdate(dtuTable);

        for(MiData miData: uploadData.getMiDataList()){
            // 检查逆变器表中是否有对应表项，没有则插入
            try{
                MiInfoTable miInfoTable = miInfoTableService.getById(miData.getId());
                if(miInfoTable == null){
                    miInfoTable = new MiInfoTable();
                    miInfoTable.setId(miData.getId());
                    miInfoTable.setDtuId(uploadData.getDtuId());
                    miInfoTableService.save(miInfoTable);
                } // 触发器自动在发电信息和报警信息表插入

                // 更新当前发电信息表
                MiPowerTable miPowerTable = new MiPowerTable();
                BeanUtils.copyProperties(miData, miPowerTable);
                miPowerTable.setUpdateTime(uploadData.getUpdateTime());
                miPowerTable.setState(0);
                miPowerTableService.updateById(miPowerTable);

                // 插入数据到逆变器24小时发电信息表
                MiDayPower miDayPower = new MiDayPower();
                BeanUtils.copyProperties(miPowerTable, miDayPower);
                miDayPowerService.save(miDayPower);

                // 根据相关参数修改报警信息表
                updateMiAlarmTable(miData,uploadData.getUpdateTime());

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return Result.success();
    }

    public void updateMiAlarmTable(MiData miData, Date updateTime){
        MiAlarmTable miAlarmTable = new MiAlarmTable();
        miAlarmTable.setId(miData.getId());
        int cur_if_alarm = miAlarmTableService.getById(miData.getId()).getIfAlarm();
        int if_alarm = 0;
        // 温度
        if(miData.getTemperature()<-20||miData.getTemperature()>60){
            if_alarm = 1;
            miAlarmTable.setTemperature(1);
        }else {
            miAlarmTable.setTemperature(0);
        }
        // 频率
        if(miData.getFreq()<50||miData.getFreq()>60){
            if_alarm = 1;
            miAlarmTable.setFreq(1);
        }else {
            miAlarmTable.setFreq(0);
        }
        // 直流电流
        if(miData.getDcCurrent()<0.5||miData.getDcCurrent()>15){
            if_alarm = 1;
            miAlarmTable.setDcCurrent(1);
        }else {
            miAlarmTable.setDcCurrent(0);
        }
        // 直流电压
        if(miData.getDcVoltage()<20||miData.getDcVoltage()>80){
            if_alarm = 1;
            miAlarmTable.setDcVoltage(1);
        }else {
            miAlarmTable.setDcVoltage(0);
        }
        // 交流电流
        if(miData.getAcCurrent()<0.5||miData.getAcCurrent()>15){
            if_alarm = 1;
            miAlarmTable.setAcCurrent(1);
        }else {
            miAlarmTable.setAcCurrent(0);
        }
        // 交流电压
        if(miData.getAcVoltage()<110||miData.getAcVoltage()>240){
            if_alarm = 1;
            miAlarmTable.setAcVoltage(1);
        }else {
            miAlarmTable.setAcVoltage(0);
        }
        // 电网电压
        if(miData.getGridVoltage()<110||miData.getGridVoltage()>240){
            if_alarm = 1;
            miAlarmTable.setGridVoltage(1);
        }else {
            miAlarmTable.setGridVoltage(0);
        }

        miAlarmTable.setIfAlarm(if_alarm);
        if(cur_if_alarm == 0 && if_alarm == 1)
            miAlarmTable.setUpdateTime(updateTime);
        miAlarmTableService.updateById(miAlarmTable);
    }
}
