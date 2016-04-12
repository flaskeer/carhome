package com.hao.job;

/**
 * Created by user on 2016/4/12.
 */
public class Command {

    private int code;
    private Body body;

    public static Command createRequestCommand(int code,Body body) {
        Command command = new Command();
        command.setCode(code);
        command.setBody(body);
        return command;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
