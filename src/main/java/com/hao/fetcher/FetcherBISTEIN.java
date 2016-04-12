package com.hao.fetcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hao.common.Commons.*;
/**
 *
 * http://www.bilstein.com.tw/product.php
 * Created by user on 2016/4/12.
 */
public class FetcherBISTEIN {

    private static Pattern pattern = Pattern.compile("'(\\d+)'");


    private static final Logger LOGGER = LoggerFactory.getLogger(FetcherBISTEIN.class);

    public static Map<String,Integer> getBrands() {
        String url = "http://www.bilstein.com.tw/product.php";

        Document content = getDocument(url, "UTF-8");
        if (content == null) {
            getBrands();
        }
        Elements liElems = content.select(".carBrand > ul > li");
        Map<String,Integer> brands = Maps.newLinkedHashMap();
        liElems.forEach(liElem -> brands.put(liElem.text(),Integer.parseInt(parseNumber(liElem))));
        return brands;
    }


    public static Map<String,Integer> getType(int number) {
        String url = "http://www.bilstein.com.tw/search_menu.php?car=model&b1sn=" + number;

        Document content = getDocument(url, "UTF-8");
        if (content == null) {
            getType(number);
        }
        Elements liElems = content.select("li");
        Map<String,Integer> map = Maps.newLinkedHashMap();
        liElems.forEach(liElem -> map.put(liElem.text(),Integer.parseInt(parseNumber(liElem))));
        return map;
    }

    public static Map<String,String> getYear(int number) {
        String url = "http://www.bilstein.com.tw/search_menu.php?car=year&b2sn=" + number;
        Document content = getDocument(url, "UTF-8");
        if (content == null) {
            getYear(number);
        }
        Elements liElems = content.select("li");
        Map<String,String> hrefs = Maps.newLinkedHashMap();

        liElems.forEach(liElem -> {
            String rel = liElem.select("a").attr("rel");
            rel = rel.replace(" ", "%20");
            String href = "http://www.bilstein.com.tw/" + rel;
            hrefs.put(liElem.text(),href);
        });
        return hrefs;
    }

    public static List<String> getShock(String href) {
        Document content = getDocument(href, "UTF-8");
        if (content == null) {
            getShock(href);
        }
        Elements liElems = content.select(".searchList > li");
        List<String> dataList = Lists.newArrayList();
        liElems.forEach(liElem -> {
            dataList.add(liElem.text());
        });
        return dataList;
    }



    private static String parseNumber(Element element) {
        String number = "";
        String attr = element.attr("onclick");
        Matcher matcher = pattern.matcher(attr);
        if (matcher.find()) {
           number = matcher.group(1);
        }
        return number;

    }

    public static void execute(String filePath) {
        Map<String, Integer> brandMap = getBrands();
        brandMap.forEach((brand,number) -> {
            Map<String, Integer> typeMap = getType(number);
            typeMap.forEach((type,typeNumber) -> {
                Map<String,String> hrefMap = getYear(typeNumber);
                hrefMap.forEach((year,href) -> {
                    List<String> shockData = getShock(href);
                    String shock = "";
                    for (String s : shockData) {
                        shock += "\"" + s + "\""  + ",";
                    }
                    String data = "\"" + brand + "\"" + "," +
                                  "\"" + type + "\"" + "," +
                                  "\"" + year + "\"" + "," +
                                  shock;
                    System.out.println(data);
                    try {
                        writeStringtoFile(filePath,data + "\n",true);
                    } catch (IOException e) {
                        LOGGER.error("exception:{}",e);
                    }
                });
            });
        });
    }

    public static void main(String[] args) {
        execute("D:/tmp/bistein.txt");
    }

}
