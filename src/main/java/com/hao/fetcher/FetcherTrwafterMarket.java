package com.hao.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hao.common.Commons;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * https://www.trwaftermarket.com/cn/catalogue/ 爬取此网站爬虫
 * Created by user on 2016/3/11.
 */
public class FetcherTrwafterMarket {

    private static String getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .header("X-Current-Language-Code","zh-CN")
                    .header("X-Request-Verification-Token","VCDTgCwiuHPQlnYSW8TDxScEPGqpXICRCguJWhzSHTQur10XKWpplky1JwaDGQuytj9MUSLnvcQk_l4d06IjClpyiZg1:PTq4jQ1nu-t1P6v_fiSAAhPVd8YwuNsKwR7UdIYxgeihiL4kjSdhOvEzdPYQMziK6r4IC-yaV6yiSgWIzz11SFys9SQ1")
                    .header("Cookie","ASP.NET_SessionId=vvvcxmiouivdrbomgbadmy0v; __utma=224664965.981537798.1453268473.1453337431.1453341585.3; __utmc=224664965; __utmz=224664965.1453268473.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __unam=925a2e4-1525d8d2c8c-3954b846-11; __RequestVerificationToken=PHsyj-V1vgP0hiATgy12DbAdEh6nrq4VzbTLWmNA8GSKy6naGgzMuCy3Nyj4C_Q1ZKUG5E7uTTa_OGur5nUFQ_Ipq9A1; cb-enabled=accepted; EPi:NumberOfVisits=14,2016-03-11T05:10:29,2016-03-11T05:57:25,2016-03-11T07:12:38,2016-03-11T09:07:32,2016-03-15T02:17:26,2016-03-16T01:46:11,2016-03-18T01:38:30,2016-03-18T02:35:35,2016-04-21T08:03:33,2016-04-22T00:59:29; _ga=GA1.2.981537798.1453268473; _gat=1; epslanguage=zh-CN")
                    .get();
        } catch (IOException e) {
            getDocument(url);
        }
        if (document == null) {
            getDocument(url);
        } else {
            return document.text().replaceAll("&quot;","");
        }
        return null;
    }

    private static Map<String,String> parseHomePage(String url) throws IOException {
        Map<String,String> map = Maps.newLinkedHashMap();
        String json = getDocument(url);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray manufacturers = jsonObject.getJSONArray("manufacturers");
        for (int i = 0; i < manufacturers.size(); i++) {
            String text = manufacturers.getJSONObject(i).getString("text");
            String value = manufacturers.getJSONObject(i).getString("value");
            map.put(text,value);
        }
        return map;
    }

    private static void parseModels(Map<String,String> map,String path) throws IOException {
            for (Map.Entry<String,String> typeEntry:map.entrySet()) {
                String modelIdUrl = "https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P&manufacturerId=" + typeEntry.getValue();
                Map<String, String> modelMap = getData("models", modelIdUrl);
                if (modelMap == null) return;
                for (Map.Entry<String, String> modelEntry : modelMap.entrySet()) {
                    String vehicleIdUrl = modelIdUrl + "&modelId=" + modelEntry.getValue();
//                System.out.println("vehurl:" + vehicleIdUrl);
                    Map<String, String> vehiclesMap = getData("vehicles", vehicleIdUrl);
                    if (vehiclesMap == null) continue;
                    for (Map.Entry<String, String> veicleEntry : vehiclesMap.entrySet()) {
                        String variantUrl = vehicleIdUrl + "&vehicleId=" + veicleEntry.getValue();
//                    System.out.println("variantUrl:" + variantUrl);
                        Map<String, String> variantMap = getData("variants", variantUrl);
                        if (variantMap == null) continue;
                        for (Map.Entry<String, String> variantEntry : variantMap.entrySet()) {
                            Map<String, String> productMap = getData("productGroups", variantUrl);
                            if (productMap == null) continue;
                            for (Map.Entry<String, String> producResultEntry : productMap.entrySet()) {
                                String finallyUrl = variantUrl + "&productGroupId=" + producResultEntry.getValue();
//                            System.out.println("finallyUrl:" + finallyUrl);
                                Map<String, String> productResultMap = getFinallyData("productResults", finallyUrl);
                                if (productResultMap == null) continue;
                                for (Map.Entry<String, String> productEntry : productResultMap.entrySet()) {
                                    String value = "\"" + typeEntry.getKey() + "\"" + ","
                                            + "\"" + modelEntry.getKey() + "\"" + ","
                                            + "\"" + veicleEntry.getKey() + "\"" + ","
                                            + "\"" + variantEntry.getKey() + "\"" + ","
                                            + "\"" + producResultEntry.getKey() + "\"" + ","
                                            + "\"" + productEntry.getKey() + "\"" + ","
                                            + "\"" + productEntry.getValue() + "\"";
                                    writeStringtoFile(path, value + "\n", true);
                                    System.out.println(value);
                                }
                            }
                        }
                    }
                }
            }

    }

    private static void parseChinaModels(Map<String,String> map,String path) throws IOException{
        for (Map.Entry<String,String> typeEntry:map.entrySet()) {
            String modelIdUrl = "https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P&manufacturerId=" + typeEntry.getValue();
            System.out.println("modelIdUrl:" + modelIdUrl);
            Map<String, String> modelMap = getData("models", modelIdUrl);
            if (modelMap == null) return;
            for (Map.Entry<String, String> modelEntry : modelMap.entrySet()) {
                String vehicleIdUrl = modelIdUrl + "&modelId=" + modelEntry.getValue();
                System.out.println("vehicleIdUrl:" + vehicleIdUrl);
                Map<String, String> vehiclesMap = getData("vehicles", vehicleIdUrl);
                if (vehiclesMap == null) continue;
                for (Map.Entry<String, String> veicleEntry : vehiclesMap.entrySet()) {
                    for (Map.Entry<String, String> variantEntry : vehiclesMap.entrySet()) {
                        Map<String, String> productMap = getData("productGroups", vehicleIdUrl);
                        if (productMap == null) continue;
                        for (Map.Entry<String, String> producResultEntry : productMap.entrySet()) {
                            String finallyUrl = vehicleIdUrl + "&productGroupId=" + producResultEntry.getValue();
                            System.out.println("finallyUrl:" + finallyUrl);
                            Map<String, String> productResultMap = getFinallyData("productResults", finallyUrl);
                            if (productResultMap == null) continue;
                            for (Map.Entry<String, String> productEntry : productResultMap.entrySet()) {
                                String value = "\"" + typeEntry.getKey() + "\"" + ","
                                        + "\"" + modelEntry.getKey() + "\"" + ","
                                        + "\"" + veicleEntry.getKey() + "\"" + ","
                                        + "\"" + variantEntry.getKey() + "\"" + ","
                                        + "\"" + producResultEntry.getKey() + "\"" + ","
                                        + "\"" + productEntry.getKey() + "\"" + ","
                                        + "\"" + productEntry.getValue() + "\"";
                                writeStringtoFile(path, value + "\n", true);
                                System.out.println(value);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void writeStringtoFile(String path,String data,boolean append) throws IOException {
        FileUtils.writeStringToFile(new File(path),data,append);
    }

    private static Map<String, String> getFinallyData(String type,String url) throws IOException {
        Map<String,String> map = Maps.newLinkedHashMap();
        String json = getDocument(url);
        if (Strings.isNullOrEmpty(json)) {
            getFinallyData(type,url);
        }
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject == null) return null;
        JSONArray models = jsonObject.getJSONArray(type);
        if(models == null) return null;
        for (int i = 0; i < models.size(); i++) {
            String text = models.getJSONObject(i).getString("productCode");
            String value = models.getJSONObject(i).getString("attributeText");
            map.put(text,value);
        }
        return map;
    }


    private static Map<String,String> getData(String type,String url) throws IOException {
        Map<String,String> map = Maps.newLinkedHashMap();
        String json = getDocument(url);
        if (Strings.isNullOrEmpty(json)) {
            getData(type,url);
        }
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject == null) return null;
        JSONArray models = jsonObject.getJSONArray(type);
        if(models == null) return null;
        for (int i = 0; i < models.size(); i++) {
            String text = models.getJSONObject(i).getString("text");
            String value = models.getJSONObject(i).getString("value");
            String encode = URLEncoder.encode(value, "UTF-8");
            map.put(text,encode);
        }
        return map;
    }


    public static void main(String[] args) throws IOException {
        System.out.println("=============welcome into spider system:====================");
        System.out.println("please input the file path you want to store:");
        Scanner scanner = new Scanner(System.in);
        String filePath = null;
        while (scanner.hasNext()) {
            filePath = scanner.next();
            System.out.println("==========now start downloading=====================");
            break;
        }
        Map<String, String> map = parseHomePage("https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P");
        parseModels(map,filePath);
        parseChinaModels(map,filePath);
    }



}
