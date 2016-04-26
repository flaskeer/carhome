package com.hao.enums;

/**
 * Created by user on 2016/2/24.
 */
public enum  TypeMessageEnum {

    ERROR("danger"),
    INFO("info"),
    SUCCESS("success"),
    WARN("warning");

    private String type;

    TypeMessageEnum(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
