package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantInfoTable;
import com.example.server.exception.PoiException;
import com.example.server.service.PlantInfoTableService;
import com.example.server.mapper.PlantInfoTableMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 86130
* @description 针对表【plant_info_table】的数据库操作Service实现
* @createDate 2023-05-02 21:30:15
*/
@Service
public class PlantInfoTableServiceImpl extends ServiceImpl<PlantInfoTableMapper, PlantInfoTable>
    implements PlantInfoTableService{
    @Resource
    private PlantInfoTableMapper plantInfoTableMapper;
    public void updatePlant(PlantInfoTable poi){
        int row = plantInfoTableMapper.updateById(poi);
        if( row==0 ){
            throw PoiException.OperateFail();
        }
    }
    public int countConnectedMi(Integer plantId){
        return plantInfoTableMapper.countConnectedMi(plantId);
    }

    public int countConnectedDtu(Integer plantId){
        return plantInfoTableMapper.countConnectedDtu(plantId);
    }

}




