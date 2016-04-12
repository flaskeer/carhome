package com.hao.job;

import com.google.common.base.Objects;

/**
 *
 * 返回给客户端的响应
 * Created by user on 2016/4/12.
 */
public class Response {

    private boolean success;

    private String msg;

    private String code;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("success", success)
                .add("msg", msg)
                .add("code", code)
                .toString();
    }
}
