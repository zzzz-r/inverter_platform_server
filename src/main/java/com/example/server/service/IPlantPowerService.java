package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.entity.PlantPower;

public interface IPlantPowerService extends IService<PlantPower> {
    void deletePlant(int id);
}
