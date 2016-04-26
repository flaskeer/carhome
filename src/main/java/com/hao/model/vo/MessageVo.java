package com.hao.model.vo;

/**
 * Created by user on 2016/2/24.
 */
public class MessageVo {

    /**
     * 对应页面消息框的html class
     */
    private String type = "info";

    /**
     * 页面图标属性
     */
    private String icon;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息框是否有关闭按钮
     */
    private boolean close = true;

    public MessageVo() {
    }

    public MessageVo(String type, String icon, String content, boolean close) {
        this.type = type;
        this.icon = icon;
        this.content = content;
        this.close = close;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}
