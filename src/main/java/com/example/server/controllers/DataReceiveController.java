package com.example.server.controllers;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.Form.MiData;
import com.example.server.Form.UploadData;
import com.example.server.entity.*;
import com.example.server.service.*;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    @Resource
    private PlantOwnerTableService plantOwnerTableService;
    @Resource
    private IUserService userService;
    @Resource
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailFrom;


    @PostMapping("/upload")
    // body请求中raw json参数
    public Result upLoadData(@RequestBody UploadData uploadData){
        log.info("{}",uploadData);
        // 更新DTU最后更新时间，或插入新DTU
        Date currentDate = new Date();
        DtuTable dtuTable = new DtuTable();
        dtuTable.setId(uploadData.getDtuId());
        dtuTable.setState(0);
        dtuTable.setUpdateTime(currentDate);
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
                miPowerTable.setUpdateTime(currentDate);
                miPowerTable.setState(0);
                miPowerTableService.updateById(miPowerTable);

                // 插入数据到逆变器24小时发电信息表
                MiDayPower miDayPower = new MiDayPower();
                BeanUtils.copyProperties(miPowerTable, miDayPower);
                miDayPowerService.save(miDayPower);

                // 根据相关参数修改报警信息表
                updateMiAlarmTable(miData,currentDate);

            }catch (Exception e){
//                e.printStackTrace();
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
        if(cur_if_alarm == 0 && if_alarm == 1) {
            miAlarmTable.setUpdateTime(updateTime);
            try{
                sendAlarmMail(miData);
            } catch (Exception e) {
            }
        }
        miAlarmTableService.updateById(miAlarmTable);
    }

    public void sendAlarmMail(MiData miData) {
        int plantId = miInfoTableService.getPlantIdByMiId(miData.getId()); // 获得对应电站id

        List<User> receivers = new ArrayList<>(); // 存储所有需要接收邮件的用户

        // 给所属业主发送邮件
        List<PlantOwnerTable> plantOwnerTables = plantOwnerTableService.getByPlantId(plantId);
        for (PlantOwnerTable ownerTable : plantOwnerTables) {
            User user = userService.getById(ownerTable.getUserId());
            if (user != null && user.getAlarmMessage() == 1 && !StrUtil.isBlank(user.getEmail())) {
                receivers.add(user);
            }
        }

        // 给机构用户发送邮件
        int instituteId = plantOwnerTableService.getBelongedInstituteId(plantId);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 1).eq("institute_id", instituteId);
        List<User> instituteUsers = userService.list(queryWrapper);
        for (User user : instituteUsers) {
            if (user.getAlarmMessage() == 1 && !StrUtil.isBlank(user.getEmail())) {
                receivers.add(user);
            }
        }

        String mailContent = generateMailContent(miData);

        // 使用ExecutorService来管理线程池
        ExecutorService executor = Executors.newFixedThreadPool(10); // 创建大小为10的线程池

        // 将发送邮件的任务提交到线程池中
        for (User receiver : receivers) {
            executor.submit(() -> {
                sendMail("微型逆变器设备报警提醒", mailContent, receiver.getEmail());
            });
        }

        // 关闭线程池
        executor.shutdown();
    }

    private void sendMail(String subject, String content, String receiver) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);  // 发送人
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    private String generateMailContent(MiData miData) {
        return "报警设备编号：" + miData.getId() + "\n\n"
                + "PV输入电压：" + miData.getDcVoltage() + " V\n"
                + "PV输入电流：" + miData.getDcCurrent() + " A\n"
                + "交流输出电压：" + miData.getAcVoltage() + " V\n"
                + "交流输出电流：" + miData.getAcCurrent() + " A\n"
                + "电网电压：" + miData.getGridVoltage() + " V\n"
                + "温度：" + miData.getTemperature() + " ℃\n"
                + "频率：" + miData.getFreq() + " Hz\n\n"
                + "更详细信息可登录云平台查看。";
    }

}
