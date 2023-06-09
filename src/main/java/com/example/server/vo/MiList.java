package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MiList {
    private Integer id;
    private Integer state;
    private Integer ifAlarm;
    private Integer dtuId;
    private Double power;
    private Double capacity;
    private Date updateTime;
    private Double dayGen;
    private Double dcCurrent;
    private Double dcVoltage;
    private Double acCurrent;
    private Double acVoltage;
    private Double freq;
    private Double temperature;
    private Double gridVoltage;
}
