package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName plant_power_table
 */
@TableName(value ="plant_power_table")
@Data
public class PlantPowerTable implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 0123分别表示正常、接入中、设备离线
     */
    private Integer state;

    /**
     * 0表示正常，1表示报警
     */
    private Boolean alarm;

    /**
     * 
     */
    private Double power;

    /**
     * 
     */
    private Double dayGen;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}