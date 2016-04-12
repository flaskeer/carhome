package com.hao.job;

/**
 * Created by user on 2016/4/12.
 */
public class Result {

    private String msg;

    private Action action;

    public Result(String msg, Action action) {
        this.msg = msg;
        this.action = action;
    }

    public Result(Action action) {
        this.action = action;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
