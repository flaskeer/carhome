package com.hao.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 该接口不能被扫描到
 * Created by user on 2016/4/15.
 */
public interface MyMapper<T> extends Mapper<T>,MySqlMapper<T>{
    //该接口被扫描到会报错
}
