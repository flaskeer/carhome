package com.hao.config;

import com.hao.util.csrf.CSRFInterceptor;
import com.hao.util.session.SessionInterceptor;
import com.hao.util.xss.XssFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by user on 2016/2/17
 */
@Configuration
@ComponentScan(basePackages = "com.hao.controller")
public class WebConfig extends WebMvcConfigurerAdapter{

    @Value("${login.exclude.uri}")
    private String[] excludeUris;


    @Bean(name = "sessionInterceptor")
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean(name = "csrfInterceptor")
    public CSRFInterceptor csrfInterceptor() {
        return new CSRFInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**").excludePathPatterns(excludeUris).excludePathPatterns("/user/isLogin");
        registry.addInterceptor(csrfInterceptor());
    }


    @Bean(name = "xssFilter")
    public XssFilter xssFilter() {
        return new XssFilter();
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(xssFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }


}
