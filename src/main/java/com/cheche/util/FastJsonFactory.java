package com.cheche.util;

import com.cheche.util.converter.FastjsonRequestConverter;
import com.cheche.util.converter.FastjsonResponseConverter;
import com.google.common.base.Charsets;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;


public class FastJsonFactory extends Converter.Factory {

    private Charset charset;

    public FastJsonFactory(Charset charset) {
        this.charset = charset;
    }

    public static FastJsonFactory create(Charset charset) {
        return new FastJsonFactory(charset);
    }

    public static FastJsonFactory create() {
        return create(Charsets.UTF_8);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastjsonRequestConverter<>(type,charset);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastjsonResponseConverter<>(type,charset);
    }
}
