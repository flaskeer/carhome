package com.hao.fetcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
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
                    .header("X-Request-Verification-Token","sgYmMX3r9IMjVhSZDSQHRf83eCF-b_yMpaEnU2X4qY3wW9KvXiB6HgHZqz1H521Cdcn799zI0RNEBcuY4wDJdj-Nnqs1:B4D_AwQS0D4oz9LiVxVS9bwoWBf7OmV1gP21tia-xa-sYibWcGYe74dbjCEPU8pqQMv_MyBLElopK9Ki_r4aHTYgF-M1")
                    .header("Cookie","ASP.NET_SessionId=vvvcxmiouivdrbomgbadmy0v; __utma=224664965.981537798.1453268473.1453337431.1453341585.3; __utmc=224664965; __utmz=224664965.1453268473.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __unam=925a2e4-1525d8d2c8c-3954b846-11; __RequestVerificationToken=PHsyj-V1vgP0hiATgy12DbAdEh6nrq4VzbTLWmNA8GSKy6naGgzMuCy3Nyj4C_Q1ZKUG5E7uTTa_OGur5nUFQ_Ipq9A1; cb-enabled=accepted; EPi:NumberOfVisits=14,2016-03-11T05:10:29,2016-03-11T05:57:25,2016-03-11T07:12:38,2016-03-11T09:07:32,2016-03-15T02:17:26,2016-03-16T01:46:11,2016-03-18T01:38:30,2016-03-18T02:35:35,2016-04-21T08:03:33,2016-04-22T00:59:29; _ga=GA1.2.981537798.1453268473; _gat=1; epslanguage=zh-CN")
                    .header("X-Requested-With","XMLHttpRequest")
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
        if (Strings.isNullOrEmpty(json)) {
            parseHomePage(url);
        }
        JSONObject jsonObject = JSON.parseObject(json);
        if (jsonObject == null) {
            parseHomePage(url);
        }
        JSONArray manufacturers = jsonObject.getJSONArray("manufacturers");
        if (manufacturers == null) {
            parseHomePage(url);
        }
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
            if (modelMap == null) continue;
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
//                            writeStringtoFile(path, finallyUrl + "\n", true);
//                            System.out.println("finallyUrl:" + finallyUrl);
                                Map<String, String> productResultMap = getFinallyData("productResults", finallyUrl);
                                if (productResultMap == null) continue;
                                for (Map.Entry<String, String> productEntry : productResultMap.entrySet()) {
                                    String value = "\"" + entry.getKey() + "\"" + ","
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
        for (Map.Entry<String,String> entry : map.entrySet()) {
            String modelIdUrl = "https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P&manufacturerId=" + entry.getValue();
//            System.out.println("modelIdUrl:" + modelIdUrl);
            Map<String, String> modelMap = getData("models", modelIdUrl);
            if (modelMap == null) continue;
            for (Map.Entry<String, String> modelEntry : modelMap.entrySet()) {
                String vehicleIdUrl = modelIdUrl + "&modelId=" + modelEntry.getValue();
//                System.out.println("vehicleIdUrl:" + vehicleIdUrl);
                Map<String, String> vehiclesMap = getData("vehicles", vehicleIdUrl);
                if (vehiclesMap == null) continue;
                for (Map.Entry<String, String> veicleEntry : vehiclesMap.entrySet()) {
                    for (Map.Entry<String, String> variantEntry : vehiclesMap.entrySet()) {
                        Map<String, String> productMap = getData("productGroups", vehicleIdUrl);
                        if (productMap == null) continue;
                        for (Map.Entry<String, String> producResultEntry : productMap.entrySet()) {
                            String finallyUrl = vehicleIdUrl + "&productGroupId=" + producResultEntry.getValue();
//                            writeStringtoFile(path, finallyUrl + "\n", true);
//                            System.out.println("finallyUrl:" + finallyUrl);
                            Map<String, String> productResultMap = getFinallyData("productResults", finallyUrl);
                            if (productResultMap == null) continue;
                            for (Map.Entry<String, String> productEntry : productResultMap.entrySet()) {
                                String value = "\"" + entry.getKey() + "\"" + ","
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
        System.out.println("please input the file path you want to store:(suffix must be .txt)");
        Scanner scanner = new Scanner(System.in);
        String filePath = null;
        while (scanner.hasNext()) {
            filePath = scanner.next();
            System.out.println("==========now start downloading=====================");
            break;
        }
        Map<String, String> map = parseHomePage("https://www.trwaftermarket.com/api/CatalogueData?market=cn&vehicleType=P");
        //{DS (长安标致雪铁龙)=3999, GMC (进口)=39, SMART (进口)=1138, 阿巴斯 (进口)=3854, 阿尔法.罗密欧 (进口)=2, 阿斯顿·马丁 (进口)=881, 阿维亚 (进口)=132, 奥迪 (进口)=5, 奥迪 (一汽奥迪)=2858, 保时捷 (进口)=92, 宝骏 (上汽通用五菱)=3764, 宝马 (华晨宝马)=3124, 宝马 (进口)=16, 北京汽车=3773, 北汽制造=3071, 奔驰 (北京奔驰)=3645, 奔腾 (一汽)=3762, 本田 (东风本田)=3126, 本田 (广汽本田)=3129, 本田汽车 (进口)=45, 比亚迪=3122, 标致 (东风标致雪铁龙)=3531, 标致 (进口)=88, 别克 (进口)=816, 别克 (上海通用)=3136, 宾利 (进口)=815, 长安=2852, 长城=2903, 大发 (一汽大发)=3274, 大宇 (进口)=185, 大众 (进口)=121, 大众 (上海大众)=3035, 大众 (一汽大众)=2859, 戴姆勒 (福建戴姆勒)=3522, 道奇 (进口)=29, 帝豪 (吉利)=3332, 东风风神=2862, 东风风行=2863, 东南 (东南汽车)=2855, 法拉利 (进口)=700, 菲亚特 (广汽菲亚特)=3761, 菲亚特 (进口)=35, 菲亚特 (南京菲亚特)=3131, 丰田 (广汽丰田)=3130, 丰田 (一汽丰田)=3137, 丰田汽车 (进口)=111, 福特 (长安福特马自达)=3125, 福特 (江铃)=3656, 福特 (进口)=36, 福特 ASIA / OZEANIA (进口)=2864, 福特 美国 (进口)=776, 观致=3839, 广州汽车=3196, 哈飞=2866, 哈弗=3968, 海马汽车=3036, 悍马 (进口)=1506, 红旗 (一汽)=2871, 华利 (一汽)=2872, 华普=2905, 华沙 (进口)=775, 华泰=3562, 霍顿（进口）=801, 吉利=2590, 吉普 (北京奔驰)=3142, 吉普 (北京吉普)=2851, 吉普 (进口)=882, 佳宝 (一汽)=2909, 江淮=2873, 捷豹 (进口)=56, 金杯 (华晨金杯)=2888, 开瑞 (奇瑞)=3286, 凯迪拉克 (进口)=819, 凯迪拉克 (上海通用)=3528, 克莱斯勒 (北京奔驰)=3646, 克莱斯勒 (进口)=20, 蓝旗亚 (进口)=64, 兰博基尼 (进口)=701, 劳斯莱斯 (进口)=705, 雷克萨斯 (进口)=842, 雷诺 (进口)=93, 力帆=3086, 莲花 (进口)=802, 莲花汽车=3661, 猎豹 (长丰猎豹)=2889, 林肯 (进口)=1200, 铃木 (昌河铃木)=3199, 铃木 (长安铃木)=3143, 铃木 (进口)=109, 路虎 (进口)=1820, 陆风 (江铃)=2589, 玛莎拉蒂汽车 (进口)=771, 马自达 (长安福特马自达)=3532, 马自达 (海南马自达)=2868, 马自达 (进口)=72, 马自达 (一汽马自达)=3138, 梅赛德斯奔驰 (进口)=74, 美佳 (进口)=845, 迷你 (进口)=1523, 名爵 (进口)=75, 名爵 (南京名爵)=3133, 名爵 (上汽)=3838, 摩根 (进口)=803, 讴歌 (进口)=1505, 欧宝 (进口)=84, 欧洲之星 (青年莲花)=3267, 纳智捷 (东风裕隆)=3742, 奇瑞=2887, 起亚 (东风悦达起亚)=3127, 起亚 (进口)=184, 启辰=3799, 庆铃=3139, 全球鹰 (吉利)=3697, 日本大发 (进口)=25, 日产 (东风日产)=3141, 日产 (进口)=80, 日产 (郑州日产)=2910, 荣威 (上汽荣威)=3202, 瑞麒 (奇瑞)=3285, 萨博 (进口)=99, 三菱 (北京奔驰)=3648, 三菱 (北京吉普)=3650, 三菱 (东南)=3658, 三菱 (广汽三菱)=3837, 三菱 (进口)=77, 上海汇众=3155, 上汽大通=3300, 双龙 (进口)=175, 斯巴鲁 (进口)=107, 斯柯达 (进口)=106, 斯柯达 (上海大众-斯柯达)=3530, 威兹曼 (进口)=1558, 威麟 (奇瑞)=3284, 沃尔沃 (长安福特马自达)=3533, 沃尔沃 (进口)=120, 沃尔沃亚太=4167, 五菱 (上汽通用五菱)=3140, 五十铃 (进口)=54, 西亚特 (进口)=104, 夏利 (天津一汽)=2890, 现代 (北京现代)=3123, 现代 (华泰)=3128, 现代 (进口)=183, 雪佛兰 (进口)=138, 雪佛兰 (上海通用)=3527, 雪佛兰 (上汽通用-雪佛兰)=3651, 雪铁龙 (东风标致雪铁龙)=2891, 雪铁龙 (进口)=21, 一汽=3610, 英菲尼迪 (东风)=4211, 英菲尼迪 (进口)=1526, 英伦 (吉利)=3704, 中华 (华晨中华)=2736, 众泰=3160}

//        Map<String,String> day01Map = Maps.newLinkedHashMap();
//        day01Map.put("DS (长安标致雪铁龙)","3999");
//        day01Map.put("GMC (进口)","39");
//        day01Map.put("SMART (进口)=1138","1138");
//        day01Map.put("阿巴斯 (进口)","3854");
//        day01Map.put("阿尔法.罗密欧 (进口)","2");
//        day01Map.put(" 阿斯顿·马丁 (进口)","881");
//        day01Map.put("阿维亚 (进口)","132");
//        day01Map.put("奥迪 (进口)=5","5");
//        parseModels(map,filePath);
        parseChinaModels(map,filePath);
    }

}
