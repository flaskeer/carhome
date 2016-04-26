package com.hao.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hao.util.HttpClientUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static com.hao.common.Commons.*;
import java.util.Map;
import java.util.regex.Pattern;

import static com.hao.common.Commons.*;

/**
 * http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml
 * Created by user on 2016/4/11.
 */
public class FetcherContitech {

//    private static Pattern pattern = Pattern.compile("")

    public static Map<String,String> brands() {
        String url = "http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml";
        Document content = getDocument(url, "UTF-8");
        Elements optElems = content.select("#Select1_Markenauswahl").select("option");
        Map<String,String> brands = Maps.newLinkedHashMap();
        optElems.stream().filter(optElem -> !optElem.attr("value").equals("0")).forEach(optElem -> brands.put(optElem.attr("value"),optElem.text()));
        System.out.println(brands);
        return brands;
    }


    public static Map<String,String> models(String herstellerID) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheModelleAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("HerstellerID",herstellerID);
        String content = content(url,payloadMap);
        content = content.replace("\\","");

        System.out.println(content);
        Map<String,String> map = Maps.newLinkedHashMap();
//        JSONObject jsonObject = JSON.parseObject(content,);
//        System.out.println(jsonObject);
//        JSONArray jsonArray = jsonObject.getJSONArray("d");
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONArray data = jsonArray.getJSONArray(i);
//            map.put(data.getString(0),data.getString(1));
//        }
//        System.out.println(map);
        return map;
    }

    public static void types(String modelId) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheTypenAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("ModellID",modelId);
        String content = content(url,payloadMap);
        content = content.replace("\\\\", "");
        System.out.println(content);
    }

    public static void art(String typId) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheArtikelAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("TypID",typId);
        String content = content(url, payloadMap);
        content = content.replace("\\","");
        parseContent(content);
    }



    public static String content(String url,Map<String,String> payloadMap) {
        String content = HttpClientUtil.sendJsonHttpPost(url, payloadMap);
        if (Strings.isNullOrEmpty(content)) {
            content(url, payloadMap);
        }
        return content;
    }

    public static void parseContent(String json) {
        json = json.replace("\\","");
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("d");

    }

    public static void spec(String url) {
        Document document = getDocument(url, "UTF-8");
        Elements trElems = document.select("#Technischedaten > tbody > tr");
        String imgSrc = document.select("#StuecklistenHauptbild > img").attr("src");
        String img = "http://aam-china.contitech.de/pages/pic/" + imgSrc;
        System.out.println(img);
        trElems.forEach(trElem -> System.out.println(trElem));
    }

    public static void execute() {

    }

    public static void main(String[] args) throws Exception {
//        brands();
        models("99");
//        types("");
//        art("5146");
//        spec("http://aam-china.contitech.de/pages/pic/produkte.cshtml?lg=cn&applg=cn&ArtikelID=AVX10X800");
    }

}
