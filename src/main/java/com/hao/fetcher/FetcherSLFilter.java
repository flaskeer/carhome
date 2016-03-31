package com.hao.fetcher;


import com.google.common.collect.Maps;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

import static com.hao.common.Commons.*;

/**
 * 处理这个网站
 * http://www.slfilter.com/cn/Product.aspx
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
        brandMap.forEach((brandId,text) ->{
            typeMap.forEach((typeId,typeValue) ->{
                String ajaxUrl = "http://www.slfilter.com/cn/GetProductList.aspx?act=brand&cate_id=1&brand=" + brandId + "&type_id=" + typeId;
                getData(ajaxUrl,"/search/spider/slfitler" + typeId + ".txt",text,typeValue);
                sleep();
            });
        });

    }

    public static void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void getData(String url,String storePath,String text,String type){
        Document document = getDocument(url, "UTF-8");
        Elements trElems = document.select("tr");
        for (int i = 2; i < trElems.size(); i++) {
            Elements tdELems = trElems.get(i).select("td");
            String value = "\"" + text + "\"" + "," + "\"" + type + "\"" + ",";
            for (Element tdELem : tdELems) {
                if(tdELem.text().isEmpty()){
                    value += "\"" + "\"" + ",";
                }else{
                    value += "\"" +tdELem.text() + "\"" + ",";
                }
            }
            try {
                writeStringtoFile(storePath,value + "\n",true);
            } catch (IOException e) {}
        }
    }


    public static void main(String[] args) {

        fetchRawData("http://www.slfilter.com/cn/Product.aspx");
    }
}
