package com.example.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${upload.accessPath}")
    private String accessPath;
    @Value("${upload.localPath}")
    private String localPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){ //处理资源访问
        //访问accessPath路径下的所有文件，将被映射到localPath下并访问对应文件
        registry.addResourceHandler(accessPath+"**").addResourceLocations("file:"+localPath);
    }
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {  //跨域访问
        corsRegistry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
