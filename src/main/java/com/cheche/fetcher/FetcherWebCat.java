package com.cheche.fetcher;


import com.cheche.util.HttpClientUtil;
import com.google.common.collect.Maps;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

import static com.cheche.common.Commons.*;

/**
 * Created by user on 2016/3/14.
 */
public class FetcherWebCat {

    private static Document getRawContent(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie","NC%5FSPR=31; ASPSESSIONIDCWBBRRDT=PJCIKIHAMGDOBMNGCMKFNNMB; ASPSESSIONIDAUDCSSAT=DKIGDHPAAAAMFEHDEDAPKKEG; ASPSESSIONIDAWBBQQDT=CDOHGBCBFOMKGBCAMMMAGMII; ASPSESSIONIDAUBDSSAT=FECBJLEBPCGPFMHEFGFFLKAC; NC%5FEINSPNR%5FTYP=32; NC%5FKTYPNR%5FHISTORY=%C2%A71%241117184%24111%24102926%24%E4%B8%B0%E7%94%B0%24SOLUNA+%28AL50%29%241%2C5%C2%A71%243543%24111%24453%24%E4%B8%B0%E7%94%B0%24%E5%87%AF%E7%BE%8E%E7%91%9E+%28%5FV2%5F%29%242%2E0+Gli+16V+%28SV21%5F%2C+SV25%5F%29%C2%A71%241115712%24111%24102664%24%E4%B8%B0%E7%94%B0%24ARIST%24Arist%C2%A71%245517%2477%248631%24%E4%B8%89%E8%8F%B1%24ASX%242%2E0+i+%E5%9B%9B%E8%BD%AE%E9%A9%B1%E5%8A%A8%C2%A71%2433436%2477%248631%24%E4%B8%89%E8%8F%B1%24ASX%241%2E6; ASPSESSIONIDCGSBQQQR=HENDHBPCLLOFDBGPFEPEPPEC; NC%5FKATKZ=N; ASPSESSIONIDAGQCQRQR=DLFFKLBDJPPAENHDHHJMBGOB; NC%5FLKZ=TJ; TS01d8a546=01d76f378667f7e0e9d71f634ca40eb02976bf2daa7ca6283979d26c1ce8fd5d1f7a8bbb985fe6ebbde1a44760da2811d35ad9126aa83fa903d7ccd30e318486fac66ef5d8280da808efc63a7b0db189058d1e0804765da95905b086e3b368dfddeef5695f2c2d9a388b20bf3b7a511ea3c6f2b0c5e276037fe68c5df77e6a818a05245713b2364a41e49b69bcaae2468d8404328e17f4dbbe66be2222cedfdd96a1c18bbb089c919857ad6b6a1bd10458990d12569ab53c237226a21f25e08bf217603c4539be8bc1ff1a58b976622dd8d9cd0c87");
        String content = HttpClientUtil.sendGet(httpGet, "UTF-8");
        Document document = Jsoup.parse(content);
        return document;
    }

    private static Map<String, String> parseHomePage(String url) throws IOException {
        Map<String,String> map = Maps.newLinkedHashMap();
        Document document = getRawContent(url);
        Elements optElems = document.select(".SelectCourierGray > option");
        optElems.stream().filter(optElem -> !optElem.text().equals("Auswahl")).forEach(optElem -> {
            String value = optElem.attr("value");
            String text = optElem.text();
            map.put(value,text);
        });
        return map;
    }

    private static void getData(Map<String,String> map,String storePath) throws IOException {
       for (Map.Entry<String,String> manufacturerEntry : map.entrySet()) {
           String url = "https://webcat.zf.com/nc2_dialog.asp?MODE&KMODNR=0&SUCHEN2&KAT_KZ=N&KHERNR=" + manufacturerEntry.getKey();
           Map<String, String> typeMap = parseHomeList(url);
           for (Map.Entry<String,String> typeEntry : typeMap.entrySet()) {
               Map<String, String> hrefMap = parseHref(manufacturerEntry.getKey(), typeEntry.getKey());
               for (Map.Entry<String,String> hrefEntry : hrefMap.entrySet()) {
                   Map<String, String> dataMap = parseSpecPage(hrefEntry.getKey());
                   for (Map.Entry<String,String> dataEntry : dataMap.entrySet()) {
                       String data = "\"" + manufacturerEntry.getValue()  + "\"" + "," +
                                     "\"" + typeEntry.getValue() + "\"" + "," +
                                     "\"" + hrefEntry.getValue() + "\"" + "," +
                                     "\"" + dataEntry.getKey()   + "\"" +","  +
                                     "\"" + dataEntry.getValue() + "\"";
                       writeStringtoFile(storePath,data + "\n",true);
                       System.out.println("data:" + data);
                   }
               }
           }
       }
    }

