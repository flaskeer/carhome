package com.hao.fetcher;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static com.hao.common.Commons.*;
import java.util.List;

/**
 * http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml
 * Created by user on 2016/4/11.
 */
public class FetcherContitech {

    public static List<String> brands() {
        String url = "http://aam-china.contitech.de/pages/web-katalog/web-katalog_cn.cshtml";
        Document content = getDocument(url, "UTF-8");
        Elements optElems = content.select("#Select1_Markenauswahl").select("option");
        List<String> brands = Lists.newArrayList();
        optElems.stream().filter(optElem -> !optElem.attr("value").equals("0")).forEach(optElem -> brands.add(optElem.text()));
        System.out.println(brands);
        return brands;
    }




    public static void main(String[] args) throws Exception {
        brands();
    }

}
