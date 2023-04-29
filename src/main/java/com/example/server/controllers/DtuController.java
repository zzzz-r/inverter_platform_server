package com.example.server.controllers;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.entity.DtuTable;
import com.example.server.entity.MiAlarmTable;
import com.example.server.exception.PoiException;
import com.example.server.service.DtuTableService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dtu")
public class DtuController {
    @Resource
    private DtuTableService dtuTableService;
    @GetMapping("/list/{plantId}")
    public Result list(@PathVariable int plantId){
        List<DtuList> dtuLists = dtuTableService.listDtuByPlantId(plantId);
        return Result.success(dtuLists);
    }

    @GetMapping("/alarm")
    public Result listAlarmDtu(){
        try{
            QueryWrapper<DtuTable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("state", 1);
            List<DtuTable> dtuTableList = dtuTableService.list(queryWrapper);
            return Result.success(dtuTableList);
        }catch (Exception e){
            e.printStackTrace();
            throw PoiException.OperateFail();
        }
    }
}
