package com.example.server.entity;

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
     * 
     */
    private Double power;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer state;

    /**
     *
     */
    private Double dayGen;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}