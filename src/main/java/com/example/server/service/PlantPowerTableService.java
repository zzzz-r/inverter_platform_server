package com.example.server.service;

import com.example.server.entity.PlantPowerTable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86130
* @description 针对表【plant_power_table】的数据库操作Service
* @createDate 2023-05-03 09:30:37
*/
public interface PlantPowerTableService extends IService<PlantPowerTable> {
    void deletePlant(int id);
}
