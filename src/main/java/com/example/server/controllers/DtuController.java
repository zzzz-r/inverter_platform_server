package com.example.server.controllers;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.entity.DtuTable;
import com.example.server.entity.MiAlarmTable;
import com.example.server.entity.PlantBasicInfo;
import com.example.server.exception.PoiException;
import com.example.server.service.DtuTableService;
import com.example.server.service.IPlantBasicInfoService;
import com.example.server.service.InstituteTableService;
import com.example.server.vo.DtuAlarmVo;
import com.example.server.vo.DtuList;
import com.example.server.vo.MiAlarmVo;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dtu")
public class DtuController {
    @Resource
    private DtuTableService dtuTableService;
    @Resource
    private IPlantBasicInfoService plantBasicInfoService;
    @Resource
    private InstituteTableService instituteTableService;
    @GetMapping("/list/{plantId}")
    public Result list(@PathVariable int plantId){
        List<DtuList> dtuLists = dtuTableService.listDtuByPlantId(plantId);
        return Result.success(dtuLists);
    }

    @GetMapping("/alarm")
    public Result listAlarmDtu(){
        try{
            List<DtuAlarmVo> dtuAlarmVoList = instituteTableService.listInstitute().stream()
                    .flatMap(institute -> dtuTableService.listAlarmByInstitute(institute.getId()).stream()
                            .map(item -> {
                                DtuAlarmVo dtuAlarmVo = new DtuAlarmVo();
                                BeanUtils.copyProperties(item, dtuAlarmVo);
                                PlantBasicInfo plant = plantBasicInfoService.getById((Serializable) item.getPlantId());
                                if(plant != null){
                                    dtuAlarmVo.setPlantName(plant.getName());
                                }
                                return dtuAlarmVo;
                            }))
                    .collect(Collectors.toList());
            return Result.success(dtuAlarmVoList);
        }catch (Exception e){
            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }
}