    private static Map<String,String> parseSpecPage(String url) {
        Document content = getRawContent(url);
        Map<String,String> map = Maps.newLinkedHashMap();
        Elements row2Elems = content.select(".TableRow2");
        Elements row1Elems = content.select(".TableRow1");
        Map<String, String> row2Map = parseSpecData(row2Elems);
        Map<String, String> row1Map = parseSpecData(row1Elems);
        map.putAll(row1Map);
        map.putAll(row2Map);
        return map;
    }

    private static Map<String,String> parseSpecData(Elements elements) {
        Map<String,String> map = Maps.newLinkedHashMap();
        for (Element element : elements) {
            int tdSize = element.select("td").size();
            if(tdSize == 7) {
                String id = element.select("td").get(2).select("b > a").text();
                String name = element.select("td").get(5).text();
                String position = element.select("td").get(6).select("ul").get(0).select("li").text();
                if(name.contains("减振器")) {
                    map.put(id + "\"" + ","  +"\"" + name,position);
                }
            } else if (tdSize == 6){
                String id = element.select("td").get(2).select("b > a").text();
                String name = element.select("td").get(4).text();
                String position = element.select("td").get(5).select("ul").get(0).select("li").text();
                if(name.contains("减振器")) {
                    map.put(id + "\"" + ","  +"\"" + name,position);
                }
            } else{
                continue;
            }

        }
        return map;
    }

    private static Map<String, String> parseHomeList(String url) {
        Document content = getRawContent(url);
        Elements optElems = content.select(".SelectCourier > option");
        Map<String,String> map = Maps.newLinkedHashMap();
        optElems.stream().forEach(optElem -> {
            String value = optElem.attr("value");
            String text = optElem.text();
            map.put(value,text);
        });
        return map;
    }

    private static Map<String,String> parseHref(String manufacturerId,String typeId) {
        String url = "https://webcat.zf.com/nc2_dialog.asp?MODE=&SUCHEN2=OK&KAT_KZ=N&KHERNR=" + manufacturerId +"&KMODNR=" + typeId + "&BJ=&MCODE=&HUBRAUM=&HUBRAUM_10=X&HUBRAUM_10x=10&LEISTUNG=&LEISTUNG_ART=KW&LEISTUNG_10=%23%23&LEISTUNG_10x=10&DBPAGESIZE=0";
        Document content = getRawContent(url);
        Map<String,String> map = Maps.newLinkedHashMap();
        Elements aElems = content.select("a");
        for (Element aElem : aElems) {
            if(aElem.attr("href").startsWith("nc2_dialog_teile.asp")) {
                String needUrl = getNeedUrl(aElem.attr("href"));
                map.put(needUrl,aElem.select("b").text());
            }
        }
        return map;
    }

    private static String getNeedUrl(String url) {
        String substring = url.substring(url.indexOf("KAT_KZ=N") + 8);
        String link = "https://webcat.zf.com/nc2_dialog_teile.asp?MODE=&PICTO_SEL=&EINSPNR_TYP=32&SUCHEN=%E6%98%BE%E7%A4%BA%E9%83%A8%E4%BB%B6&DBPAGESIZE=50" + substring;
        return link;
    }


    public static void main(String[] args) throws IOException {
        Map<String, String> map = parseHomePage("https://webcat.zf.com/nc2_dialog.asp?MODE&KMODNR=0&SUCHEN2&KAT_KZ=P&KAT_KZ=N&KHERNR=2246");
        getData(map,"D:/tmp/webcat3.txt");
    }
}
