package com.example.server.service;

import com.example.server.entity.PlantPowerHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.vo.GenHistory;

import java.util.List;

/**
* @author 86130
* @description 针对表【plant_power_history】的数据库操作Service
* @createDate 2023-04-29 11:49:38
*/
public interface PlantPowerHistoryService extends IService<PlantPowerHistory> {
    List<PlantPowerHistory> selectByPlantId(Integer plantId);
    List<GenHistory> countGenHistory(List<Integer> plantIdList); // 传入电站Id列表，计算发电历史信息
}
