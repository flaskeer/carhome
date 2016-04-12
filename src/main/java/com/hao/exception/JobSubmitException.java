package com.hao.exception;

/**
 * Created by user on 2016/4/12.
 */
public class JobSubmitException extends RuntimeException{

    public JobSubmitException() {
        super();
    }

    public JobSubmitException(String message) {
        super(message);
    }

    public JobSubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobSubmitException(Throwable cause) {
        super(cause);
    }
}
