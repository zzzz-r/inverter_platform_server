package com.example.server.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class RedisUtils {
    private static StringRedisTemplate stringRedisTemplate;
    @Resource
    private StringRedisTemplate RedisTemplate;
    @PostConstruct
    public void setRedisTemplate() {
        stringRedisTemplate = RedisTemplate;
    }

    /**
     * 获取Redis缓存
     * @param key 缓存key
     * @return 缓存值
     */
    public static String getRedisCache(String key){
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(jsonStr)){
            return null;
        } else{
            return jsonStr;
        }
    }

    /**
     * 设置Redis缓存
     * @param key 缓存key
     * @param value 缓存值
     */
    public static void setRedisCache(String key, Object value){
        if (value == null) {
            return;
        }
        String jsonStr = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key, jsonStr);
    }

    // 删除缓存
    public static void flushRedisCache(String key) {
        stringRedisTemplate.delete(key);
    }
}
