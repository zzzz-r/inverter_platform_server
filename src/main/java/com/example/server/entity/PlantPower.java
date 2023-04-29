package com.example.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("plant_power_table")
public class PlantPower {
    private Integer id;
    private Integer state;
    private Boolean alarm;
    private Float power;
    private Float dayGen;
}
