package com.hao.fetcher;

import com.hao.util.HttpClientUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


import static com.hao.common.Commons.*;

/**
 * Created by user on 2016/3/1.
 */
public class FetcherFilter {

    public static final Pattern pattern = Pattern.compile("\\<\\!\\[CDATA\\[(?<text>[^\\]]*)\\]\\]\\>");

    private static void fetchLink(String param) throws Exception {
        String url = "http://www.filter-tora.com//aspect/aspectCascading.aspx?aspectuid=d2-270668214-2a262&docuid=270668214&mdocuid=0&ev=onAdvanceSearch&m=1&r=" + Math.random();
        Map<String,String> map = Maps.newLinkedHashMap();
        map.put("advanceSearchParam1",param);
        String content = HttpClientUtil.sendHttpPost(url, map);
        content = filter(content);
        List<String> pageUlrs = Lists.newArrayList();
        Document document = Jsoup.parse(content,url);
        Elements aElems = document.select("a");
        String lastPage = aElems.get(aElems.size() - 2).text();
        int lastPageId = Integer.parseInt(lastPage);
        for (int i = 1; i <= lastPageId; i++) {
            String pageUrl = "http://www.filter-tora.com/Aspect/GetAspectPageContent.aspx?auid=d2-270668214-2a264&docuid=270668214&curPid=" + i;
            System.out.println("pageurl is:" + pageUrl);
            pageUlrs.add(pageUrl);
        }
       parseLink(pageUlrs);
    }

