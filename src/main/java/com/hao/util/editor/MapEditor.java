package com.hao.util.editor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Map;

/**
 * Created by user on 2016/4/1.
 */
public class MapEditor extends PropertyEditorSupport{

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            setValue(JSON.parseObject(text,new TypeReference<Object>(){}));
        }
        super.setAsText(text);
    }

    @Override
    public String getAsText() {
        Map<?,?> value = (Map<?, ?>) getValue();
        if (value == null) {
            return "";
        }
        return JSON.toJSONString(value);
    }
}
