package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.entity.DtuTable;
import com.example.server.vo.DtuList;

import java.util.List;

/**
* @author 86130
* @description 针对表【dtu_table】的数据库操作Service
* @createDate 2023-04-24 23:14:48
*/
public interface DtuTableService extends IService<DtuTable> {
    List<DtuList> listDtuByPlantId(int plantId);

    List<DtuTable> listAlarmByInstitute(Integer id);
}