    private static String filter(String content){
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            content = matcher.group(1);
        }
        content.replaceAll("&quot;","\"");
        content.replaceAll("&amp;","&");
        content.replaceAll("&lt;","<");
        content.replaceAll("&gt;",">");
        content.replaceAll("&nbsp;"," ");
        return content;
    }

    private static void parseLink(List<String> pageUrls){
        HashSet<String> sets = Sets.newLinkedHashSet();
        pageUrls.forEach(pageUrl ->{
            System.out.println("start crawl href:" + pageUrl);
            try {
                Map<String,String> map = Maps.newLinkedHashMap();
                map.put("mynet_CSubjectParameter","AAEAAAD/////AQAAAAAAAAAMAgAAAEZjbi5teW5ldC5wbHVnaW4sIFZlcnNpb249MS4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxsDAMAAABCRGF0YU1lbWJlcnMsIFZlcnNpb249MS4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxsBQEAAAAhY24ubXluZXQucGx1Z2luLkNTdWJqZWN0UGFyYW1ldGVyBgAAABlfY2hhbmdlZFBob3RvQ2F0ZWdvcnlOYW1lEl9jaGFuZ2VkUGFyYW1ldGVycxxfY2hhbmdlZERvY1NlYXJjaENvbmR0aW9uQ29sH19jaGFuZ2VkTWVtYmVyU2VhcmNoQ29uZHRpb25Db2waX2NoYW5nZWRTbWFydERhdGFHcm91cE5hbWUVX2NoYW5nZWRQcm9wZXJ0eU5hbWVzAQMEBAED4gFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5EaWN0aW9uYXJ5YDJbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV0sW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dK2NuLm15bmV0LkRhdGFNZW1iZXJzLkNEb2NTZWFyY2hDb25kaXRpb25Db2wDAAAALmNuLm15bmV0LkRhdGFNZW1iZXJzLkNNZW1iZXJTZWFyY2hDb25kaXRpb25Db2wDAAAAf1N5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbU3lzdGVtLlN0cmluZywgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0CAAAABgQAAAAACQUAAAAJBgAAAAkHAAAACQQAAAAJCQAAAAQFAAAA4gFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5EaWN0aW9uYXJ5YDJbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV0sW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dAwAAAAdWZXJzaW9uCENvbXBhcmVyCEhhc2hTaXplAAMACJIBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuR2VuZXJpY0VxdWFsaXR5Q29tcGFyZXJgMVtbU3lzdGVtLlN0cmluZywgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0IAAAAAAkKAAAAAAAAAAUGAAAAK2NuLm15bmV0LkRhdGFNZW1iZXJzLkNEb2NTZWFyY2hDb25kaXRpb25Db2wRAAAAFF9tdWx0eUtleVdvcmRzU2VhcmNoDV9yb3RhdGlvblJ1bGUOX2RldGFpbFBhZ2VVSUQFX3RvcE4OX2RhdGFJdGVtQ291bnQOX3RvcE1vc3REb2NJZHMKX3BhZ2VJbmRleAlfcGFnZVNpemUNX29yZGVyQnlGaWVsZAhzb3J0REVTQw9fbWFya0V4cHJlc3Npb24PX2NvbmRpdGlvbkluZGV4DV9jb250YWluc0dpZnQXX2NvbnRhaW5zQmluZGluZ1Byb2R1Y3QPX2xpc3RDb25kaXRpb25zEV9hZHZhbmNlQ29uZHRpb25zFF9pc0RlZmF1bHREYXRhU291cmNlAAEBAAADAAABAAEAAAADAwABCAh,U3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uSW50MzIsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dCAgBCAEBkQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW2NuLm15bmV0LkRhdGFNZW1iZXJzLkNEb2NTZWFyY2hDb25kaXRpb24sIERhdGFNZW1iZXJzLCBWZXJzaW9uPTEuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49bnVsbF1dkQFTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW2NuLm15bmV0LkRhdGFNZW1iZXJzLkNEb2NTZWFyY2hDb25kaXRpb24sIERhdGFNZW1iZXJzLCBWZXJzaW9uPTEuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49bnVsbF1dAQMAAAABCgkEAAAAAAAAAEkBAAAJDAAAAAEAAAAKAAAABg0AAAALY3JlYXRlZHRpbWUBBg4AAAAbIEFORCAkezB9IEFORCAkezF9IEFORCAkezJ9AwAAAAABCQ8AAAAJEAAAAAAFBwAAAC5jbi5teW5ldC5EYXRhTWVtYmVycy5DTWVtYmVyU2VhcmNoQ29uZGl0aW9uQ29sAgAAABVfc2VhcmNoRmllbGRWYWx1ZUxpc3QUX3NlYXJjaEZpZWxkTmFtZUxpc3QDA39TeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1df1N5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbU3lzdGVtLlN0cmluZywgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0DAAAACgoECQAAAH9TeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJpYy5MaXN0YDFbW1N5c3RlbS5TdHJpbmcsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24GAAAICAkRAAAAAQAAAAEAAAAECgAAAJIBU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuR2VuZXJpY0VxdWFsaXR5Q29tcGFyZXJgMVtbU3lzdGVtLlN0cmluZywgbXNjb3JsaWIsIFZlcnNpb249NC4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1iNzdhNWM1NjE5MzRlMDg5XV0AAAAABAwAAAB,U3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdGAxW1tTeXN0ZW0uSW50MzIsIG1zY29ybGliLCBWZXJzaW9uPTQuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OV1dAwAAAAZfaXRlbXMFX3NpemUIX3ZlcnNpb24HAAAICAgJEgAAAAAAAAAAAAAABA8AAACRAVN5c3RlbS5Db2xsZWN0aW9ucy5HZW5lcmljLkxpc3RgMVtbY24ubXluZXQuRGF0YU1lbWJlcnMuQ0RvY1NlYXJjaENvbmRpdGlvbiwgRGF0YU1lbWJlcnMsIFZlcnNpb249MS4wLjAuMCwgQ3VsdHVyZT1uZXV0cmFsLCBQdWJsaWNLZXlUb2tlbj1udWxsXV0DAAAABl9pdGVtcwVfc2l6ZQhfdmVyc2lvbgQAACpjbi5teW5ldC5EYXRhTWVtYmVycy5DRG9jU2VhcmNoQ29uZGl0aW9uW10DAAAACAgJEwAAAAMAAAADAAAAARAAAAAPAAAACRQAAAAAAAAAAAAAABERAAAABAAAAAYVAAAAG0NoYW5nZWREb2NTZWFyY2hDb25kaXRvbkNvbA0DDxIAAAAAAAAACAcTAAAAAAEAAAAEAAAABChjbi5teW5ldC5EYXRhTWVtYmVycy5DRG9jU2VhcmNoQ29uZGl0aW9uAwAAAAkWAAAACRcAAAAJGAAAAAoHFAAAAAABAAAAAAAAAAQoY24ubXluZXQuRGF0YU1lbWJlcnMuQ0RvY1NlYXJjaENvbmRpdGlvbgMAAAAFFgAAAChjbi5teW5ldC5EYXRhTWVtYmVycy5DRG9jU2VhcmNoQ29uZGl0aW9uAwAAAApfZmllbGROYW1lDV9vcGVyYXRvclR5cGULX2ZpZWxkVmFsdWUBBAIoY24ubXluZXQuRGF0YU1lbWJlcnMuRVNlYXJjaE9wZXJhdG9yVHlwZQMAAAADAAAABhkAAAAHdmlzaWJsZQXm////KGNuLm15bmV0LkRhdGFNZW1iZXJzLkVTZWFyY2hPcGVyYXRvclR5cGUBAAAAB3ZhbHVlX18ACAMAAAACAAAABhsAAAABMQEXAAAAFgAAAAYcAAAAC2RvY3R5cGVOYW1lAeP////m////BAAAAAYeAAAAF3Byb2R1Y3RfY3VzdG9tZXJfbXBfMTAxARgAAAAWAAAABh8AAAAKY2F0ZWdvcnlpZAHg////5v///wYAAAAJIQAAAA8hAAAAAQAAAAjoAwAACw==");
                String pageContent = HttpClientUtil.sendHttpPost(pageUrl, map);
                String replace = pageContent.replace("&lt;", "<").replace("&gt;", ">");
                Document document = Jsoup.parse(replace);
                Elements aELems = document.select("table").select("a");
                for (Element aELem : aELems) {
                    String href = aELem.attr("href");
                    String absHref = "http://www.filter-tora.com" + href;
                    System.out.println("abshref is:" + absHref);
                    sets.add(absHref);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sets.forEach(href -> {
            try {
                writeStringtoFile("D:/tmp/filter_link.txt", href + "\n", true);
            } catch (IOException e) {}
        });
    }

    public static void parseSpecLink(String url){
        List<String> contents = Lists.newArrayList();
        Document document = getDocument(url,"utf-8");
        String imgSrc = document.select("img").attr("src");
        contents.add(imgSrc);
        Elements pElems = document.select("#divCascading_d2-270666610-2a31 > div > p");
        System.out.println("pelems size:" + pElems.size());
        for (Element pElem : pElems) {
            String text = pElem.text();
            System.out.println("raw text:" + text);
            String val = text.replaceAll("\\u00A0","");
            System.out.println("now text:" + val);
            contents.add(val);
        }
        List<String> collect = contents.stream().map(content -> "\"" + content + "\"" + ",").collect(Collectors.toList());
        String result = "";
        for (String s : collect) {
            result += s;
        }
        System.out.println(result);
        try {
            writeStringtoFile("D:/tmp/filter_content.txt",result + "\n",true);
        } catch (IOException e) {}

    }


    public static void main(String[] args) {
//        List<String> params = Lists.newArrayList();
//        params.add("1000");
//        params.add("1001");
//        params.add("1003");
//        params.add("1004");
//        params.forEach(param -> {
//            try {
//                fetchLink(param);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        try {
            List<String> links = readLink("D:/tmp/filter_link.txt");
            links.forEach(link ->{
                parseSpecLink(link);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
