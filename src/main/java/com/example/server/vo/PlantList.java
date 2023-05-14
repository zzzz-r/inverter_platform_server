package com.example.server.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlantList {
    private Integer id;
    private String name;
    private Integer state;
    private Boolean alarm;
    private String Institute;
    private List<String> owner;
    private Double power;
    private Double dayGen;
    private Double capacity;
    private Date buildDate;
    private String cover;
    private Double lat;
    private Double lng;
}
