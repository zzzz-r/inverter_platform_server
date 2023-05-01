package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.InstituteTable;
import com.example.server.entity.User;
import com.example.server.enums.ResultEnum;
import com.example.server.exception.PoiException;
import com.example.server.mapper.PlantPowerMapper;
import com.example.server.service.InstituteTableService;
import com.example.server.mapper.InstituteTableMapper;
import com.example.server.utils.TokenUtils;
import com.example.server.vo.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* @author 86130
* @description 针对表【institute_table】的数据库操作Service实现
* @createDate 2023-04-29 22:55:35
*/
@Service
public class InstituteTableServiceImpl extends ServiceImpl<InstituteTableMapper, InstituteTable>
    implements InstituteTableService{
    @Resource
    private InstituteTableMapper instituteTableMapper;

    public List<InstituteTable> listInstitute(){
        User user = TokenUtils.getCurrentUser();
        // 普通用户返回无权限
        if(user == null || user.getType() == 0)
            throw PoiException.NoRoot();
        // 找到当前用户的机构id
        int id = user.getInstituteId();
        List<InstituteTable> institutesVo = findAllChildInstitute(id);
        return institutesVo;
    }

    // 查找对应id的机构下的所有子机构
    public List<InstituteTable> findAllChildInstitute(int id){
        List<InstituteTable> institutes = instituteTableMapper.list();
        List<InstituteTable> institutesVo = new ArrayList<>();
        // 递归遍历所有机构，找到pid下所属的所有机构
        for(InstituteTable institute : institutes){
            if(institute.getId() == id){
                institutesVo.add(institute);
                findChildInstitute(institute, institutes, institutesVo);
            }
        }
        return institutesVo;
    }

    // 递归查找institute下所有的子机构
    public void findChildInstitute(InstituteTable institute, List<InstituteTable> institutes, List<InstituteTable> institutesVo){
        for(InstituteTable child : institutes){
            if(child.getPid() == institute.getId()){
                institutesVo.add(child);
                findChildInstitute(child, institutes, institutesVo);
            }
        }
    }

    public String getNameById(int id){
        return instituteTableMapper.getNameById(id);
    }
}




