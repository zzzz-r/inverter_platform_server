package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName institute_table
 */
@TableName(value ="institute_table")
@Data
public class InstituteTable implements Serializable {
    /**
     * 机构id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 上级机构id
     */
    private Integer pid;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 机构类型
     */
    private String type;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private Integer contactTel;

    /**
     * 地址
     */
    private String address;

    /**
     * 描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}