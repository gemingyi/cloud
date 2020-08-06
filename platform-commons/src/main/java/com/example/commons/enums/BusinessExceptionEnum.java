package com.example.commons.enums;


import com.example.commons.exceptions.*;

/**
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 10:01$
 */
public enum BusinessExceptionEnum {

    /**
     * 数据未找到
     */
    NOT_FOUND(DataNotFoundException.class, ResultCode.RESULT_DATA_NONE),

    /**
     * 接口方法不允许
     */
//    METHOD_NOT_ALLOWED(MethodNotAllowException.class, HttpStatus.METHOD_NOT_ALLOWED, ResultCode.INTERFACE_ADDRESS_INVALID),

    /**
     * 数据已存在
     */
    CONFLICT(DataConflictException.class, ResultCode.DATA_ALREADY_EXISTED),

    /**
     * 用户未登录
     */
    UNAUTHORIZED(UserNotLoginException.class, ResultCode.USER_NOT_LOGGED_IN),

    /**
     * 无访问权限
     */
    FORBIDDEN(PermissionForbiddenException.class, ResultCode.PERMISSION_NO_ACCESS),

    /**
     * 远程访问时错误
     */
//    REMOTE_ACCESS_ERROR(RemoteAccessException.class, HttpStatus.INTERNAL_SERVER_ERROR, ResultCode.INTERFACE_OUTTER_INVOKE_ERROR),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(InternalServerException.class, ResultCode.SYSTEM_INNER_ERROR);


    private Class<? extends BusinessException> eClass;

    private ResultCode resultCode;


    BusinessExceptionEnum(Class<? extends BusinessException> eClass, ResultCode resultCode) {
        this.eClass = eClass;
        this.resultCode = resultCode;
    }

    public Class<? extends BusinessException> getEClass() {
        return eClass;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }


    public static boolean isSupportException(Class<?> z) {
        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (exceptionEnum.eClass.equals(z)) {
                return true;
            }
        }
        return false;
    }

    public static BusinessExceptionEnum getByEClass(Class<? extends BusinessException> eClass) {
        if (eClass == null) {
            return null;
        }

        for (BusinessExceptionEnum exceptionEnum : BusinessExceptionEnum.values()) {
            if (eClass.equals(exceptionEnum.eClass)) {
                return exceptionEnum;
            }
        }
        return null;
    }
}
