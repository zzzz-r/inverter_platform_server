package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantPowerTable;
import com.example.server.exception.PoiException;
import com.example.server.mapper.PlantInfoTableMapper;
import com.example.server.service.PlantPowerTableService;
import com.example.server.mapper.PlantPowerTableMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 86130
* @description 针对表【plant_power_table】的数据库操作Service实现
* @createDate 2023-05-03 09:30:37
*/
@Service
public class PlantPowerTableServiceImpl extends ServiceImpl<PlantPowerTableMapper, PlantPowerTable>
    implements PlantPowerTableService{
    @Resource
    private PlantPowerTableMapper plantPowerTableMapper;
    @Resource
    private PlantInfoTableMapper plantInfoTableMapper;

    public void deletePlant(int id){
        int row = plantPowerTableMapper.deleteById(id);
        if( row==0 ){
            throw PoiException.OperateFail();
        }
        row = plantInfoTableMapper.deleteById(id); // 根据poi_id删除pic_table中的数据
        if( row==0 ){
            throw PoiException.OperateFail();
        }
    }
}




