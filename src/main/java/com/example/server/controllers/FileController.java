package com.example.server.controllers;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.server.entity.Files;
import com.example.server.service.IStorageService;
import com.example.server.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequestMapping("/")
public class FileController {
    @Resource
    private IStorageService storageService;
    @Value("${upload.accessPath}") // Value注解获得application.yml中的值
    private String accessPath;
    @Value("${upload.localPath}")
    private String localPath;
    @PostMapping(value = "/upload") // 上传form-data，类型为file，key值需设置为file，与下对应
    public Result upload(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request; //转换request请求
        MultipartFile file = multipartRequest.getFile("file"); //获取文件
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        // 保存文件
        Files saveFile = storageService.saveFile(file, fileUUID, localPath);
        return Result.success(accessPath+saveFile.getName());
    }
}
