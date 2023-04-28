package com.example.server.enums;

public enum ResultEnum {
    //枚举变量
    SUCCESS(0,"操作成功"),
    ERROR_UNKNOWN(-1,"未知错误"),
    ERROR_NOT_FOUND(1,"资源未找到"),
    ERROR_OPERATION(2,"操作不成功")
    ;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    int code;
    String msg;

    ResultEnum(int code, String msg){
        this.msg=msg;
        this.code=code;
    }
}
