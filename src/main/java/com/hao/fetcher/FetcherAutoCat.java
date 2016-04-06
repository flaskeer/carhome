package com.hao.fetcher;

import com.google.common.collect.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hao.common.Commons.*;
/**
 *
 *爬取此网站 http://autocat.gates.cn/App/CarSearch
 * Created by user on 2016/4/5.
 */
public class FetcherAutoCat {

    private static ExecutorService executorService = Executors.newFixedThreadPool(1000);
    private static LinkedBlockingQueue<String> specUrlQueue = new LinkedBlockingQueue<>();
    /**
     * 从http://autocat.gates.cn/App/CarSearch去爬取到所有的品牌
     * @param url
     * @return
     */
    public static List<String> fetchBrand(String url) {
        Document content = null;
        try {
            content = getDocument(url, "UTF-8");
        } catch (Exception e) {
            //如果报异常证明抓取有问题  重新连接
            fetchBrand(url);
        }
        Elements optElems = content.select("#CarBrand").select("option");
        List<String> brandList = Lists.newArrayList();
        optElems.stream().filter(optElem -> !optElem.text().isEmpty()).forEach(optElem -> brandList.add(optElem.attr("value")));
        return brandList;
    }


    /**
     * 抓取到所有的品牌连接 拼接出 品牌的url 为下一步做准备
     * @param url http://autocat.gates.cn/App/CarSearch
     * @return
     */
    public static List<String> fetchBrandLinks(String url) {
        List<String> brandList = fetchBrand(url);
        List<String> links = Lists.newArrayList();
        brandList.forEach(brand -> {
            String specLink = "http://autocat.gates.cn/App/CarSearch?brand=" + brand;
            links.add(specLink);
        });
        return links;
    }

    /**
     * 传入的值为#{fetchBrandLinks(url)} 抓取的链接
     * @param link
     * @return
     */
    public static List<String> parseBrandLink(String link) {
        Document content = null;
        try {
            content = getDocument(link, "UTF-8");
        } catch (Exception e) {
            parseBrandLink(link);
        }
        Elements trElems = content.select(".CarListBox");
        List<String> needUrls = Lists.newArrayList();
        trElems.forEach(trElem -> {
            String onclick = trElem.attr("onclick");
            String number = onclick.substring(onclick.indexOf("'")+1,onclick.lastIndexOf("'"));
            String needUrl = "http://autocat.gates.cn/App/CarProducts?GatesNumber=" + number;
            needUrls.add(needUrl);
        });
        return needUrls;
    }

    /**
     * 分析详情页参数
     * @param specLink
     */
    public static void parseSpecPage(String specLink,String filePath) {
        StringBuilder builder = new StringBuilder();
        Document content = null;
        try {
            content = getDocument(specLink, "UTF-8");
        } catch (Exception e) {
            parseSpecPage(specLink,filePath);
        }
        while (content == null) {
            parseSpecPage(specLink,filePath);
        }
        Elements tableElems = content.select("table");
        Elements tdElems = null;
        if (tableElems != null) {
            tdElems = tableElems.get(1).select("tr").get(1).select("td");
        }
        if (tdElems != null) {
            tdElems.forEach(tdElem -> {
               builder.append("\"").append(tdElem.text()).append("\"").append(",");
            });
        }
        System.out.println("now downloading url is:" + specLink);
        Elements productTrElems = null;
        if (tableElems != null) {
            productTrElems = tableElems.get(2).select("tr");
        }
        if(productTrElems == null) {
            System.out.println(content);
        } else {
            productTrElems.stream().filter(productTrElem -> !productTrElem.text().contains("技术支持")).forEach(productTrElem -> {
                Elements productTdElems = productTrElem.select("td");
                productTdElems.forEach(productTdElem -> {
                    builder.append("\"").append(productTdElem.text()).append("\"").append(",");
                });
            });
            try {
                writeStringtoFile(filePath, builder.toString() + "\n", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(builder.toString());
        }
    }

    public static void execute(String url,String storePath) throws Exception {
        List<String> brandUrls = fetchBrandLinks(url);
        for (String brandUrl : brandUrls) {
            List<String> specUrls = parseBrandLink(brandUrl);
            for (String specUrl : specUrls) {
                specUrlQueue.put(specUrl);
            }
        }

        for (int i = 0; i < 300; i++) {
            executorService.execute(() -> {
                while (true) {
                    try {
                        parseSpecPage(specUrlQueue.take(), storePath);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public static void main(String[] args) {
        try {
            execute("http://autocat.gates.cn/App/CarSearch","D:/tmp/autocat.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
