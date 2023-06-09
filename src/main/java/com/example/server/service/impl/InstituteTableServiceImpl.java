package com.example.server.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.common.Constants;
import com.example.server.entity.InstituteTable;
import com.example.server.entity.User;
import com.example.server.service.InstituteTableService;
import com.example.server.mapper.InstituteTableMapper;
import com.example.server.utils.RedisUtils;
import com.example.server.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        // 找到当前用户的机构id
        assert user != null;
        int id = user.getInstituteId();

        List<InstituteTable> childInstitutes;
//        // 查找是否有对应缓存
//         String cache = RedisUtils.getRedisCache(Constants.INSTITUTES);
//        if(cache == null){
//            childInstitutes = findAllChildInstitute(id);
//            RedisUtils.setRedisCache(Constants.INSTITUTES,childInstitutes); // 设置缓存
//        } else{
//            childInstitutes = JSONUtil.toBean(cache, new TypeReference<List<InstituteTable>>() {
//            }, true); // 取缓存并转换为对象
//        }
        childInstitutes = findAllChildInstitute(id);
        return childInstitutes;
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
            if(Objects.equals(child.getPid(), institute.getId())){
                institutesVo.add(child);
                findChildInstitute(child, institutes, institutesVo);
            }
        }
    }

    public String getNameById(int id){
        return instituteTableMapper.getNameById(id);
    }
}




