package com.cheche.parser;

import static com.cheche.common.Commons.*;
import com.cheche.model.Price;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;

/**
 * Created by user on 2016/2/18.
 */
public class ParserHomePage {

    public static final Logger logger = LoggerFactory.getLogger(ParserHomePage.class);
    /**
     * 用来解析首页  获取车系首页的数据
     * 车系的数据需要
     * 新车指导价：6.27-7.47万 (4款车型)
     *  二手车价格：1.00-7.20万 (48个车源)
     *  用户评分
     * 同时提取出参数配置的链接
     * 还需要停售的数据
     * @param url
     * @throws IOException
     */
    public static void parseHomePage(String url,String path) throws IOException {
        Document document = getDocument(url);
        //处理车系首页需要的数据  转换为price的实体类
        Price price = homePageData(document);
        Elements liElems = document.select(".nav-item");
        if(liElems.isEmpty()) return;
        Elements aElem = liElems.get(1).select("a");
        if(!aElem.isEmpty()){
            String href = aElem.attr("href");
            if(logger.isInfoEnabled()){
                logger.info("href is:{}" + href);
            }
            writeStringtoFile(path,href + "\n",true);
        }

    }

    /**
     * 处理汽车首页需要的数据 将其组装成price model
     * @param document
     * @return
     */
    private static Price homePageData(Document document){
        Elements dtElems = document.select(".autoseries-info > dl > dt");
        Price price = new Price();
        String newPrice = dtElems.get(0).select("a").get(0).text();
        String carType = dtElems.get(0).select("a").get(1).text();
        String oldPrice = dtElems.get(1).select("a").get(0).text();
        String carSource = dtElems.get(1).select("a").get(1).text();
        String score = document.select(".koubei-score > a").get(1).text();
        String carName = document.select(".subnav-title-name > a").get(0).text();
        price.setCarName(carName);
        price.setCarSource(carSource);
        price.setCarType(carType);
        price.setNewPrice(newPrice);
        price.setOldPrice(oldPrice);
        price.setScore(score);
        return price;
    }

    /**
     * 解析车系页面  获取停售页面链接
     * @param url  车系页面链接
     * @param path   保存的文件路径
     * @throws IOException
     */
    private static void stopSaleLink(String url,String path) throws IOException {
        Document document = getDocument(url);
        String href = document.select(".other-car > .link-sale").attr("href");
        if(href.isEmpty()) return;
        String link = "http://www.autohome.com.cn" + href;
        if(logger.isDebugEnabled()){
            logger.debug("stop sale link is:{}",link);
        }
        writeStringtoFile(path,link + "\n",true);
    }



    public static void main(String[] args) {
        try {
            List<String> links = readLink("D:/tmp/carhomelink1.txt");
            links.forEach(link ->{
                try {
//                    parseHomePage(link,"D:/tmp/config.txt");
                    stopSaleLink(link,"D:/tmp/stopsale.txt");
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                }
            });
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

}
