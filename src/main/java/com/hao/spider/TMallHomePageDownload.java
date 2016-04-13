package com.hao.spider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hao.model.spider.Page;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.selector.Html;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.hao.common.Commons.*;
/**
 * Created by user on 2016/4/13.
 */
public class TMallHomePageDownload implements Downloader{

    private static final Logger LOGGER = LoggerFactory.getLogger(TMallHomePageDownload.class);

    private static final String ASYNC_URL_TEMPLATE = "https://%s/widgetAsync.htm?ids=%s&path=%s&callback=callbackGetMods%s&site_instance_id=%s";

    @Override
    public Page download(String url) throws IOException {
        Preconditions.checkNotNull(url);
        URL indexUrl = new URL(url);
        Document document = getDocument(url, "UTF-8");
        String content = document.html();
        Html mainHtml = Html.create(content);
        String siteId = mainHtml.regex("site_instance_id=(\\d+)", 1).get();
        List<String> asyncIdList = mainHtml.xpath("//div[@class='J_TAsyncModule']/@data-widgetid").all();
        List<Html> asyncHtmlList = Lists.newArrayListWithExpectedSize(asyncIdList.size());
        for (String id : asyncIdList) {
            String aUrl = String.format(ASYNC_URL_TEMPLATE,indexUrl.getHost(),id,indexUrl.getPath(),id,siteId);
            Document aDocument = getDocument(aUrl, "UTF-8");
            String aHtml = aDocument.html();
            String aContent = aHtml.substring(aHtml.indexOf("{"), aHtml.lastIndexOf("}"));
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("content is :{}",aContent);
            }
            asyncHtmlList.add(Html.create(aContent));
            sleep(1,3);
        }
        return Page.create(url,mainHtml,asyncHtmlList);
    }
}
