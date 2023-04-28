package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.MiInfoTable;
import com.example.server.service.MiInfoTableService;
import com.example.server.mapper.MiInfoTableMapper;
import com.example.server.vo.MiList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86130
* @description 针对表【mi_info_table】的数据库操作Service实现
* @createDate 2023-04-24 21:36:41
*/
@Service
public class MiInfoTableServiceImpl extends ServiceImpl<MiInfoTableMapper, MiInfoTable>
    implements MiInfoTableService{
    @Resource
    private MiInfoTableMapper miInfoTableMapper;
    @Override
    public List<MiList> listMiByPlantId(int plantId){
        return miInfoTableMapper.listMiByPlantId(plantId);
    }
}



