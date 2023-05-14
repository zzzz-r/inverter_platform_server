package com.example.server.mapper;

import com.example.server.entity.MiInfoTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.vo.MiList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86130
* @description 针对表【mi_info_table】的数据库操作Mapper
* @createDate 2023-04-24 21:36:41
* @Entity com.example.server.entity.MiInfoTable
*/
public interface MiInfoTableMapper extends BaseMapper<MiInfoTable> {
    List<MiList> listMiByPlantId(@Param("plantId") int plantId);

    String getPlantNameByMiId(int miId);

    Integer getPlantIdByMiId(int miId);
}




