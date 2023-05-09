package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GenReport {
    private String name;
    private Date dayTime;
    private Double dayGen;
}
