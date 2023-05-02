package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName plant_power_history
 */
@TableName(value ="plant_power_history")
@Data
public class PlantPowerHistory implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 当日发电量
     */
    private Double dayGen;

    /**
     * 容量
     */
    private Double capacity;

    /**
     * 日期
     */
    private Date dayTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}