package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.entity.PlantBasicInfo;

import java.util.List;

public interface IPlantBasicInfoService extends IService<PlantBasicInfo> {
    void saveNewPlant(PlantBasicInfo poi);
    void updatePlant(PlantBasicInfo poi);
}
