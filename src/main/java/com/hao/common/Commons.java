package com.hao.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hao.util.HttpClientUtil;
import com.google.common.collect.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * 用来存放公共的操作
 * Created by user on 2016/2/18.
 */
public class Commons {

    /**
     * 获取document
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDocument(String url) throws IOException {
        String content = HttpClientUtil.sendHttpsGet(url);
        Document document = Jsoup.parse(content);
        return document;

    }

    public static Document getDocument(String url,String charset){
        String content = HttpClientUtil.sendHttpGet(url,charset);
        return Jsoup.parse(content);
    }

    /**
     * 将字符串写入文件
     * @param path  文件路径
     * @param data
     * @param append 追加
     * @throws IOException
     */
    public static void writeStringtoFile(String path,String data,boolean append) throws IOException {
        FileUtils.writeStringToFile(new File(path),data,append);
    }

    /**
     * 从文件路径中读取链接并转换成一个list
     * @param path
     * @return
     * @throws IOException
     */
    public static List<String> readLink(String path) throws IOException {
        List<String> links = Lists.newArrayList();
        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String input = null;
        while((input = reader.readLine()) != null){
            links.add(input);
        }
        return links;
    }

    /**
     * 处理特定格式的JSON字符串
     * {
     "returncode": 0,
     "message": null,
     "result": {
     "seriesid": 771,
     "yearid": 5063,
     "paramtypeitems": [
     {
     "name": "基本参数",
     "paramitems": [
     {
     "name": "车型名称",
     "valueitems": [
     {
     "specid": 16364,
     "value": "汉兰达 2013款  2.7L 两驱5座紫金版"
     },
     {
     "specid": 16365,
     "value": "汉兰达 2013款 2.7L 两驱7座紫金版"
     },
     {
     "specid": 16470,
     "value": "汉兰达 2013款 2.7L 两驱7座探索版"
     }
     ]
     },

     },
     .... 解析config  option的JSON字符串
     其中typeItems,items 在不同JSON字符串中不同 eg :paramtypeitems  paramitems
     请去对应页面查找
     * @param json
     */
    /**
     *
    public static List<List<String>> parseJson(String json,String typeItems,String items,String valueorName,String[] ids){
        List<String> lists = Lists.newArrayListWithExpectedSize(ids.length);
        List<List<String>> results = Lists.newArrayList();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray(typeItems);
        for (int k = 0; k < jsonArray.size(); k++) {

            JSONArray jsonArray1 = jsonArray.getJSONObject(k).getJSONArray(items);
            for (int i = 0; i < jsonArray1.size(); i++) {
                JSONArray jsonArray2 = jsonArray1.getJSONObject(i).getJSONArray("valueitems");

                for (int j = 0; j < jsonArray2.size(); j++) {
                    String specId = jsonArray.getJSONObject(j).getString("specid");
                    String value = jsonArray2.getJSONObject(j).getString(valueorName);
                    lists.add(value);
                }
                results.add(lists);
                lists = Lists.newArrayListWithCapacity(ids.length);
            }
        }
        return results;
    }
     **/


    public static Table<String,String,String> parseJson(String json,String typeItems,String items,String valueorName,String[] ids){
//        List<Table<String,String,String>> lists = Lists.newArrayListWithExpectedSize(ids.length);
//        List<List<Table<String,String,String>>> results = Lists.newArrayList();
//        Map<String,String> map = Maps.newHashMap();
        Table<String,String,String> table = HashBasedTable.create();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray(typeItems);
        for (int k = 0; k < jsonArray.size(); k++) {
            JSONArray jsonArray1 = jsonArray.getJSONObject(k).getJSONArray(items);
            for (int i = 0; i < jsonArray1.size(); i++) {

                String name = jsonArray1.getJSONObject(i).getString("name");
                JSONArray jsonArray2 = jsonArray1.getJSONObject(i).getJSONArray("valueitems");
                for (int j = 0; j < jsonArray2.size(); j++) {
                    String specId = jsonArray2.getJSONObject(j).getString("specid");
                    String value = jsonArray2.getJSONObject(j).getString(valueorName);
//                    map.put(specId,value);
                    table.put(name,specId,value);
//                    lists.add(table);
//                    table = HashBasedTable.create();
//                    map = Maps.newHashMap();
                }
//                results.add(lists);
//                lists = Lists.newArrayListWithCapacity(ids.length);
            }
        }
        return table;
    }


    public static List<String> parseStandardField(String json){
        List<String> names = Lists.newArrayList();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("items");
        for (int i = 0; i < jsonArray.size(); i++) {
            String name = jsonArray.getJSONObject(i).getString("name");
            names.add(name);
        }
        return names;
    }


    /**
     * 用来处理color的解析程序 因为color和 option config格式不同
     * @param json
     * @param ids
     */
    /**
    public static List<List<String>> parseJsonForColor(String json,String[] ids){
        List<String> lists = Lists.newArrayListWithExpectedSize(ids.length);
        List<List<String>> results = Lists.newArrayList();
        JSONObject jsonObject = JSON.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("specitems");
        for (int k = 0; k < jsonArray.size(); k++) {
            JSONArray jsonArray1 = jsonArray.getJSONObject(k).getJSONArray("coloritems");
            for (int i = 0; i < jsonArray1.size(); i++) {
                String value = jsonArray1.getJSONObject(i).getString("name");
                lists.add(value);

            }
            results.add(lists);
            lists = Lists.newArrayList();
        }
        return results;
    }
    **/


    /**
     * 用来处理color的解析程序 因为color和 option config格式不同
     * @param json
     * @param ids
     *
     * 暂时忽略颜色的处理
     */
//    public static List<Table<String,String,List<String>>> parseJsonForColor(String json,String[] ids){
//        List<String> lists = Lists.newArrayListWithExpectedSize(ids.length);
//        List<Map<String,List<String>>> results = Lists.newArrayList();
////        Map<String,List<String>> map = Maps.newHashMap();
//        Table<String,String,String> table = HashBasedTable.create();
//        JSONObject jsonObject = JSON.parseObject(json);
//        JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("specitems");
//        for (int k = 0; k < jsonArray.size(); k++) {
//            String specId = jsonArray.getJSONObject(k).getString("specid");
//            JSONArray jsonArray1 = jsonArray.getJSONObject(k).getJSONArray("coloritems");
//            for (int i = 0; i < jsonArray1.size(); i++) {
//                String value = jsonArray1.getJSONObject(i).getString("name");
//                lists.add(value);
//            }
//            map.put(specId,lists);
//            results.add(map);
//            map = Maps.newHashMap();
//            lists = Lists.newArrayList();
//        }
//        return results;
//    }

    public static String[] split(String list){
        String[] ids = list.split(",");
        return ids;
    }
}
