package com.cheche.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;


public class MapConveter {

    private MapConveter() {}

    /**
     * 将对象转换为JSON  然后转成map
     * @param object
     * @return
     */
    public static Map<String,Object> convert(Object object) {
        String jsonStr = JSON.toJSONString(object);
        return JSON.parseObject(jsonStr,new TypeReference<Map<String, Object>>() {});
    }

}
