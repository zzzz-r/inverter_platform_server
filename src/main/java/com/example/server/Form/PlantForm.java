package com.example.server.Form;

import lombok.Data;

import java.util.List;

@Data
public class PlantForm {
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
    private String cover;
    private Integer instituteId;
    List<Integer> userId;
}