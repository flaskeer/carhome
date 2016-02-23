package com.cheche.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by user on 2016/2/17.
 */
public class HttpClientUtil {

    public static final CloseableHttpClient httpClient = HttpClients.custom().build();
    public static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private HttpClientUtil(){}

    private static RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(15000)
            .setConnectTimeout(15000)
            .setConnectionRequestTimeout(15000)
            .build();

    /**
     * 发送get请求 response里拿到的编码格式是GB2312
     * @param httpGet
     * @return
     */
    private static String sendGet(HttpGet httpGet){
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String content = null;
        try{
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try{
                IOUtils.copy(entity.getContent(),outputStream);
            }catch(Exception e){
                if(e instanceof IOException){
                }else{
                    throw e;
                }
            }
            content = new String(outputStream.toByteArray(),"gbk");
            EntityUtils.consumeQuietly(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(response != null){
                    response.close();
                }

            }catch (IOException e){
                if(logger.isDebugEnabled()){
                    logger.debug(e.getMessage());
                }
            }

        }
        return content;
    }

    public static void close() throws IOException {
        httpClient.close();
    }

    public static String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendGet(httpGet);
    }

    public static void main(String[] args) {

        String content = sendHttpsGet("http://www.autohome.com.cn/grade/carhtml/C.html");
        System.out.println(content);
    }
}
