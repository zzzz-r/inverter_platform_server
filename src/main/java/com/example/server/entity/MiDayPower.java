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
 * @TableName mi_day_power
 */
@TableName(value ="mi_day_power")
@Data
public class MiDayPower implements Serializable {
    /**
     * 
     */
    @TableId
    private Object id;

    /**
     * 
     */
    @TableId
    private Date updateTime;

    /**
     * 
     */
    private Double power;

    /**
     * 
     */
    private Double temperature;

    /**
     * 电网电压
     */
    private Double gridVoltage;

    /**
     * 电网频率
     */
    private Double freq;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}