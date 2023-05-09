package com.example.server.vo;

import lombok.Data;
import java.util.List;

@Data
public class CountVo {
    private List<GenHistory>  genHistory;
    private Integer miNum;
    private Integer dtuNum;
    private Integer onlineNum;
    private Integer offlineNum;
    private Integer buildNum;
    private Integer alarmNum;
    private Integer noAlarmNum;
}
