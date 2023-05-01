package com.example.server.mapper;

import com.example.server.entity.InstituteTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 86130
* @description 针对表【institute_table】的数据库操作Mapper
* @createDate 2023-04-29 22:55:35
* @Entity com.example.server.entity.InstituteTable
*/
public interface InstituteTableMapper extends BaseMapper<InstituteTable> {
    @Select("SELECT * FROM institute_table")
    List<InstituteTable> list();

    @Select("SELECT name FROM institute_table WHERE id = #{id}")
    String getNameById(int id);
}




