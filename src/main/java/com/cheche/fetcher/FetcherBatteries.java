package com.cheche.fetcher;

import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cheche.common.Commons.*;
/**
 *
 * 爬取http://www.varta-automotive.cn/zh-cn/search-batteries的蓄电池数据
 * @author donghao
 * Created by user on 2016/2/24.
 */
public class FetcherBatteries {

    /**
     *
     * @param url  网页的url
     * @param charset  网页编码
     */
    public static List<String> parseRawPage(String url, String charset){
        Preconditions.checkNotNull(url,"url can not be null");
        ArrayList<String> brandList = Lists.newArrayList();
        Document document = getDocument(url, charset);
        Elements optElems = document.select("select > option");
        for (Element optElem : optElems) {
            String text = optElem.val();
            if(!text.contains("Select One")){
                brandList.add(text);
            }
        }
        return brandList;
    }

    /**  获得数据格式{三菱=三菱, 一汽海马=海马, 通用别克=别克 ...}
     * 解析获取生产厂商的数据
     * @param brandList
     * @return
     */
    public static Map<String, String> getFirmLink(List<String> brandList){
        Map<String,String> map = Maps.newLinkedHashMap();
        for (String brand : brandList) {
            String url = "http://www.varta-automotive.cn/index.php/tools/blocks/find_a_battery/lookup.php?make=" + brand + "&placeHolder=%E8%AF%B7%E9%80%89%E6%8B%A9%E6%82%A8%E7%9A%84%E6%B1%BD%E8%BD%A6%E7%94%9F%E4%BA%A7%E5%8E%82%E5%95%86&";
            Document document = getDocument(url, "UTF-8");
            Elements optElems = document.select("option");
            for (Element optElem : optElems) {
                String firm = optElem.val();
                if(!firm.contains("Select One")){
                    map.put(firm,brand);
                }
            }
        }
        return map;
    }

    /**
     * 传入的map 格式 {firm=brand,''''''''}
     * 将链接写入Linkpath  将数据写入dataPath 是为了防止请求过于频繁被封
     * @param map
     * @return
     */
    public static void getTypeLink(Map<String,String> map,String linkPath,String dataPath){
        StringBuilder builder = new StringBuilder();
        map.forEach( (firm,brand) ->{
            String url = "http://www.varta-automotive.cn/index.php/tools/blocks/find_a_battery/lookup.php?make=" + brand +"&oem=" + firm +"&placeHolder=%E8%AF%B7%E9%80%89%E6%8B%A9%E6%82%A8%E7%9A%84%E6%B1%BD%E8%BD%A6%E5%9E%8B%E5%8F%B7&";
            Document document = getDocument(url, "UTF-8");
            Elements optElems = document.select("option");
            for (Element optElem : optElems) {
                String type = optElem.val();
                if(!type.contains("Select One")){
                    builder.append("\"").append(brand).append("\"").append(",").append("\"").append(firm).append("\"").append(",").append("\"").append(type).append("\"").append("\n");
                    String searchLink = "http://www.varta-automotive.cn/zh-cn/search-batteries?make=" + brand + "&oem=" + firm +"&model=" + type;
                    try {
                        writeStringtoFile(linkPath,searchLink + "\n",true);
                    } catch (IOException e) {e.printStackTrace();}
                }
            }
        });
        try {
            writeStringtoFile(dataPath,builder.toString(),true);
        } catch (IOException e) {e.printStackTrace();
        }
    }

    public static List<String> parseSearchLink(String link) {
        Document document = getDocument(link, "UTF-8");
        Elements resultElems = document.select(".searchResult");
        List<String> values = Lists.newArrayListWithCapacity(resultElems.size());
        for (Element resultElem : resultElems) {
            String name = resultElem.select(".details > h3 > a").text();
            String detailName = resultElem.select(".details > h5").text();
            String value = "," + "\"" + name + "\"" + "," + "\"" + detailName + "\"";
            values.add(value);
        }
        return values;
    }

    /**
     *  解析的数据格式
     *  雷诺,雷诺,科雷傲
        雷诺,雷诺,梅佳娜
        雷诺,雷诺,梅佳娜CC
     *@param linkPath  传入链接的文件路径
     * @param dataPath  传入数据的文件路径
     * @throws IOException
     */
    public static void combineData(String linkPath,String dataPath,String writePath) throws IOException {
        List<String> linkList = readLink(linkPath);
        List<String> dataList = readLink(dataPath);
        for (int i = 0; i < linkList.size(); i++) {
            String link = linkList.get(i);
            link = link.replaceAll(" ","%20");
            List<String> nameList = parseSearchLink(link);
            for (String name : nameList) {
                String val = dataList.get(i) + name;
                writeStringtoFile(writePath,val + "\n",true);
            }
        }


    }



    public static void main(String[] args) {
////        List<String> strings = parseRawPage("http://www.varta-automotive.cn/zh-cn/search-batteries", "UTF-8");
////        Map<String, String> firmLink = getFirmLink(strings);
////        getTypeLink(firmLink,"D:/tmp/batteries_link.txt","D:/tmp/batteries_data.txt");
//            List<String> s = parseSearchLink("http://www.varta-automotive.cn/zh-cn/search-batteries?make=中华&oem=华晨中华&model=骏捷");
//            System.out.println(s);
        try {
            combineData("D:/tmp/batteries_link.txt","D:/tmp/batteries_data.txt","all_batteries_data.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
