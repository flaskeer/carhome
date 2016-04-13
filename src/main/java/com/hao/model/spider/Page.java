package com.hao.model.spider;

import us.codecraft.webmagic.selector.Html;

import java.util.List;

/**
 * 包含 含有Ajax和非Ajax数据的方式
 * Created by user on 2016/4/13.
 */
public class Page {

    private String request;

    private Html html;

    private boolean hasAjax;

    private List<Html> ajaxHtml;

    public Page() {
    }

    public Page(String request, Html html, boolean hasAjax, List<Html> ajaxHtml) {
        this.request = request;
        this.html = html;
        this.hasAjax = hasAjax;
        this.ajaxHtml = ajaxHtml;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public Html getHtml() {
        return html;
    }

    public void setHtml(Html html) {
        this.html = html;
    }

    public boolean isHasAjax() {
        return hasAjax;
    }

    public void setHasAjax(boolean hasAjax) {
        this.hasAjax = hasAjax;
    }

    public List<Html> getAjaxHtml() {
        return ajaxHtml;
    }

    public void setAjaxHtml(List<Html> ajaxHtml) {
        this.ajaxHtml = ajaxHtml;
    }

    public static Page create(String request,Html mainHtml) {
        return new Page(request,mainHtml,false,null);
    }

    public static Page create(String request,Html mainHtml,List<Html> ajaxHtml) {
        return new Page(request,mainHtml,true,ajaxHtml);
    }
}
