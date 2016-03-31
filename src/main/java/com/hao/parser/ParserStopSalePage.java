package com.hao.parser;


import com.hao.model.StopSale;
import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hao.common.Commons.*;
/**
 * Created by user on 2016/2/18.
 */
public class ParserStopSalePage {

    public static final Logger logger = LoggerFactory.getLogger(ParserStopSalePage.class);
    public static final Pattern pattern = Pattern.compile("(\\d+)");

    /**
     * 解析停售页面的数据
     * 包括获取参数配置页面的链接
     * @param url  停售页面链接
     * @param path 停售页面参数配置链接保存路径
     * @throws IOException
     */
    public static List<StopSale> parseStopSaleData(String url,String path) throws IOException {
        try {
            Document document = getDocument(url);
            List<StopSale> lists = Lists.newArrayList();
            //这部分用来获取参数配置
            Elements configElems = document.select(".models_nav");
            for (Element configElem : configElems) {
                String href = configElem.select("a").get(1).attr("href");
                String link = "http://www.autohome.com.cn/" + href;
            }
            //这部分用来将数据组装成一个stopSale的model对象
            Elements carElems = document.select(".car_price");
            for (int i = 0; i < carElems.size(); i++) {
                StopSale stopSale = new StopSale();
                String id = null;
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    id = matcher.group(1);
                }
                String carName = document.select(".subnav-title-name > a").text();
                String year = carElems.get(i).select("span").get(0).text();
                String advicePrice = carElems.get(i).select("span > strong").get(0).text();
                String usedPrice = carElems.get(i).select("span > strong").get(1).text();
                String link = configElems.get(i).select("a").get(1).attr("href");
                link = "http://www.autohome.com.cn/" + link;
                stopSale.setYear(year);
                stopSale.setAdvicePrice(advicePrice);
                stopSale.setCarName(carName);
                stopSale.setLink(link);
                stopSale.setUsedPrice(usedPrice);
                stopSale.setId(id);
                lists.add(stopSale);
            }
            return lists;
        } catch (Exception e){
            if(e instanceof IllegalArgumentException){
                writeStringtoFile("error_url.txt",url + "\n",true);
                return null;
            }else{
                throw e;
            }
        }
    }


}
