package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.MiAlarmTable;
import com.example.server.mapper.MiAlarmTableMapper;
import com.example.server.service.MiAlarmTableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86130
* @description 针对表【mi_alarm_table】的数据库操作Service实现
* @createDate 2023-04-28 14:05:54
*/
@Service
public class MiAlarmTableServiceImpl extends ServiceImpl<MiAlarmTableMapper, MiAlarmTable>
    implements MiAlarmTableService{
    @Resource
    private MiAlarmTableMapper miAlarmTableMapper;
    public List<MiAlarmTable> listAllAlarm(){
        QueryWrapper<MiAlarmTable> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("if_alarm", 1);
        return miAlarmTableMapper.selectList(queryWrapper);
    }
    public List<MiAlarmTable> listAlarmByInstitute(Integer instituteId){
        return miAlarmTableMapper.listAlarmByInstitute(instituteId);
    }
}




