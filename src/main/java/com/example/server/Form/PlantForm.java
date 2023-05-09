package com.example.server.Form;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlantForm {
    private Integer id;
    private String name;
    private String province;
    private String city;
    private String county;
    private String address;
    private Double lat;
    private Double lng;
    private Date buildDate;
    private String plantType;
    private String sysType;
    private Double capacity;
    private Double selfUseRate;
    private Double elecBenefit;
    private Double subsidyBenefit;
    private Double cost;
    private Double dailyRepay;
    private String cover;
    private Integer instituteId;
    List<Integer> userId;
}