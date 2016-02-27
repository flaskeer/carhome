package com.cheche.fetcher;

import com.cheche.util.HttpClientUtil;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
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

    /**
     * 解析http://www.autohome.com.cn/grade/carhtml/A.html这种格式的页面  按照三层目录层级排放
     * http://car1.autoimg.cn/logo/brand/50/129472203719848750.jpg,奥迪,奥迪(进口),Crosslane Coupe,http://www.autohome.com.cn/2908/#levelsource=000000000_0&pvareaid=101594
     *http://car1.autoimg.cn/logo/brand/50/129472203719848750.jpg,奥迪,奥迪(进口),奥迪TT offroad,http://www.autohome.com.cn/3479/#levelsource=000000000_0&pvareaid=101594
     *http://car1.autoimg.cn/logo/brand/50/129472203719848750.jpg,奥迪,奥迪(进口),e-tron quattro,http://www.autohome.com.cn/3894/#levelsource=000000000_0&pvareaid=101594
     *http://car1.autoimg.cn/logo/brand/50/129472203719848750.jpg,奥迪,奥迪(进口),Nanuk,http://www.autohome.com.cn/3210/#levelsource=000000000_0&pvareaid=101594
     *http://car1.autoimg.cn/logo/brand/50/129472203719848750.jpg,奥迪,奥迪(进口),quattro,http://www.autohome.com.cn/2218/#levelsource=000000000_0&pvareaid=101594
     * @param url
     * @return
     * @throws IOException
     */
    private static Object fetchSinglePageLink(String url) throws IOException {
        List<String> singlePageLinks = Lists.newArrayList();
        List<String> grayPageLinks = Lists.newArrayList();
        StringBuilder builder = new StringBuilder();
        Document document = getDocument(url);
        Elements dlElems = document.select("dl");
        for (int i = 0; i < dlElems.size(); i++) {
            String img = dlElems.get(i).select("dt > a > img").attr("src");
            String firstBrand = dlElems.get(i).select("dt > div > a").text();
            Elements divElems = dlElems.get(i).select("dd > div.h3-tit");
            for (int j = 0; j < divElems.size(); j++) {
                String secondBrand = divElems.get(j).text();
                Element ulElem = dlElems.get(i).select("dd > ul").get(j);
                Elements liElems = ulElem.select("li");
                for (Element liElem : liElems) {
                    Elements aElems = liElem.select("h4 > a");
                    if(aElems.hasClass("greylink")) {
                        String thirdBrand = aElems.text();
                        builder.append(img).append(",").append(firstBrand).append(",")
                                .append(secondBrand).append(",").append(thirdBrand).append(",").append(aElems.attr("href")).append("\n");
                    }else {
                        String thirdBrand = aElems.text();
                        builder.append(img).append(",").append(firstBrand).append(",")
                                .append(secondBrand).append(",").append(thirdBrand).append(",").append(aElems.attr("href")).append("\n");
                    }
                }

            }
        }

//        Pair<List<String>, List<String>> pair = Pair.of(singlePageLinks, grayPageLinks);
        return builder.toString();
    }



    /**
     * 用来将所有车型写入文件
     */
//    public static void getAndWriteLinks(String filePath){
//        List<String> pages = pages();
//        pages.forEach(pageUrl ->{
//            try {
//                List<String> singlePageLinks = fetchSinglePageLink(pageUrl);
//                singlePageLinks.forEach(link ->{
//                    if(logger.isInfoEnabled()){
//                        logger.info("link is:{}",link);
//                    }
//                    try {
//                        FileUtils.writeStringToFile(new File(filePath),link + "\n",true);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//    }


    public static void main(String[] args) {
//        getAndWriteLinks("");
        try {
//            Document document = getDocument("http://www.autohome.com.cn/grade/carhtml/C.html");
//            System.out.println(document);
            Object o = fetchSinglePageLink("http://www.autohome.com.cn/grade/carhtml/A.html");
            System.out.println(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
