package com.cheche.parser;

import static com.cheche.common.Commons.*;

import com.cheche.model.Price;
import com.cheche.model.StopSale;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/2/18.
 */
public class ParserHomePage {

    public static final Pattern pattern = Pattern.compile("(\\d+)");
    public static final Pattern patternContent = Pattern.compile("\\((.*)\\)");

    public static final Logger logger = LoggerFactory.getLogger(ParserHomePage.class);
    /**
     * 用来解析首页  获取车系首页的数据
     * 车系的数据需要
     * 新车指导价：6.27-7.47万 (4款车型)
     *  二手车价格：1.00-7.20万 (48个车源)
     *  用户评分
     * 同时提取出参数配置的链接
     * 还需要停售的数据
     * http://www.autohome.com.cn/2097/
     * @param url
     * @throws IOException
     */
    public static Optional<Map<String, Price>> parseHomePage(String url,String path) throws IOException {
        Document document = getDocument(url);
        Map<String,Price> homeData = Maps.newLinkedHashMap();
        //处理车系首页需要的数据  转换为price的实体类
        List<String> oldList = getJsonp(url);
        if(oldList == null) return null;
        Price price = homePageData(document,oldList);
        Elements liElems = document.select(".nav-item");
        if(liElems.isEmpty()) return null;
        Elements aElem = liElems.get(1).select("a");
        if(!aElem.isEmpty()){
            String href = aElem.attr("href");
            homeData.put(href,price);
            if(logger.isInfoEnabled()){
                logger.info("href is:{}" + href);
            }
        }
        Optional<Map<String, Price>> optHomeData = Optional.of(homeData);
        return optHomeData;
    }

    public static Map<StopSale,List<List<Object>>> parseGrayPage(String url) throws IOException {
        Map<StopSale,List<List<Object>>> map = Maps.newLinkedHashMap();
        String link = stopSaleLink(url, "");
        if(link == null) return null;
        List<StopSale> stopSales = ParserStopSalePage.parseStopSaleData(link, "");
        if(stopSales == null) return null;
        for (StopSale stopSale : stopSales) {
            String configLink = stopSale.getLink();
            List<List<Object>> lists = ParserSpecificPage.parseSpecificPage(configLink, "");
            if (lists == null){
                map.put(stopSale,null);
                continue;
            }
            map.put(stopSale,lists);
        }
    return map;
    }

    /**
     * 处理汽车首页需要的数据 将其组装成price model
     * @param document
     * @param oldList  这是处理回调函数返回的数据
     * @return
     */
    private static Price homePageData(Document document,List<String> oldList){
        Elements dtElems = document.select(".autoseries-info > dl > dt");
        Price price = new Price();
        String newPrice = dtElems.get(0).select("a").get(0).text();
        String carType = null;
        if(document.select(".koubei-score > a").size() > 1){
            carType = dtElems.get(0).select("a").get(1).text();
        }else{
            carType = "暂无";
        }
        String oldPrice = oldList.get(0) + "-" + oldList.get(1);
        String carSource = oldList.get(2);
        String SId = oldList.get(3);
        String engine = document.select(".autoseries-info > dl > dd").get(1).text();
        String specData = document.select(".autoseries-info > dl > dd").get(2).text();
        String score = null;
        if(document.select(".koubei-score > a").size() > 1){
            score = document.select(".koubei-score > a").get(1).text();
        }else{
            score = "暂无口碑";
        }
        String carName = document.select(".subnav-title-name > a").get(0).text();
        String bigImg = document.select(".autoseries-pic-img1 > a > img").attr("src");
        String factImg = document.select(".autoseries-pic-img2").get(0).select("a > img").attr("src");
        String videoImg = document.select(".autoseries-pic-img2").get(1).select("a > img").attr("src");
        price.setCarName(carName);
        price.setCarSource(carSource);
        price.setCarType(carType);
        price.setNewPrice(newPrice);
        price.setOldPrice(oldPrice);
        price.setScore(score);
        price.setBigImg(bigImg);
        price.setFactImg(factImg);
        price.setVideoImg(videoImg);
        price.setEngine(engine);
        price.setSpecData(specData);
        price.setSId(SId);
        return price;
    }

    /**
     * 解析车系页面  获取停售页面链接
     * @param url  车系页面链接
     * @param path   保存的文件路径
     * @throws IOException
     */
    private static String stopSaleLink(String url,String path) throws IOException {
        try {

            Document document = getDocument(url);
            String href = document.select(".other-car > .link-sale").attr("href");
            if (href.isEmpty()) return null;
            String link = "http://www.autohome.com.cn" + href;
            if (logger.isDebugEnabled()) {
                logger.debug("stop sale link is:{}", link);
            }
            return link;
        } catch (Exception e){
            writeStringtoFile(path,url + "\n",true);
            return null;
        }
    }

    private static List<String> getJsonp(String url) throws IOException {
        Matcher matcher = pattern.matcher(url);
        String sid = null;
        if(matcher.find()){
            sid = matcher.group(1);
        }
        String jsonp = "http://api.che168.com/auto/ForAutoCarPCInterval.ashx?callback=che168CallBack&_appid=cms&sid=" + sid + "&yid=0&pid=110000";
        Document document = null;
        try {
            document = getDocument(jsonp);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
                writeStringtoFile("error_url.txt",url,true);
            }else{
                throw e;
            }
        }
        if(document == null) return null;
        return parseJsonp(document);
    }

    /**
     * 处理jsonp {"returncode":0,"message":"","result":{"SId":3343,"YId":0,"minPrice":"4.90","maxPrice":"7.80","url":"http://www.che168.com/china/baojun/baojun610/4_8/?pvareaid=100383","count":8}}
     *
     * @param document
     * @return
     */
    private static List<String> parseJsonp(Document document) {
        List<String> list = Lists.newArrayListWithCapacity(4);
        String content = document.html();
        Matcher matcher = patternContent.matcher(content);
        if(matcher.find()){
            content = matcher.group(1);
        }
        content = filter(content);
        if(content.contains("不存在车源信息")){
            list.add("暂无");
            list.add("暂无");
            list.add("暂无");
            list.add("暂无");
            return list;
        }
        String SId = content.substring(content.indexOf("SId:")+4, content.indexOf(",YId"));
        String minPrice = content.substring(content.indexOf("minPrice:")+9, content.indexOf(",maxPrice"));
        String maxPrice = content.substring(content.indexOf("maxPrice:")+9, content.indexOf(",url"));
        String count = content.substring(content.indexOf("count:")+6, content.indexOf("}"));
        list.add(minPrice);
        list.add(maxPrice);
        list.add(count);
        list.add(SId);
        return list;
    }

    private static String filter(String content) {
        String cont = content.replaceAll("&", "").replaceAll("quot;","").replace("\"","");
        return cont;
    }


}
