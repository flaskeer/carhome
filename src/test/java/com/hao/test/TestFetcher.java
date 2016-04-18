package com.hao.test;

import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.hao.common.Commons.*;
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

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        Object[] array = new Object[100];
//        Arrays.setAll(array, index -> index);
        Arrays.parallelSetAll(array, index -> index);
        long time = System.currentTimeMillis() - start;
        System.out.println(time);
    }

    @Test
    public void test2() {
//        LinkedHashMap generalForm = new LinkedHashMap(){{
//            put("__EVENTTARGET","");
//            put("__EVENTARGUMENT","");
//            put("__LASTFOCUS","");
//            put("__VIEWSTATE","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");
//            put("__VIEWSTATEGENERATOR","EDD8C9AE");
//        }};
//        generalForm.put("__VIEWSTATE","222");
//        System.out.println(generalForm);
    }

}
