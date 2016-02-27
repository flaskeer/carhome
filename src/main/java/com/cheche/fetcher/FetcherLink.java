package com.cheche.fetcher;

import com.cheche.model.Price;
import com.cheche.model.StopSale;
import com.cheche.parser.ParserHomePage;
import com.cheche.parser.ParserSpecificPage;
import com.cheche.util.HttpClientUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private static Pair<Map<String, String>, Map<String, String>> fetchSinglePageLink(String url) throws IOException {
        Map<String,String> singlePageMap = Maps.newLinkedHashMap();
        Map<String,String> grayPageMap = Maps.newLinkedHashMap();
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
                    if(aElems.text().isEmpty()){
                        continue;
                    }
                    if(aElems.hasClass("greylink")) {
                        String thirdBrand = aElems.text();
                        grayPageMap.put(aElems.attr("href"),firstBrand + "," + img + "," + secondBrand + "," + thirdBrand + ",");
//                        builder.append(img).append(",").append(firstBrand).append(",")
//                                .append(secondBrand).append(",").append(thirdBrand).append(",").append(aElems.attr("href")).append("\n");
                    }else {
                        String thirdBrand = aElems.text();
                        singlePageMap.put(aElems.attr("href"),firstBrand + "," + img + "," + secondBrand + "," + thirdBrand + ",");
//                        builder.append(img).append(",").append(firstBrand).append(",")
//                                .append(secondBrand).append(",").append(thirdBrand).append(",").append(aElems.attr("href")).append("\n");
                    }
                }

            }
        }

        Pair<Map<String, String>, Map<String, String>> mapPair = Pair.of(singlePageMap, grayPageMap);
        return mapPair;
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

    public static Object get() throws IOException{
        List<String> pages = pages();
        StringBuilder builder = new StringBuilder();
        pages.forEach(pageUrl ->{
            try {
                Pair<Map<String, String>, Map<String, String>> mapPair = fetchSinglePageLink(pageUrl);
                Map<String, String> singleMap = mapPair.getLeft();
                Map<String, String> grayMap = mapPair.getRight();
                //处理带有配置参数的页面
//                singleMap.forEach((homeUrl,data) ->{
//                    try {
//                        Map<String, Price> priceMap = ParserHomePage.parseHomePage(homeUrl, "");
//                        assert priceMap != null;
//                        priceMap.forEach((configUrl, homeData) ->{
//                            try {
//                                List<List<Object>> lists = ParserSpecificPage.parseSpecificPage(configUrl, "");
//                                assert lists != null;
//                                lists.forEach(list ->{
//                                    String s = "finally value:" + data + "," + homeData + "," + list;
//                                    System.out.println(s);
//                                    builder.append(data).append(",").append(homeData).append(",").append(list);
//                                });
//
//                            } catch (IOException e) {}
//                        });
//                    } catch (IOException e) {}
//                });
                //解析灰色链接的数据
                grayMap.forEach((homeUrl,data) ->{
                    try {
                        Map<StopSale, List<List<Object>>> map = ParserHomePage.parseGrayPage(homeUrl);
                        if(map == null){
                            String s = "finally value:(only data)" + data;
                            System.out.println(s);
                        }else{
                            map.forEach((stopSale, lists) -> {
                                if (lists == null){
                                    String s = "finally value:(data and stopSale)" + data + "," + stopSale;
                                    System.out.println(s);
                                }else{
                                    lists.forEach(list ->{
                                        String s = "finally value:(all)" + data + "," + stopSale + "," + list;
                                        System.out.println(s);
                                    });
                                }

                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {}
        });
        return builder.toString();
    }


    public static void main(String[] args) {
//        getAndWriteLinks("");
        try {
//            Document document = getDocument("http://www.autohome.com.cn/grade/carhtml/C.html");
//            System.out.println(document);
//            Object o = fetchSinglePageLink("http://www.autohome.com.cn/grade/carhtml/A.html");
//            System.out.println(o);
            Object o = get();
            System.out.println(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
