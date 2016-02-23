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

}
