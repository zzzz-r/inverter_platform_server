package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("plant_info_table")
public class PlantBasicInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String province;
    private String city;
    private String county;
    private String address;
    private float lat;
    private float lng;
    private String buildDate;
    private String plantType;
    private String sysType;
    private float capacity;
    private float selfUseRate;
    private float elecBenefit;
    private float subsidyBenefit;
    private float cost;
    private float daily_repay;
    private String ownerName;
    private String ownerTel;
    private String cover;
}