package com.example.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.entity.PlantPowerHistory;
import com.example.server.service.PlantPowerHistoryService;
import com.example.server.mapper.PlantPowerHistoryMapper;
import com.example.server.vo.GenHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author 86130
* @description 针对表【plant_power_history】的数据库操作Service实现
* @createDate 2023-04-29 11:49:38
*/
@Service
public class PlantPowerHistoryServiceImpl extends ServiceImpl<PlantPowerHistoryMapper, PlantPowerHistory>
    implements PlantPowerHistoryService{
    @Resource
    private PlantPowerHistoryMapper plantPowerHistoryMapper;
    public List<PlantPowerHistory> selectByPlantId(Integer plantId){
        return plantPowerHistoryMapper.selectByPlantId(plantId);
    }

    public List<GenHistory> countGenHistory(List<Integer> plantIdList){
        List<GenHistory> genHistoryList = new ArrayList<>();
        // 遍历plantIdList，对于每个电站id，查询该电站对应的发电历史信息列表
        for (Integer plantId : plantIdList) {
            List<PlantPowerHistory> plantPowerHistoryList = selectByPlantId(plantId);
            for (PlantPowerHistory plantPowerHistory : plantPowerHistoryList) {
                Date dayTime = plantPowerHistory.getDayTime();
                boolean found = false;
                // 遍历每个电站的发电历史信息列表，对于每条记录，判断该记录的日期是否已存在于统计数据中
                for (GenHistory genHistory : genHistoryList) {
                    if (genHistory.getDayTime().equals(dayTime)) {
                        //如果日期已存在，则将该记录的日发电量dayGen累加到对应日期的总发电量中
                        if(plantPowerHistory.getDayGen() != null)
                            genHistory.setDayGen(genHistory.getDayGen() + plantPowerHistory.getDayGen());
                        if(plantPowerHistory.getCapacity() != null)
                            genHistory.setCapacity(genHistory.getCapacity() + plantPowerHistory.getCapacity());
                        found = true;
                        break;
                    }
                }
                //如果日期不存在，则创建一个新的GenHistory对象，并将该记录的日期和日发电量赋值给该对象，然后将该对象添加到统计数据列表中
                if (!found) {
                    GenHistory genHistory = new GenHistory();
                    genHistory.setDayTime(dayTime);
                    genHistory.setDayGen(plantPowerHistory.getDayGen());
                    genHistory.setCapacity(plantPowerHistory.getCapacity());
                    genHistoryList.add(genHistory);
                }
            }
        }
        return genHistoryList;
    }
}




