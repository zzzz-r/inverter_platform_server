package com.example.server.service;

import com.example.server.entity.PlantOwnerTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86130
* @description 针对表【plant_owner_table】的数据库操作Service
* @createDate 2023-05-01 13:21:22
*/
public interface PlantOwnerTableService extends IService<PlantOwnerTable> {
    List<PlantOwnerTable> getByPlantId(int plantId);
    List<PlantOwnerTable> getByInstituteId(int instituteId);
    void deleteByPlantId(int plantId);
}
