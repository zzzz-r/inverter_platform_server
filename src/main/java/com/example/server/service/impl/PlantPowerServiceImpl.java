package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantPower;
import com.example.server.exception.PoiException;
import com.example.server.mapper.PlantBasicInfoMapper;
import com.example.server.mapper.PlantPowerMapper;
import com.example.server.service.IPlantPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PlantPowerServiceImpl extends ServiceImpl<PlantPowerMapper, PlantPower> implements IPlantPowerService {
    @Resource
    private PlantPowerMapper plantPowerMapper;
    @Resource
    private PlantBasicInfoMapper plantBasicInfoMapper;
    @Override
    public void deletePlant(int id){
        int row = plantPowerMapper.deleteById(id);
        if( row==0 ){
            throw PoiException.OperateFail();
        }
        row = plantBasicInfoMapper.deleteById(id); // 根据poi_id删除pic_table中的数据
        if( row==0 ){
            throw PoiException.OperateFail();
        }
    }
}
