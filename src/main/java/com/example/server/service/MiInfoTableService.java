package com.example.server.service;

import com.example.server.entity.MiInfoTable;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.vo.MiList;

import java.util.List;

/**
* @author 86130
* @description 针对表【mi_info_table】的数据库操作Service
* @createDate 2023-04-24 21:36:41
*/
public interface MiInfoTableService extends IService<MiInfoTable> {
    List<MiList> listMiByPlantId(int plantId);

    String getPlantName(Object miId);
}
