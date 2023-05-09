package com.example.server.service;

import com.example.server.entity.PlantInfoTable;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86130
* @description 针对表【plant_info_table】的数据库操作Service
* @createDate 2023-05-02 21:30:15
*/
public interface PlantInfoTableService extends IService<PlantInfoTable> {
    void updatePlant(PlantInfoTable poi);
    int countConnectedMi(Integer plantId);
    int countConnectedDtu(Integer plantId);
}
