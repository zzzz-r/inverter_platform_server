package com.example.server.controllers;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.common.Constants;
import com.example.server.entity.InstituteTable;
import com.example.server.entity.User;
import com.example.server.exception.PoiException;
import com.example.server.service.IUserService;
import com.example.server.service.InstituteTableService;
import com.example.server.utils.RedisUtils;
import com.example.server.utils.TokenUtils;
import com.example.server.vo.Result;
import com.example.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/institute")
public class InstituteController {
    @Resource
    private InstituteTableService instituteTableService;
    @Resource
    private IUserService userService;

    @GetMapping("/list") // 列出该机构用户所属机构下的所有机构
    public Result listInstitute(){
        User user = TokenUtils.getCurrentUser();
        // 普通用户返回无权限
        if(user == null || user.getType() == 0)
            throw PoiException.NoRoot();
        List<InstituteTable> institutesVo = instituteTableService.listInstitute();
        return Result.success(institutesVo);
    }
    @GetMapping("/listUser/{id}") // 列出该机构用户所属机构下的所有用户,if_institute为true表示机构用户，否则业主
    public Result listUser(@PathVariable int id, boolean if_institute){ // id为机构id
        try{
            //先查找id下属机构
            List<InstituteTable> institutesVo = instituteTableService.findAllChildInstitute(id);;

            List<UserVo> userVos = new ArrayList<>();
            for(InstituteTable instituteTable: institutesVo){
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("institute_id", instituteTable.getId())
                            .eq("type", if_institute);
                List<User> uses = userService.list(queryWrapper);
                userVos.addAll(uses.stream()
                        .map(u -> {
                            UserVo userVo = new UserVo();
                            userVo.setId(u.getId());
                            userVo.setUserName(u.getUserName());
                            userVo.setEmail(u.getEmail());
                            userVo.setPhone(u.getPhone());
                            userVo.setInstituteId(u.getInstituteId());
                            userVo.setCreateTime(u.getCreateTime());
                            return userVo;
                        })
                        .collect(Collectors.toList()));
            }
            return Result.success(userVos);
        }catch (Exception e){
//            e.printStackTrace();
            return Result.fail();
        }
    }

    @PostMapping("/save") // 保存或更新
    public Result saveInstitute(@RequestBody InstituteTable instituteTable){
        boolean flag = instituteTableService.saveOrUpdate(instituteTable);
        if(flag) {
//            RedisUtils.flushRedisCache(Constants.INSTITUTES);
            return Result.success();
        }
        else
            return Result.fail();
    }

    @DeleteMapping("/delete/{id}") //删除
    public Result deleteInstitute(@PathVariable int id){
        boolean flag = instituteTableService.removeById(id);
        if(flag) {
//            RedisUtils.flushRedisCache(Constants.INSTITUTES);
            return Result.success();
        }
        else
            return Result.fail();
    }
}
