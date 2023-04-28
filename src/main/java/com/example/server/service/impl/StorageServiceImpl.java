package com.example.server.service.lmpl;

import com.example.server.service.IStorageService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class StorageServiceImpl implements IStorageService {
    @Override
    public Boolean save(MultipartFile file, String fileName, String filePath){
        String path = filePath + fileName;
        File targetFile = new File(path);
        if(!targetFile.getParentFile().exists()){ //如果文件路径不存在则创建对应目录
            targetFile.getParentFile().mkdirs();
        }
        try {
            FileCopyUtils.copy(file.getBytes(), targetFile); //将file以二进制形式写入targetFile
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
