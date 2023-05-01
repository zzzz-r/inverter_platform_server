package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private Integer id;
    private String userName;
    private String email;
    private String phone;
    private Integer type;
    private Integer instituteId;
    private Date createTime;
}
