package com.example.server.mapper;

import com.example.server.entity.PlantPowerHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 86130
* @description 针对表【plant_power_history】的数据库操作Mapper
* @createDate 2023-04-29 11:49:38
* @Entity com.example.server.entity.PlantPowerHistory
*/
public interface PlantPowerHistoryMapper extends BaseMapper<PlantPowerHistory> {

    List<PlantPowerHistory> selectByPlantId(Integer plantId);
}




