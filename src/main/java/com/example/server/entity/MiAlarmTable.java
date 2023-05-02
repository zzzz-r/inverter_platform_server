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
 * @TableName mi_alarm_table
 */
@TableName(value ="mi_alarm_table")
@Data
public class MiAlarmTable implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private Integer ifAlarm;

    /**
     * 
     */
    private Integer temperature;

    /**
     * 
     */
    private Integer freq;

    /**
     * 
     */
    private Integer dcCurrent;

    /**
     * 
     */
    private Integer dcVoltage;

    /**
     * 
     */
    private Integer acCurrent;

    /**
     * 
     */
    private Integer acVoltage;

    /**
     * 
     */
    private Integer gridVoltage;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}