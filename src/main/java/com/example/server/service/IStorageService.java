package com.example.server.service;

import com.example.server.entity.Files;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    Files saveFile(MultipartFile file, String fileName, String filePath); //保存上传文件
}
