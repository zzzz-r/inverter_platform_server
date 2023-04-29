package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.entity.DtuTable;
import com.example.server.vo.DtuList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86130
* @description 针对表【dtu_table】的数据库操作Mapper
* @createDate 2023-04-24 23:14:48
* @Entity com.example.server.entity.DtuTable
*/
public interface DtuTableMapper extends BaseMapper<DtuTable> {
    List<DtuList> listDtuByPlantId(@Param("plantId") int plantId);
}




