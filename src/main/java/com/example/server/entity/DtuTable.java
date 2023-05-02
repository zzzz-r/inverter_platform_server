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
 * @TableName dtu_table
 */
@TableName(value ="dtu_table")
@Data
public class DtuTable implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer state;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer plantId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}