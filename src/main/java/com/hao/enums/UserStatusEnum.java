package com.hao.enums;

/**
 * Created by user on 2016/4/19.
 */
public enum  UserStatusEnum {

    AWAIT("待激活"),
    NORMAL("正常状态"),
    TEMP_FORZEN("临时状态"),
    FORZEN("冻结"),
    DELETED("已删除");

    private String description;

    UserStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
