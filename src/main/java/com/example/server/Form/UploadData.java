package com.example.server.Form;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UploadData {
    private Integer dtuId;
    private List<MiData> miDataList;
}
