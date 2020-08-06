package com.example.payserver.handler;

import com.example.commons.enums.ResultCode;
import com.example.commons.exceptions.BusinessException;
import com.example.commons.result.SysErrorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 单体全局异常处理
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 15:54$
 */
@RestControllerAdvice
public class PayServiceGlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(PayServiceGlobalExceptionHandler.class);


    /**
     * 处理请求参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SysErrorResult<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            sb.append(fieldError.getField()).append("字段：").append(fieldError.getDefaultMessage());
        }
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(ResultCode.PARAM_IS_INVALID, e, sb);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SysErrorResult<Object>> handleBusinessException(BusinessException e, HttpServletRequest request) {
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<SysErrorResult<Object>> handleThrowable(Throwable e, HttpServletRequest request) {
        SysErrorResult<Object> defaultErrorResult = SysErrorResult.failure(ResultCode.SYSTEM_INNER_ERROR, e, null);
        //TODO 可通过邮件、发送信息至开发人员、记录存档等操作
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultErrorResult);
    }

}
