package com.example.server.exception;

import com.example.server.enums.ResultEnum;
import com.example.server.mapper.PoiMapper;

public class PoiException extends RuntimeException{
    private PoiException(String msg){
        super(msg); //调用父类构造函数
    }
    //以下是不同类型的异常
    public static PoiException NotFound(){
        return new PoiException(ResultEnum.ERROR_NOT_FOUND.getMsg());
    }
    public static PoiException OperateFail(){
        return new PoiException(ResultEnum.ERROR_OPERATION.getMsg());
    }
    public static PoiException Unknown(){
        return new PoiException(ResultEnum.ERROR_UNKNOWN.getMsg());
    }
}
