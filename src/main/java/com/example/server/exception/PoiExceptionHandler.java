package com.example.server.exception;

import com.example.server.enums.ResultEnum;
import com.example.server.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PoiExceptionHandler {
    @ExceptionHandler(PoiException.class) //处理PoiException类型的异常，每当抛出一个PoiException异常就被该函数捕获
    public Result poiExceptionHandler(Exception e){
        if (e.getMessage().endsWith(ResultEnum.ERROR_NOT_FOUND.getMsg())) {
            return Result.fail(ResultEnum.ERROR_NOT_FOUND);
        }else if (e.getMessage().endsWith(ResultEnum.ERROR_OPERATION.getMsg())){
            return Result.fail(ResultEnum.ERROR_OPERATION);
        }else if (e.getMessage().endsWith(ResultEnum.ERROR_LOGIN.getMsg())){
            return Result.fail(ResultEnum.ERROR_LOGIN);
        }else if (e.getMessage().endsWith(ResultEnum.ERROR_REGISTER.getMsg())) {
            return Result.fail(ResultEnum.ERROR_REGISTER);
        }else if (e.getMessage().endsWith(ResultEnum.ERROR_TOKEN.getMsg())) {
            return Result.fail(ResultEnum.ERROR_TOKEN);
        }else if (e.getMessage().endsWith(ResultEnum.ERROR_ROOT.getMsg())) {
            return Result.fail(ResultEnum.ERROR_ROOT);
        }
        return Result.fail();
    }
    @ExceptionHandler(Exception.class) //全局异常捕获
    public Result serverErrorHandler(Exception e){
        return Result.fail();
    }
}
