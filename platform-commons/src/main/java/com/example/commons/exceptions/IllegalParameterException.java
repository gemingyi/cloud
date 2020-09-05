package com.example.commons.exceptions;

/**
 * 参数不合法
 */
public class IllegalParameterException extends BusinessException {

    private static final long serialVersionUID = 2659909836556958676L;

    public IllegalParameterException() {
        super();
    }

    public IllegalParameterException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalParameterException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }

    public IllegalParameterException(String msg) {
        super(msg);
    }

    public IllegalParameterException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }
}
