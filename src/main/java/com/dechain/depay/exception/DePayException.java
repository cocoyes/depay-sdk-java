package com.dechain.depay.exception;

/**
 * DePay异常抽象类
 * @author apachemoy
 * @site
 * @date 2021-06-08 11:00
 */
public abstract class DePayException extends Exception {

    private static final long serialVersionUID = 2566087783987900120L;

    private int statusCode;

    public DePayException(String message) {
        super(message, null);
    }

    public DePayException(String message, Throwable e) {
        super(message, e);
    }

    public DePayException(String message, int statusCode, Throwable e) {
        super(message, e);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(super.toString());
                return sb.toString();
    }
}
