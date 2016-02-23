package com.cheche.parser;


import com.cheche.model.StopSale;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static com.cheche.common.Commons.*;
/**
 * Created by user on 2016/2/18.
 */
public class ParserStopSalePage {

    public static final Logger logger = LoggerFactory.getLogger(ParserStopSalePage.class);

    /**
     * 解析停售页面的数据
     * 包括获取参数配置页面的链接
     * @param url  停售页面链接
     * @param path 停售页面参数配置链接保存路径
     * @throws IOException
     */
    public static void parseStopSaleData(String url,String path) throws IOException {
        Document document = getDocument(url);
        StopSale stopSale = new StopSale();
        //这部分用来获取参数配置
        Elements configElems = document.select(".models_nav");
        for (Element configElem : configElems) {
            String href = configElem.select("a").get(1).attr("href");
            String link = "http://www.autohome.com.cn/" + href;
            writeStringtoFile(path,link + "\n",true);
        }
        //这部分用来将数据组装成一个stopSale的model对象
        Elements carElems = document.select(".car_price");
        for (int i = 0; i < carElems.size(); i++) {
            String carName = document.select(".subnav-title-name > a").text();
            String year = carElems.get(i).select("span").get(0).text();
            String advicePrice = carElems.get(i).select("span > strong").get(0).text();
            String usedPrice = carElems.get(i).select("span > strong").get(1).text();
            String link =configElems.get(i).select("a").get(1).attr("href");
            stopSale.setYear(year);
            stopSale.setAdvicePrice(advicePrice);
            stopSale.setCarName(carName);
            stopSale.setLink(link);
            stopSale.setUsedPrice(usedPrice);
        }
    }

    public static void main(String[] args) {
        try {
            List<String> stopSaleLinks = readLink("D:/tmp/stopsale.txt");
            stopSaleLinks.forEach(stopSaleLink -> {
                try {
                    parseStopSaleData(stopSaleLink,"D:/tmp/stopsaleconfig.txt");
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                }
            });
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }
}
