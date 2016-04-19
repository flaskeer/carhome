package com.hao.fetcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hao.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hao.common.Commons.*;
/**
 *
 * 抓取此网站：http://qichechaxun15.cw503.4everdns.com/query.aspx
 * Created by user on 2016/4/18.
 */
public class FetcherBrakes {

    private static final String url = "http://qichechaxun15.cw503.4everdns.com/query.aspx";
    private static LinkedHashMap<String,String> generalForm = new LinkedHashMap();

    static {
        generalForm.put("__EVENTTARGET","");
        generalForm.put("__EVENTARGUMENT","");
        generalForm.put("__LASTFOCUS","");


        generalForm.put("__VIEWSTATE","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");

        generalForm.put("__VIEWSTATEGENERATOR","EDD8C9AE");
    }


    public static List<String> getBrands() {
        Document content = getDocument(url, "UTF-8");
        if (content == null) {
            getBrands();
        }

        Elements viewState = content.select("#__VIEWSTATE");
        String value = viewState.attr("value");
        generalForm.put("__VIEWSTATE",value);

        Elements optElems = content.select("#DropDownList1 > option");
        List<String> brands = Lists.newArrayList();
        optElems.stream().filter(optElem -> !optElem.text().equals("选择-品牌")).forEach(optElem -> brands.add(optElem.text()));
        brands.remove(0);   //去除第一个空格
        return brands;
    }

    public static List<String> getManufacturer(String brand) throws Exception {
        Map<String,String> form = Maps.newLinkedHashMap();
        form.putAll(generalForm);
        form.put("DropDownList1",brand);
        String html = HttpClientUtil.sendHttpPost(url, form);
        if (html == null) {
            getManufacturer(brand);
        }
        Document content = Jsoup.parse(html);
        Elements viewState = content.select("#__VIEWSTATE");
        String value = viewState.attr("value");
        generalForm.put("__VIEWSTATE",value);// 这个值必须替换成正确的值  否则取不到数据
        System.out.println(generalForm);
        Elements optElems = content.select("#DropDownList2 > option");
        List<String> manus = Lists.newArrayList();
        optElems.stream().filter(optElem -> !optElem.text().equals("选择-制造商")).forEach(optElem -> manus.add(optElem.text()));
        return manus;
    }

    public static List<List<String>> getType(String brand,String manu) throws Exception {
        Map<String,String> form = Maps.newLinkedHashMap();
        form.putAll(generalForm);
        form.put("DropDownList1",brand);
        form.put("DropDownList2",manu);
        String html = HttpClientUtil.sendHttpPost(url, form);
        if (html == null) {
            getType(brand,manu);
        }
        Document content = Jsoup.parse(html);

        Elements viewState = content.select("#__VIEWSTATE");
        String value = viewState.attr("value");
        generalForm.put("__VIEWSTATE",value);

        Elements optElems1 = content.select("#DropDownList3 > option");
        List<String> types = Lists.newArrayList();   // 车型
        optElems1.stream().filter(optElem -> !optElem.text().equals("选择-车型")).forEach(optElem -> types.add(optElem.text()));
        List<String> axis = Lists.newArrayList();  // 轴
        Elements optElems2 = content.select("#TextBox1 > option");
        optElems2.stream().filter(optElem -> !optElem.text().equals("选择-类型")).forEach(optElem -> axis.add(optElem.text()));
        List<String> productLines = Lists.newArrayList();   //产品线
        Elements optElems3 = content.select("#DropDownList4 > option");
        optElems3.stream().filter(optElem -> !optElem.text().equals("选择-类型")).forEach(optElem -> productLines.add(optElem.text()));
        List<List<String>> lists = Lists.newArrayList();
        lists.add(types);
        lists.add(axis);
        lists.add(productLines);
        return lists;
    }

