package com.hao.enums;

/**
 * Created by user on 2016/4/12.
 */
public enum  RequestCode {
    SUBMIT_JOB(1);

    private int code;

    RequestCode(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    public static RequestCode valueOf(int code) {
        for (RequestCode requestCode : RequestCode.values()) {
            if (requestCode.code == code) {
                return requestCode;
            }
        }
        throw new IllegalArgumentException("can not found the response code");
    }

}
