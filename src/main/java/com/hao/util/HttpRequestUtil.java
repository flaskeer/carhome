package com.hao.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 这一HTTP头一般格式如下:

 X-Forwarded-For: client1, proxy1, proxy2
 其中的值通过一个 逗号+空格 把多个IP地址区分开, 最左边（client1）是最原始客户端的IP地址, 代理服务器每成功收到一个请求，就把请求来源IP地址添加到右边。 在上面这个例子中，这个请求成功通过了三台代理服务器：proxy1, proxy2 及 proxy3。请求由client1发出，到达了proxy3（proxy3可能是请求的终点）。请求刚从client1中发出时，XFF是空的，请求被发往proxy1；通过proxy1的时候，client1被添加到XFF中，之后请求被发往proxy2;通过proxy2的时候，proxy1被添加到XFF中，之后请求被发往proxy3；通过proxy3时，proxy2被添加到XFF中，之后请求的的去向不明，如果proxy3不是请求终点，请求会被继续转发。

 鉴于伪造这一字段非常容易，应该谨慎使用X-Forwarded-For字段。正常情况下XFF中最后一个IP地址是最后一个代理服务器的IP地址, 这通常是一个比较可靠的信息来源。
 * Created by user on 2016/4/1.
 */
public enum  HttpRequestUtil {

    INSTANCE;

    public String getClientIP(HttpServletRequest request) {
        String xForwarded = request.getHeader("x-forwarded-for");
        String clientIP;
        if (xForwarded == null) {
            clientIP = request.getRemoteAddr();
        } else {
            clientIP = extractClientIpFromXForwardedFor(xForwarded);
        }
        return clientIP;
    }

    public String extractClientIpFromXForwardedFor(String xForwardedFor) {
        if (xForwardedFor == null) {
            String[] tokenize = xForwardedFor.trim().split(",");
            if (tokenize.length != 0) {
                return tokenize[0].trim();
            }
        }
        return null;
    }

}
