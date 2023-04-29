package com.example.server.Form;

import lombok.Data;

@Data
public class MiData {
    private Integer id;
    private Double power;
    private Double dayGen;
    private Double temperature;
    private Double freq;
    private Double dcCurrent;
    private Double dcVoltage;
    private Double acCurrent;
    private Double acVoltage;
    private Double gridVoltage;
}
