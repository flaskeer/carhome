package com.cheche.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
                    .header("X-Request-Verification-Token","E_MHw3VzcdTjCei7c9N6HuFJYLK_uuacYAE-mZyPrnbfOAZ4oC0bgzM62Yavkp47nRIcch1r-ZfbS18dI7YtPO9h-Wc1:icW9QQnPRg8xq9lU1HtCLMCafyrKnS0896ctDMhog4UF4ahtqqgECyPcSS_c-aIbXdus0kQg5jNfOQMaiH0Z-NWbOzM1")
                    .get();
        } catch (IOException e) {
            getDocument(url);
        }
        return document.text().replaceAll("&quot;","");
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
        for (Map.Entry<String,String> entry : map.entrySet()) {
            String modelIdUrl = "https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P&manufacturerId=" + entry.getValue();
            Map<String, String> modelMap = getData("models", modelIdUrl);
            for (Map.Entry<String,String> modelEntry : modelMap.entrySet()) {
                String vehicleIdUrl = modelIdUrl + "&modelId=" + modelEntry.getValue();
                System.out.println("vehurl:" + vehicleIdUrl);
                Map<String, String> vehiclesMap = getData("vehicles", vehicleIdUrl);
                for (Map.Entry<String,String> veicleEntry : vehiclesMap.entrySet()) {
                    String variantUrl = vehicleIdUrl + "&vehicleId=" + veicleEntry.getValue();
                    System.out.println("variantUrl:" + variantUrl);
                    Map<String, String> variantMap = getData("variants", variantUrl);
                    for (Map.Entry<String,String> variantEntry : variantMap.entrySet()) {

                        String finallyUrl = variantUrl + variantEntry.getValue() + "&productGroupId=854";
                        System.out.println("finally:" + finallyUrl);
                        Map<String, String> productResultMap = getFinallyData("productResults", finallyUrl);
                        if(productResultMap == null) continue;
                        for (Map.Entry<String,String> productEntry : productResultMap.entrySet()) {
                            String value = "\"" + entry.getKey() + "\"" +","
                                         + "\"" + modelEntry.getKey() + "\"" + ","
                                         + "\"" + veicleEntry.getKey() + "\"" + ","
                                         + "\"" + variantEntry.getKey()  + "\"" +  ","
                                         + "\"" + productEntry.getKey()  + "\"" + ","
                                         + "\"" + productEntry.getValue() + "\"";
                            writeStringtoFile(path,value + "\n",true);
                            System.out.println(value);
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
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray models = jsonObject.getJSONArray(type);
        if(models == null) return null;
        for (int i = 0; i < models.size(); i++) {
            String text = models.getJSONObject(i).getString("productCode");
            String value = models.getJSONObject(i).getString("attributeText");
//            String substring = value.substring(text.indexOf("安装位置: "), text.indexOf("安装位置: ") + 8);
            map.put(text,value);
        }
        return map;
    }


    private static Map<String,String> getData(String type,String url) throws IOException {
        Map<String,String> map = Maps.newLinkedHashMap();
        String json = getDocument(url);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray models = jsonObject.getJSONArray(type);
        for (int i = 0; i < models.size(); i++) {
            String text = models.getJSONObject(i).getString("text");
            String value = models.getJSONObject(i).getString("value");
            String encode = URLEncoder.encode(value, "UTF-8");
            map.put(text,encode);
        }
        return map;
    }

    public static List<String> readLink(String path) throws IOException {
        List<String> links = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String input = null;
        while((input = reader.readLine()) != null){
            links.add(input);
        }
        return links;
    }


    public static void main(String[] args) throws IOException {
        Map<String, String> map = parseHomePage("https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P");
        System.out.println(map);
        parseModels(map,"D:/tmp/traftermarket222.txt");
//        List<String> strings = readLink("D:/tmp/tmp.txt");
//        strings.forEach(string ->{
//            String s = string.substring(string.indexOf("安装位置: "), string.indexOf("安装位置: ") + 8);
//            try {
//                writeStringtoFile("D:/tmp/tetetete.txt",s + "\n",true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

}
