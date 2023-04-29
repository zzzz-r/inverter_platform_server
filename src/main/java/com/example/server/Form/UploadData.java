package com.example.server.Form;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UploadData {
    private Object dtuId;
    private Date updateTime;
    private List<MiData> miDataList;
}
