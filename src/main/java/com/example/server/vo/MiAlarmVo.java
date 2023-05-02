package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MiAlarmVo {
    private Integer id;
    private Integer state;
    private String plantName;
    private Integer ifAlarm;
    private Integer temperature;
    private Integer freq;
    private Integer dcCurrent;
    private Integer dcVoltage;
    private Integer acCurrent;
    private Integer acVoltage;
    private Integer gridVoltage;
    private Date updateTime;
}
