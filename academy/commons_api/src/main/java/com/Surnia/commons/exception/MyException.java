package com.Surnia.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MyException
 * @Description TODO
 * @Author Surnia
 * @Date 2021/8/27
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends RuntimeException {

    private Integer code;
    private String exceptionMessage;

}
