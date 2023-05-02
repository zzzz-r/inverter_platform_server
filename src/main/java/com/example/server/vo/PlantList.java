package com.example.server.vo;

import lombok.Data;

import java.util.List;

@Data
public class PlantList {
    private Integer id;
    private String name;
    private Integer state;
    private Boolean alarm;
    private String Institute;
    private List<String> owner;
    private Float power;
    private Float dayGen;
    private Float capacity;
    private String buildDate;
    private String cover;

}
