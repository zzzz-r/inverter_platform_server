package com.example.server.mapper;

import com.example.server.entity.MiAlarmTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86130
* @description 针对表【mi_alarm_table】的数据库操作Mapper
* @createDate 2023-04-28 14:05:54
* @Entity com.example.server.entity.MiAlarmTable
*/
public interface MiAlarmTableMapper extends BaseMapper<MiAlarmTable> {
    List<MiAlarmTable> listAlarmByInstitute(Integer instituteId);
}




