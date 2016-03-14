package com.cheche.test;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

import static com.cheche.common.Commons.*;
/**
 * Created by user on 2016/2/17.
 */
public class TestFetcher {

    @Test
    public void testDocument() throws IOException {
        Document document = getDocument("http://www.autohome.com.cn/670/5232/options.html");
        System.out.println(document);
    }

    @Test
    public void testString() {
        String text = "安装位置: 后桥; 在每根车桥上的数量: 1; 减振器的类型: 机油压力; 减振器的结构: 可伸缩的保险杠; 减振器系统: 双管; 减振器的固定方式";
        String substring = text.substring(text.indexOf("安装位置: "), text.indexOf("安装位置: ") + 8);
        System.out.println(substring);
    }

}
