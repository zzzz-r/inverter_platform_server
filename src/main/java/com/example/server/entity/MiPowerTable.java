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
 * @TableName mi_power_table
 */
@TableName(value ="mi_power_table")
@Data
public class MiPowerTable implements Serializable {
    /**
     * 
     */
    @TableId
    private Object id;

    /**
     * 功率
     */
    private Double power;

    /**
     * 最后更新时间
     */
    private Date updateTime;

    /**
     * 0在线，1离线
     */
    private Integer state;

    /**
     * 当日发电量
     */
    private Double dayGen;

    /**
     * 直流电流
     */
    private Double dcCurrent;

    /**
     * 直流电压
     */
    private Double dcVoltage;

    /**
     * 交流电流
     */
    private Double acCurrent;

    /**
     * 交流电压
     */
    private Double acVoltage;

    /**
     * 电网频率
     */
    private Double freq;

    /**
     * 温度
     */
    private Double temperature;

    /**
     * 电网电压
     */
    private Double gridVoltage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}