    public static List<List<String>> getSpecData(String brand,String manu,String type,String axis,String productLine) throws Exception {
        Map<String,String> form = Maps.newLinkedHashMap();
        form.putAll(generalForm);
        form.put("DropDownList1",brand);
        form.put("DropDownList2",manu);
        form.put("DropDownList3",type);
        form.put("TextBox1",axis);
        form.put("DropDownList4",productLine);
        form.put("TextBox2","");
        form.put("ImageButton1.x","35");   //最好给个随机值  必须加上 否则取不到值
        form.put("ImageButton1.y","6");  //最好给个随机值   必须加上 否则取不到值  很操蛋的问题
        String html = HttpClientUtil.sendHttpPost(url, form);
        if (html == null) {
            getSpecData(brand, manu, type, axis, productLine);
        }
        Document content = Jsoup.parse(html);

        Elements viewState = content.select("#__VIEWSTATE");
        String value = viewState.attr("value");
        generalForm.put("__VIEWSTATE",value);

        Elements trElems = content.select(".query01 > table > tbody > tr");
        List<List<String>> dataList = Lists.newArrayListWithCapacity(trElems.size());
        for (Element trElem : trElems) {
            String href = trElem.select("td > a").attr("href");
            href = href.replace(" ","%20");
            String specUrl = "http://qichechaxun15.cw503.4everdns.com/" + href;

            System.out.println("now downloading spec url:" + specUrl);
            List<String> param = getParam(specUrl);
            Elements tdElems = trElem.select("td");
            List<String> data = Lists.newArrayList();
            for (Element tdElem : tdElems) {
                data.add(tdElem.text());
            }

            data.addAll(param);
            dataList.add(data);
        }
        System.out.println(dataList);
        return dataList;

    }

    public static List<String> getParam(String specUrl) {
        if (Objects.equals(specUrl,"http://qichechaxun15.cw503.4everdns.com/")) {
            return Collections.emptyList();
        }

        if (!specUrl.startsWith("http://qichechaxun15.cw503.4everdns.com/query_detail.aspx?user_email=&")) {
            return Collections.emptyList();
        }
        Document content = getDocument(specUrl, "UTF-8");
        if (content == null) {
            getParam(specUrl);
        }
        List<String> params = Lists.newArrayList();
        String param = content.select(".query_right02").get(0).text();
        String imgHref = content.select(".bd > ul > img").attr("src");
        if (Objects.equals(imgHref, "")) {
            imgHref = content.select(".bd > ul > li > img").attr("src");
        }
            try {
                imgHref = URLEncoder.encode(imgHref, "UTF-8");
                imgHref.replace("%2F",".");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        String imgUrl = "http://qichechaxun15.cw503.4everdns.com/" + imgHref;
        params.add(param);
        params.add(imgUrl);
        return params;
    }

    public static void execute(String storePath) throws Exception {
        List<String> brands = getBrands();
        for (String brand : brands) {
            List<String> manufacturers = getManufacturer(brand);
            for (String manufacturer : manufacturers) {
                List<List<String>> lists = getType(brand, manufacturer);
                List<String> types = lists.get(0);    //个数不确定
                List<String> axis = lists.get(1);   // 个数为2
                List<String> productLines = lists.get(2);  //个数为2
                for (String type : types) {
                    for (String axi : axis) {
                        for (String productLine : productLines) {
                            List<List<String>> specDataList = getSpecData(brand, manufacturer, type, axi, productLine);
                            for (List<String> specData : specDataList) {
                                String result = "";
                                for (String s : specData) {
                                    result += "\"" + s + "\"" + ",";
                                }
                                System.out.println(result);
                                writeStringtoFile(storePath,result + "\n",true);
                            }
                        }
                    }
                }
            }
        }
    }



    public static void main(String[] args) throws Exception {
//        getBrands();
//        getManufacturer("奔腾");
//        getType("本田","本田");
//        getSpecData("保时捷","保时捷","Cayman","前轴","刹车盘");
//        getParam("http://qichechaxun15.cw503.4everdns.com/query_detail.aspx?user_email=17");
        execute("D:/tmp/brakes_0419_test.txt");
    }

}
