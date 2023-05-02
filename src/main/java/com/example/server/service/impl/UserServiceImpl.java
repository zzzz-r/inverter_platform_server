package com.example.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.Form.UserDTO;
import com.example.server.entity.User;
import com.example.server.exception.PoiException;
import com.example.server.mapper.InstituteTableMapper;
import com.example.server.mapper.UserMapper;
import com.example.server.service.IUserService;
import com.example.server.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private InstituteTableMapper instituteTableMapper;
    @Override
    public UserDTO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userDTO.getUserName());
        User one = userMapper.selectOne(queryWrapper);
        if (one != null && Objects.equals(one.getPassword(), userDTO.getPassword())) {
            BeanUtil.copyProperties(one, userDTO, true);
            String token = TokenUtils.genToken(one.getId().toString(),one.getPassword()); // 设置token
            userDTO.setToken(token);
            String instituteName = instituteTableMapper.getNameById(one.getInstituteId());
            userDTO.setInstitute(instituteName);
            return userDTO;
        } else {
            throw PoiException.ErrorLogin();
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userDTO.getUserName());
        User one = userMapper.selectOne(queryWrapper);
        if(one == null){
            User user = new User();
            BeanUtils.copyProperties(userDTO, user);
            int row = userMapper.insert(user);
            if(row == 0){
                throw PoiException.OperateFail();
            }else{
                return user;
            }
        }else{
            throw PoiException.ErrorRegister();
        }
    }

    public String getNameById(int id){
        return userMapper.getNameById(id);
    }

    public boolean ifInstituteUser(){
        User user = TokenUtils.getCurrentUser();
        if(user!=null){
            return user.getType() != 0;
        }else{
            throw PoiException.NoRoot();
        }
    }

    public String getCurUserName(){
        User user = TokenUtils.getCurrentUser();
        if(user!=null){
            return user.getUserName();
        }else{
            return null;
        }
    }
}
