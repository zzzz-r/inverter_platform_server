package com.example.server.controllers;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.Form.UserDTO;
import com.example.server.entity.User;
import com.example.server.exception.PoiException;
import com.example.server.service.IUserService;
import com.example.server.utils.TokenUtils;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.server.enums.ResultEnum.ERROR_PARAM;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        String userName = userDTO.getUserName();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            return Result.fail(ERROR_PARAM);
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUserName();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.fail(ERROR_PARAM);
        }
        User user = userService.register(userDTO);
        return Result.success(user);
    }

    @GetMapping("/id/{id}")
    public Result findById(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @PutMapping("/update/{id}")
    public Result Update(@RequestBody User user,@PathVariable Integer id) {
        user.setId(id);
        try{
            userService.updateById(user);
        }catch (Exception e){
            throw PoiException.ErrorRegister();
        }
        UserDTO userDTO = new UserDTO();
        user = userService.getById(user.getId());
        BeanUtils.copyProperties(user, userDTO);
        String token = TokenUtils.genToken(user.getId().toString(),user.getPassword()); // 设置token
        userDTO.setToken(token);
        return Result.success(userDTO);
    }

    @PutMapping("/resetPwd/{id}")
    public Result resetPwd(@PathVariable Integer id) {
        User user = userService.getById(id);
        if(user == null){
            Result.fail();
        }
        user.setPassword("123456");
        userService.updateById(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteUser(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success();
    }

    @PutMapping("/admin/edit")
    public Result adminEdit(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        List<User> userList = userService.list(queryWrapper);
        if(userList.size() >=1 && userList.get(0).getId() != user.getId()){ //用户已存在
            throw PoiException.ErrorRegister();
        }
        userService.updateById(user);
        return Result.success();
    }

    @PostMapping("/admin/register")
    public Result adminRegister(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", user.getUserName());
        User one = userService.getOne(queryWrapper);
        if(one != null){ //用户已存在
            throw PoiException.ErrorRegister();
        }
        boolean flag = userService.save(user);
        log.info("{}",user);
        if (flag){
            return Result.success();
        }else{
            return Result.fail();
        }
    }
}
