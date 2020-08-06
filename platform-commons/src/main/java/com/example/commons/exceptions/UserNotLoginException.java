package com.example.commons.exceptions;

/**
 * @Description:    未登录
 * @Author: mingyi ge
 * @CreateDate: 2019/6/12 10:08$
 */
public class UserNotLoginException extends BusinessException {

    private static final long serialVersionUID = -1879503946782379204L;

    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String msg) {
        super(msg);
    }

    public UserNotLoginException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

}
