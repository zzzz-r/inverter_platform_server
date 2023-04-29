package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_table")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Integer type;
    private String institute;
    private Date createTime;
    private String avatar;

}
