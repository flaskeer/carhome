package com.hao.util.converter;


import com.alibaba.fastjson.JSON;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class FastjsonResponseConverter<T> implements Converter<ResponseBody,T> {

    private Type type;
    private Charset charset;

    public FastjsonResponseConverter(Type type, Charset charset) {
        this.type = type;
        this.charset = charset;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            return JSON.parseObject(value.toString(), type);
        } finally {
            value.close();
        }
    }
}
