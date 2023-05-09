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
 * @TableName plant_info_table
 */
@TableName(value ="plant_info_table")
@Data
public class PlantInfoTable implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String province;

    /**
     * 
     */
    private String city;

    /**
     * 
     */
    private String county;

    /**
     * 
     */
    private String address;

    /**
     * 6位小数点
     */
    private Double lat;

    /**
     * 
     */
    private Double lng;

    /**
     * 
     */
    private Date buildDate;

    /**
     * 
     */
    private String plantType;

    /**
     * 
     */
    private String sysType;

    /**
     * 两位小数点，以下同，否则报错
     */
    private Double capacity;

    /**
     * 
     */
    private Double selfUseRate;

    /**
     * 
     */
    private Double elecBenefit;

    /**
     * 
     */
    private Double subsidyBenefit;

    /**
     * 
     */
    private Double cost;

    /**
     * 
     */
    private Double dailyRepay;

    /**
     * 
     */
    private String cover;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}