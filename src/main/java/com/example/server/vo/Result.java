package com.example.server.vo;

import com.example.server.enums.ResultEnum;

// 返回操作结果
public class Result<T> {
    public int code;
    public String msg;
    public T data;
    //静态方法，自动创建可以直接调用，返回Result<T>型数据
    public static <T> Result success(){
        Result t = new Result(ResultEnum.SUCCESS.getMsg(), ResultEnum.SUCCESS.getCode(),null);
        return t;
    }
    public static <T> Result success(T data){
        Result t = new Result(ResultEnum.SUCCESS.getMsg(), ResultEnum.SUCCESS.getCode(),data);
        return t;
    }
    public static <T> Result fail(){
        Result t = new Result(ResultEnum.ERROR_UNKNOWN.getMsg(), ResultEnum.ERROR_UNKNOWN.getCode(),null);
        return t;
    }
    public static <T> Result fail(ResultEnum rEnum){
        Result t = new Result(rEnum.getMsg(), rEnum.getCode(), null);
        return t;
    }
    public Result(String msg,int code,T data){
        this.msg=msg;
        this.code=code;
        this.data=data;
    }
}
