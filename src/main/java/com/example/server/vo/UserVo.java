package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo { // 权限管理返回用户信息
    private Integer id;
    private String userName;
    private String email;
    private String phone;
    private Integer type;
    private Integer instituteId;
    private Date createTime;
}
