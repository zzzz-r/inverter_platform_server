package com.example.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.Form.UserDTO;
import com.example.server.entity.User;

public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);

    String getNameById(int id);
}