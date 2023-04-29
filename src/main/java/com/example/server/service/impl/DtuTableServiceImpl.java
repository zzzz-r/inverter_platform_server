package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.DtuTable;
import com.example.server.exception.PoiException;
import com.example.server.mapper.MiInfoTableMapper;
import com.example.server.service.DtuTableService;
import com.example.server.mapper.DtuTableMapper;
import com.example.server.vo.DtuList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 86130
* @description 针对表【dtu_table】的数据库操作Service实现
* @createDate 2023-04-24 23:14:48
*/
@Service
public class DtuTableServiceImpl extends ServiceImpl<DtuTableMapper, DtuTable>
    implements DtuTableService{
    @Resource
    private DtuTableMapper dtuTableMapper;
    public List<DtuList> listDtuByPlantId(int plantId){
        try{
            return dtuTableMapper.listDtuByPlantId(plantId);
        }catch (Exception e){
            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }
}




