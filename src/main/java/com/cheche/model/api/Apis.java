package com.cheche.model.api;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 2016/3/30.
 */
@Setter
@Getter
public class Apis implements Serializable{

    @JSONField(name = "data")
    private List<Api> data;

    @JSONField(name = "total")
    private Integer total;

    @JSONField(name = "next")
    private String next;

    @JSONField(name = "message")
    private String message;

}
