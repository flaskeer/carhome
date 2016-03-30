package com.cheche.model.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.cheche.util.MapConveter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * kong API请求信息实体
 * --
 --     /\  ____
 --     <> ( oo )
 --     <>_| ^^ |_
 --     <>   @    \
 --    /~~\ . . _ |
 --   /~~~~\    | |
 --  /~~~~~~\/ _| |
 --  |[][][]/ / [m]
 --  |[][][[m]
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[][][]|
 --  |[|--|]|
 --  |[|  |]|
 --  ========
 -- ==========
 -- |[[    ]]|
 * Created by user on 2016/3/30.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Api implements Serializable{

    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "request_host")
    private String requestHost;

    @JSONField(name = "request_path")
    private String requestPath;

    @JSONField(name = "strip_request_path")
    private boolean stripRequestPath;

    @JSONField(name = "preserve_host")
    private boolean preserveHost;

    @JSONField(name = "upstream_url")
    private String upstreamUrl;

    @JSONField(name = "created_at")
    private Long createAt;

    @JSONField(name = "size")
    private Integer size;

    @JSONField(name = "offset")
    private String offset;

    @JSONField(serialize = false,deserialize = false)
    private String errorMessage;

    public Map<String,Object> toMap() {
        return MapConveter.convert(this);
    }


}
