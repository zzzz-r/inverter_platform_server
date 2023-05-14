package com.example.server.controllers;

import com.example.server.Form.PlantForm;
import com.example.server.common.Constants;
import com.example.server.entity.DtuTable;
import com.example.server.entity.PlantInfoTable;
import com.example.server.exception.PoiException;
import com.example.server.service.DtuTableService;
import com.example.server.service.InstituteTableService;
import com.example.server.service.PlantInfoTableService;
import com.example.server.utils.RedisUtils;
import com.example.server.vo.DtuAlarmVo;
import com.example.server.vo.DtuList;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dtu")
public class DtuController {
    @Resource
    private DtuTableService dtuTableService;
    @Resource
    private PlantInfoTableService plantBasicInfoService;
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
                                PlantInfoTable plant = plantBasicInfoService.getById(item.getPlantId());
                                if(plant != null){
                                    dtuAlarmVo.setPlantName(plant.getName());
                                }
                                return dtuAlarmVo;
                            }))
                    .collect(Collectors.toList());
            return Result.success(dtuAlarmVoList);
        }catch (Exception e){
//            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }

    @PostMapping("/add") // 添加
    public Result add(@RequestBody DtuTable dtuTable){
        try{
            dtuTableService.save(dtuTable);
        }catch (Exception e){
            throw PoiException.OperateFail();
        }
        return Result.success();
    }

    @DeleteMapping("/delete/{id}") //删除
    public Result delete(@PathVariable int id){
        try{
            dtuTableService.removeById(id);
        }catch (Exception e){
            throw PoiException.OperateFail();
        }
        return Result.success();
    }
}
