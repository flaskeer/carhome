package com.cheche.fetcher;

import com.cheche.model.Price;
import com.cheche.model.StopSale;
import com.cheche.parser.ParserHomePage;
import com.cheche.parser.ParserSpecificPage;
import com.cheche.util.HttpClientUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.stream.Collectors;
import static com.cheche.common.Commons.writeStringtoFile;
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
        urls.remove("http://www.autohome.com.cn/grade/carhtml/E.html");
        urls.remove("http://www.autohome.com.cn/grade/carhtml/I.html");
        urls.remove("http://www.autohome.com.cn/grade/carhtml/U.html");
        urls.remove("http://www.autohome.com.cn/grade/carhtml/V.html");
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
                        grayPageMap.put(aElems.attr("href"),"\"" + firstBrand + "\"" + "," + "\"" + img + "\"" +  "," + "\"" + secondBrand + "\"" + "," + "\"" +thirdBrand + "\"" + ",");
                    }else {
                        String thirdBrand = aElems.text();
                        singlePageMap.put(aElems.attr("href"),"\"" + firstBrand + "\"" + "," + "\"" + img + "\"" +  "," + "\"" + secondBrand + "\"" + "," + "\"" +thirdBrand + "\"" + ",");
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

    /**
     * 解析程序的入口   只要调用这个函数就可以获取全部数据
     * @param salePath  写入在售车型数据
     * @param stopSalePath 写入停售车型数据
     * @return
     * @throws IOException
     */
    public static Object get(String salePath,String stopSalePath) throws IOException{
        List<String> pages = pages();
        StringBuilder builder = new StringBuilder();
        pages.forEach(pageUrl ->{
            try {
                Pair<Map<String, String>, Map<String, String>> mapPair = fetchSinglePageLink(pageUrl);
                Map<String, String> singleMap = mapPair.getLeft();
                Map<String, String> grayMap = mapPair.getRight();
                //处理带有配置参数的页面
                singleMap.forEach((homeUrl,data) ->{
                    try {
                        Map<StopSale, List<List<Object>>> stopSaleListMap = ParserHomePage.parseGrayPage(homeUrl);
                        parseMap(stopSaleListMap,data,stopSalePath);
                        Map<String, Price> priceMap = ParserHomePage.parseHomePage(homeUrl, "");
                        assert priceMap != null;
                        priceMap.forEach((configUrl, homeData) ->{
                            try {
                                List<List<Object>> lists = ParserSpecificPage.parseSpecificPage(configUrl, "");
                                assert lists != null;
                                lists.forEach(list ->{
                                    list = list.stream().map(obj -> "\"" + obj + "\"" + ",").collect(Collectors.toList());
                                    String lst = "";
                                    for (Object o : list) {
                                        lst += o;
                                    }
                                    String s = "finally value:" + data + "," + homeData + "," + lst;
                                    System.out.println(s);
                                    String write = data + "," + homeData + "," + lst + "\n";
                                    try {
                                        writeStringtoFile(salePath,write,true);
                                    } catch (IOException e) {}
                                });

                            } catch (IOException e) {}
                        });
                    } catch (IOException e) {}
                });
                //解析灰色链接的数据
                grayMap.forEach((homeUrl,data) ->{
                    try {
                        Map<StopSale, List<List<Object>>> map = ParserHomePage.parseGrayPage(homeUrl);
                        parseMap(map,data,stopSalePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {}
        });
        return builder.toString();
    }

    private static void parseMap(Map<StopSale, List<List<Object>>> map,String data,String stopSalePath) throws IOException {
        if(map == null){
            String s = "finally value:(only data)" + data;
            System.out.println(s);
            String write = data + "\n";
            writeStringtoFile(stopSalePath,write,true);
        }else{
            map.forEach((stopSale, lists) -> {
                if (lists == null){
                    String s = "finally value:(data and stopSale)" + data + "," + stopSale;
                    System.out.println(s);
                    String write = data + "," + stopSale + "\n";
                    try {
                        writeStringtoFile(stopSalePath,write,true);
                    } catch (IOException e) {}

                }else{
                    lists.forEach(list ->{
                        list = list.stream().map(obj -> "\"" + obj + "\"" + ",").collect(Collectors.toList());
                        String lst = "";
                        for (Object o : list) {
                            lst += o;
                        }
                        String s = "finally value:(all)" + data + "," + stopSale + "," + lst;
                        System.out.println(s);
                        String write = data + "," + stopSale + "," + lst + "\n";
                        try {
                            writeStringtoFile(stopSalePath,write,true);
                        } catch (IOException e) {}
                    });
                }
            });
        }
    }


    public static void main(String[] args) {
//        getAndWriteLinks("");
        try {
//            Document document = getDocument("http://www.autohome.com.cn/grade/carhtml/C.html");
//            System.out.println(document);
//            Object o = fetchSinglePageLink("http://www.autohome.com.cn/grade/carhtml/A.html");
//            System.out.println(o);
           FetcherLink.get("D:/tmp/autohome_sale_data.txt","D:/tmp/autohome__stopsale_data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
