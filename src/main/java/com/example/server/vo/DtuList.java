package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class DtuList {
    private Object id;
    private Integer state;
    private String updateTime;
    private Integer connectNum;
}
