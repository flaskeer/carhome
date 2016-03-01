package com.cheche.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Html;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.cheche.common.Commons.*;
/**
 * Created by user on 2016/2/18.
 */
public class ParserSpecificPage {

    public static final Logger logger = LoggerFactory.getLogger(ParserSpecificPage.class);

    /**
     * 取出一页详情页的数据
     * @param url
     * @return
     * @throws IOException
     */
    public static List<List<Object>> parseSpecificPage(String url,String errorPath) throws IOException {
        System.out.println("正在解析的url:" + url);
        String content = null;
        try {
            content = getDocument(url).toString();
        } catch (Exception e) {
            return null;
        }
        if (content.contains("抱歉，暂无相关数据。") || content.contains("您访问的页面出错了")) {
            return null;
        }
        Html html = Html.create(content);
        //取出id 解析其中的js，非贪婪匹配
        try {
            String idList = html.regex("specIDs =\\[(.*?)\\];", 1).get();
            String keyLink = html.regex("keyLink = (.*?);", 1).get();
            String config = html.regex("var config = (.*?);", 1).get();
            String option = html.regex("var option = (.*)var bag", 1).get().replace(";", "");

            Preconditions.checkNotNull(keyLink, "keyLink can not be null");
            Preconditions.checkNotNull(config, "config can not be null");
            Preconditions.checkNotNull(option, "option can not be null");
            //用于将来的扩展
//        String color = html.regex("var color = (.*?);",1).get();
//        String innerColor = html.regex("var innerColor=(.*?);",1).get();
            String[] ids = split(idList);
            List<String> nameList = parseStandardField(keyLink);
            Table<String, String, String> configList = parseJson(config, "paramtypeitems", "paramitems", "value", ids);
            Table<String, String, String> optionList = parseJson(option, "configtypeitems", "configitems", "value", ids);
////        List<Map<String, List<String>>> colorList = parseJsonForColor(color, ids);
////        List<Map<String, List<String>>> innerColorList = parseJsonForColor(innerColor, ids);
            List<List<Object>> fields = parseListNoColor(ids, configList, optionList, nameList);
            return fields;
        }catch (Exception e){
            writeStringtoFile(errorPath,url + "\n",true);
            return null;
        }
    }



    /**
     * 用来处理没有颜色的数据
     * @param ids
     * @param configList
     * @param optionList
     * @return
     */
    public static List<List<Object>> parseListNoColor(String[] ids,Table<String,String,String> configList,Table<String,String,String> optionList,
                                                      List<String> nameList){
        List<List<Object>> contentLists = Lists.newArrayList();
        List<Object> contentList = Lists.newArrayList();
        for (String id : ids) {
            parseSpecList(id,configList,optionList,contentList,nameList);
            contentLists.add(contentList);
            contentList = Lists.newArrayList();
        }
        return contentLists;
    }

    /**
     *  /**
     * [[{车型名称={1002785=宝斯通 2014款 3.0TVIP版NGD3.0-C3HA}}, {车型名称={1001002=宝斯通 2010款 2.2L高级版HFC4GA2-1B}}, {车型名称={1001001=宝斯通 2010款 3.0T高级版NGD3.0-C3HA}}]
     * [车型名称, 厂商指导价(元), 厂商, 级别, 发动机, 变速箱, 长*宽*高(mm), 车身结构, .....]
     * @param id
     * @param
     * @param contentList
     * @param nameList
     * 从list里找到name,如果没有name就赋值为""
     */
    private static void parseSpecList(String id,Table<String,String,String> configTable,Table<String,String,String> optionTable, List<Object> contentList
                                         ,List<String> nameList) {
        for (String name : nameList) {
            String value = configTable.get(name, id);
            if(value == null) {
                value = optionTable.get(name, id);
            }
            contentList.add(value);
        }
        contentList.add(id);

    }

    /**
     * 用于将来的扩展
      * @param id
     * @param list
     * @param contentList
     */
    private static void parseSpecListForColor(String id,List<Map<String,List<String>>> list,List<Object> contentList){
        for (Map<String, List<String>> map : list) {
            List<String> ss = map.get(id);
            if(ss == null) continue;
            contentList.add(ss);
        }
    }


    /**
     * 批量更新
     * @param links
     * @return
     * @throws IOException
     */
    public static void updateData(List<String> links,String errorPath) throws IOException {
        for (String link : links) {
            parseSpecificPage(link,errorPath);

        }
    }


}
