package com.dechain.depay.exception;

/**
 * API连接异常
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:31
 */
public class APIConnectionException extends DePayException {

    private static final long serialVersionUID = -8764189839522042543L;

    public APIConnectionException(String message) {
        super(message);
    }

    public APIConnectionException(String message, Throwable e) {
        super(message, e);
    }

}
