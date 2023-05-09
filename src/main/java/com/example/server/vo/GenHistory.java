package com.example.server.vo;

import lombok.Data;

import java.util.Date;

@Data
public class GenHistory {
    private Date dayTime;

    private Double dayGen;

    private Double capacity;

    public GenHistory(Date dayTime, Double dayGen, Double capacity) {
        this.dayTime = dayTime;
        this.dayGen = dayGen;
        this.capacity = capacity;
    }

    public GenHistory() {
        this(null, null, null);
    }
}
