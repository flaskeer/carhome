package com.hao.model;

import com.hao.model.vo.UserVo;

import java.io.Serializable;

/**
 * Created by user on 2016/2/24.
 */
public class UserResponse implements Serializable{

    /**
     * 相应结果状态 标记成功失败
     */
    private boolean success;

    /**
     * 响应信息
     */
    private String response;

    /**
     * 注册成功的用户
     */
    private UserVo userVo;

    public UserResponse() {
        this.success = true;
    }

    public UserResponse(String response) {
        this.success = false;
        this.response = response;
    }

    public UserResponse(UserVo userVo) {
        this.success = true;
        this.userVo = userVo;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }
}
