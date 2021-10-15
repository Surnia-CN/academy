package com.Surnia.commons.handler.exceptionHandler;

import com.Surnia.commons.exception.MyException;
import com.Surnia.commons.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName MyGlobalExceptionHandler
 * @Description 全局异常处理器
 * @Author Surnia
 * @Date 2021/6/22
 * @Version 1.0
 */

@ControllerAdvice
public class MyGlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result globalException(Exception e) {
        e.printStackTrace();
        return Result.error().message(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MyException.class)
    public Result myException(MyException e) {
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getExceptionMessage());
    }

}
