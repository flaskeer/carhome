package com.hao.spider;

import com.hao.model.spider.Page;

import java.util.Map;

/**
 * Created by user on 2016/4/13.
 */
public interface PageParser {

    Map<String,Object> parse(Page page);

}
