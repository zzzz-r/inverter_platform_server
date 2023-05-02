package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName mi_info_table
 */
@TableName(value ="mi_info_table")
@Data
public class MiInfoTable implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer dtuId;

    /**
     * 容量 kWp
     */
    private Double capacity;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}