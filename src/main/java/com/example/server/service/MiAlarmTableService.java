package com.example.server.service;

import com.example.server.entity.MiAlarmTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86130
* @description 针对表【mi_alarm_table】的数据库操作Service
* @createDate 2023-04-28 14:05:54
*/
public interface MiAlarmTableService extends IService<MiAlarmTable> {
    List<MiAlarmTable> listAllAlarm();
    List<MiAlarmTable> listAlarmByInstitute(Integer instituteId);
}
