package com.hao.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.hao.util.HttpClientUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static com.hao.common.Commons.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import static com.hao.common.Commons.*;

/**
 * http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml
 * Created by user on 2016/4/11.
 */
public class FetcherContitech {


    public static Map<String,String> brands() {
        String url = "http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml";
        Document content = getDocument(url, "UTF-8");
        Elements optElems = content.select("#Select1_Markenauswahl").select("option");
        Map<String,String> brands = Maps.newTreeMap(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int i1 = Integer.parseInt(o1);
                int i2 = Integer.parseInt(o2);
                if (i1 > i2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        optElems.stream().filter(optElem -> !optElem.attr("value").equals("0")).forEach(optElem -> brands.put(optElem.attr("value"),optElem.text()));
//        System.out.println(brands);

        return brands;
    }


    public static Map<String,String> models(String herstellerID) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheModelleAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("HerstellerID",herstellerID);
        String content = content(url,payloadMap);
        Map<String, String> modelMap = parseContent(content);
//        System.out.println(modelMap);
        return modelMap;
    }

    public static Map<String, String> types(String modelId) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheTypenAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("ModellID",modelId);
        String content = content(url,payloadMap);
        Map<String, String> typeMap = parseContent(content);
//        System.out.println(typeMap);
        return typeMap;
    }

    public static Map<String, String> art(String typId) {
        String url = "http://aam-china.contitech.de/pages/web-katalog/WebService.asmx/FahrzeugsucheArtikelAuslesen";
        Map<String,String> payloadMap = Maps.newLinkedHashMap();
        payloadMap.put("TypID",typId);
        String content = content(url, payloadMap);
        Map<String, String> typeMap = parseContent(content);
        Map<String,String> urlMap = Maps.newLinkedHashMap();
        typeMap.forEach((k,v) -> {
            String val = v.substring(0,v.indexOf("\""));
            try {
                val = URLEncoder.encode(val,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String specUrl = "http://aam-china.contitech.de/pages/pic/produkte.cshtml?lg=cn&applg=cn&ArtikelID=" + val;
            urlMap.put(specUrl,v);
        });
        typeMap.putAll(urlMap);
//        System.out.println(typeMap);
        return typeMap;
    }



    public static String content(String url,Map<String,String> payloadMap) {
        String content = HttpClientUtil.sendJsonHttpPost(url, payloadMap);
        if (Strings.isNullOrEmpty(content)) {
            content(url, payloadMap);
        }
        return content;
    }

    public static Map<String,String> parseContent(String json) {
        json = json.replace("\\","");
        json = json.replace(":\"",":").replace("\"}","}");
        Map<String,String> map = Maps.newLinkedHashMap();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("d");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray data = jsonArray.getJSONArray(i);
            String value = "";
            for (int k = 1; k < data.size(); k++) {
                if (k == 1) {
                    value += data.getString(k) + "\"" + ",";
                } else {
                    value += "\"" + data.getString(k) + "\"" + ",";
                }
            }
            map.put(data.getString(0),value);
        }
        return map;
    }

    public static String spec(String url) {
        Document document = getDocument(url, "UTF-8");
        Elements trElems = document.select("#Technischedaten > tbody > tr");
        String imgSrc = document.select("#StuecklistenHauptbild > img").attr("src");
        String img = "http://aam-china.contitech.de/pages/pic/" + imgSrc;
        String val = "";
        for (Element trElem : trElems) {
            Elements tdElems = trElem.select("td");
            for (Element tdElem : tdElems) {
                val += "\"" + tdElem.text() + "\"" + ",";
            }
        }
        val += "\"" + img + "\"" + ",";
        return val;
    }

    public static void execute(String filePath) throws IOException {
        Map<String, String> brandMap = brands();
        for (Map.Entry<String,String> brandEntry : brandMap.entrySet()) {
            Map<String, String> modelMap = models(brandEntry.getKey());
            for (Map.Entry<String,String> modelEntry : modelMap.entrySet()) {
                Map<String, String> typeMap = types(modelEntry.getKey());
                for (Map.Entry<String,String> typeEntry : typeMap.entrySet()) {
                    Map<String, String> artMap = art(typeEntry.getKey());
                    for (Map.Entry<String,String> artEntry : artMap.entrySet()) {
                        String data = "";
                        if (artEntry.getKey().startsWith("http:")) {
                            data = spec(artEntry.getKey());
                        }else{
                            continue;
                        }
                        String value = "\"" + brandEntry.getValue() + "\"" + "," +
                                "\"" + modelEntry.getValue()  +
                                "\""+typeEntry.getValue() +
                                "\"" + artEntry.getValue() +
                                data;
                        writeStringtoFile(filePath,value + "\n",true);
                        System.out.println(value);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
//        brands();
//        models("99");
//        types("1425");
//        art("5146");
//        spec("http://aam-china.contitech.de/pages/pic/produkte.cshtml?lg=cn&applg=cn&ArtikelID=AVX10X800");
        execute("D:/tmp/tech0427.txt");
    }

}
