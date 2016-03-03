package com.cheche.fetcher;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

import static com.cheche.common.Commons.*;
/**
 * Created by user on 2016/3/3.
 */
public class FetcherSLFilter {


    public static void fetchRawData(String url){
        Map<String,String> brandMap = Maps.newLinkedHashMap();
        Map<String,String> typeMap = Maps.newLinkedHashMap();
        Document document = getDocument(url, "UTF-8");
        Elements selectElems = document.select("select");
        selectElems.get(0).select("option").forEach(option -> brandMap.put(option.attr("value"),option.text()));
        selectElems.get(1).select("option").forEach(option -> typeMap.put(option.attr("value"),option.text()));
        parseAjax(brandMap,typeMap);
    }

    public static void parseAjax(Map<String,String> brandMap, Map<String,String> typeMap){
        List<String> ajaxUrls = Lists.newArrayList();
        brandMap.forEach((brandId,text) ->{
            typeMap.forEach((typeId,typeValue) ->{
                String ajaxUrl = "http://www.slfilter.com/cn/GetProductList.aspx?act=brand&cate_id=1&brand=" + brandId + "&type_id=" + typeId;
                ajaxUrls.add(ajaxUrl);
            });
        });

    }

    public static void parseSpecPage(List<String> ajaxUrls){
        ajaxUrls.forEach(ajaxUrl ->{
            Document document = getDocument(ajaxUrl, "utf-8");
            document.select("");
        });
    }


    public static void main(String[] args) {
        Document document = getDocument("http://www.slfilter.com/cn/GetProductList.aspx?act=brand&cate_id=1&brand=91&type_id=1", "UTF-8");
        Elements trElems = document.select("tr");
        for (int i = 2; i < trElems.size(); i++) {
            Elements tdELems = trElems.get(i).select("td");
            for (Element tdELem : tdELems) {
                System.out.println(tdELem.text());
            }
        }
    }
}
