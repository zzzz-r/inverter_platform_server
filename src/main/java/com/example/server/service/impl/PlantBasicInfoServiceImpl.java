package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantBasicInfo;
import com.example.server.exception.PoiException;
import com.example.server.mapper.PlantBasicInfoMapper;
import com.example.server.service.IPlantBasicInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlantBasicInfoServiceImpl extends ServiceImpl<PlantBasicInfoMapper, PlantBasicInfo> implements IPlantBasicInfoService {
    @Resource
    private PlantBasicInfoMapper plantBasicInfoMapper;
    @Override
    public void updatePlant(PlantBasicInfo poi){
        try {
            int row = plantBasicInfoMapper.updateById(poi);
            if( row==0 ){
                throw PoiException.OperateFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
