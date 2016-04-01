package com.hao.config;

import com.google.common.base.Charsets;
import com.hao.util.csrf.CSRFInterceptor;
import com.hao.util.session.SessionInterceptor;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * Created by user on 2016/2/17
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport implements ResourceLoaderAware{


    private ResourceLoader resourceLoader;

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter(){
        return new ByteArrayHttpMessageConverter();
    }

    @Bean(name = "stringHttpMessageConverter")
    StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(Charsets.UTF_8);
    }

    @Bean(name = "resourceHttpMessageConverter")
    ResourceHttpMessageConverter resourceHttpMessageConverter() {
        return new ResourceHttpMessageConverter();
    }

    @Bean(name = "mappingJackson2HttpMessageConverter")
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }

    @Bean(name = "sourceHttpMessageConverter")
    SourceHttpMessageConverter sourceHttpMessageConverter() {
        return new SourceHttpMessageConverter();
    }

    @Bean(name = "jaxb2RootElementHttpMessageConverter")
    Jaxb2RootElementHttpMessageConverter jaxb2RootElementHttpMessageConverter() {
        return new Jaxb2RootElementHttpMessageConverter();
    }

    @Bean(name = "sessionInterceptor")
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean(name = "csrfInterceptor")
    public CSRFInterceptor csrfInterceptor() {
        return new CSRFInterceptor();
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(byteArrayHttpMessageConverter());
        converters.add(stringHttpMessageConverter());
        converters.add(resourceHttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
        converters.add(sourceHttpMessageConverter());
        converters.add(jaxb2RootElementHttpMessageConverter());
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor());
        registry.addInterceptor(csrfInterceptor());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
