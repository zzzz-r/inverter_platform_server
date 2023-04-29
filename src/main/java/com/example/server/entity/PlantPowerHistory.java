package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

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
    private Object id;

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