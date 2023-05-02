package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName plant_owner_table
 */
@TableName(value ="plant_owner_table")
@Data
public class PlantOwnerTable implements Serializable {
    private Integer plantId;
    private Integer userId;
    private Integer instituteId;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}