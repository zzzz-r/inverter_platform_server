package com.example.server.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.server.entity.Files;
import com.example.server.mapper.FilesMapper;
import com.example.server.service.IStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class StorageServiceImpl implements IStorageService {
    @Resource
    private FilesMapper filesMapper;
    @Override
    public Files saveFile(MultipartFile file, String fileName, String filePath){
        String path = filePath + fileName;
        File targetFile = new File(path);
        if(!targetFile.getParentFile().exists()){ //如果文件路径不存在则创建对应目录
            targetFile.getParentFile().mkdirs();
        }
        try {
            // 获取文件的md5
            String md5 = SecureUtil.md5(file.getInputStream());
            // 从数据库查询是否存在相同的记录
            Files dbFiles = getFileByMd5(md5);
            if (dbFiles != null) {
                path = dbFiles.getUrl();
                fileName = dbFiles.getName();
            } else {
                FileCopyUtils.copy(file.getBytes(), targetFile); //将file以二进制形式写入磁盘targetFile
            }

            //将文件保存到数据库
            String type = FileUtil.extName(fileName);
            long size = file.getSize();

            Files saveFile = new Files();
            saveFile.setName(fileName);
            saveFile.setType(type);
            saveFile.setSize(size/1024); // 单位 kb
            saveFile.setUrl(path);
            saveFile.setMd5(md5);
            filesMapper.insert(saveFile);

            return saveFile;
        } catch (IOException e) {
            return null;
        }
    }

    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
}
