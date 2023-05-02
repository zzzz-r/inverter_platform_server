package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantOwnerTable;
import com.example.server.service.PlantOwnerTableService;
import com.example.server.mapper.PlantOwnerTableMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86130
* @description 针对表【plant_owner_table】的数据库操作Service实现
* @createDate 2023-05-01 13:21:22
*/
@Service
public class PlantOwnerTableServiceImpl extends ServiceImpl<PlantOwnerTableMapper, PlantOwnerTable>
    implements PlantOwnerTableService{
    @Resource
    private PlantOwnerTableMapper plantOwnerTableMapper;
    @Override
    public List<PlantOwnerTable> getByPlantId(int plantId){
        return plantOwnerTableMapper.selectList(new QueryWrapper<PlantOwnerTable>().eq("plant_id", plantId));
    }
    @Override
    public void deleteByPlantId(int plantId){
        plantOwnerTableMapper.delete(new QueryWrapper<PlantOwnerTable>().eq("plant_id", plantId));
    }
    @Override
    public List<PlantOwnerTable> getByInstituteId(int instituteId){
        return plantOwnerTableMapper.selectList(new QueryWrapper<PlantOwnerTable>().eq("institute_id", instituteId));
    }
}




