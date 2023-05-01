package com.example.server.service;

import com.example.server.entity.InstituteTable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86130
* @description 针对表【institute_table】的数据库操作Service
* @createDate 2023-04-29 22:55:35
*/
public interface InstituteTableService extends IService<InstituteTable> {
    List<InstituteTable> listInstitute();
    List<InstituteTable> findAllChildInstitute(int id);
    void findChildInstitute(InstituteTable institute, List<InstituteTable> institutes, List<InstituteTable> institutesVo);
    String getNameById(int id);
}
