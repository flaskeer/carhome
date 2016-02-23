package com.cheche.fetcher;

import com.cheche.util.HttpClientUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016/2/17.
 */
public class FetcherLink {

    public static final Logger logger = LoggerFactory.getLogger(FetcherLink.class);

    private static Document getDocument(String url) throws IOException {
        String content = HttpClientUtil.sendHttpsGet(url);
        Document document = Jsoup.parse(content);
        return document;

    }

    /**
     * 得到所有的页面
     * @return
     */
    private static List<String> pages(){
        List<String> urls = Lists.newArrayList();
        for(char i = 65; i < 91;i++){
            String url = "http://www.autohome.com.cn/grade/carhtml/" + i + ".html";
            urls.add(url);
        }

        return urls;
    }

    private static List<String> fetchSinglePageLink(String url) throws IOException {
        List<String> singlePageLinks = Lists.newArrayList();
        Document document = getDocument(url);
        Elements liElems = document.select("dd > ul > li");
        for (Element liElem : liElems) {
            String href = liElem.select("h4 > a").attr("href");
            if(href.isEmpty()){
                continue;
            }
            singlePageLinks.add(href);
        }
        return singlePageLinks;
    }

    /**
     * 用来将所有车型写入文件
     */
    public static void getAndWriteLinks(String filePath){
        List<String> pages = pages();
        pages.forEach(pageUrl ->{
            try {
                List<String> singlePageLinks = fetchSinglePageLink(pageUrl);
                singlePageLinks.forEach(link ->{
                    if(logger.isInfoEnabled()){
                        logger.info("link is:{}",link);
                    }
                    try {
                        FileUtils.writeStringToFile(new File(filePath),link + "\n",true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        getAndWriteLinks("");
        try {
            Document document = getDocument("http://www.autohome.com.cn/grade/carhtml/C.html");
            System.out.println(document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
