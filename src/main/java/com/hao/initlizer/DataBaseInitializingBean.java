package com.hao.initlizer;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * 第一次启动时初始化数据库使用
 * Created by user on 2016/4/5.
 */
@Component
public class DataBaseInitializingBean implements InitializingBean {



    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
