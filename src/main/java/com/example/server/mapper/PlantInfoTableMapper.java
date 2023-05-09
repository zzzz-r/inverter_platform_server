package com.example.server.mapper;

import com.example.server.entity.PlantInfoTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86130
* @description 针对表【plant_info_table】的数据库操作Mapper
* @createDate 2023-05-02 21:30:15
* @Entity com.example.server.entity.PlantInfoTable
*/
public interface PlantInfoTableMapper extends BaseMapper<PlantInfoTable> {
    int countConnectedMi(Integer plantId);

    int countConnectedDtu(Integer plantId);
}




