package com.cheche.kong.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 请求结果响应实体
 * Created by user on 2016/3/30.
 */
@Getter
@Setter
@Builder
public class RestfulResponse<T> implements Serializable {

    private boolean success;

    private String code;

    private T msg;

    /**
     * 总数
     */
    private int results;

    private List<?> rows;

    public RestfulResponse(T msg) {
        if (msg != null) {
            this.success = false;
            this.msg = msg;
        }
    }
}
