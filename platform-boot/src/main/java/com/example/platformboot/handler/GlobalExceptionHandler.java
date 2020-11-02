package com.example.platformboot.handler;

import com.example.commons.exceptionHandle.exceptions.InternalServerException;
import com.example.commons.result.ResultCode;
import com.example.commons.exceptionHandle.exceptions.BusinessException;
import com.example.commons.result.SysErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description: 单体全局异常处理
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 15:54$
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     *  处理RequestBody参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SysErrorResult<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(ResultCode.PARAM_IS_INVALID, e);
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(ResultCode.PARAM_IS_INVALID, e.getBindingResult().getFieldError().getDefaultMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SysErrorResult<Object>> handleBusinessException(BusinessException e) {
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<SysErrorResult<Object>> handleThrowable(Throwable e) {
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(new InternalServerException());
        //TODO 可通过邮件、发送信息至开发人员、记录存档等操作
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

}
