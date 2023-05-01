package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DtuAlarmVo {
    private Object id;

    private Integer state;

    private Date updateTime;

    private String plantName;
}