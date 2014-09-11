package org.mybatis.guice;

public final class CustomException extends Exception {

    private static final long serialVersionUID = 1L;

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}