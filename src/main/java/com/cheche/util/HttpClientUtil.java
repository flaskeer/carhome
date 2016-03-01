package com.cheche.util;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

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
     * 汽车之家中网页编码格式是gb2312
     * 发送get请求 response里拿到的编码格式是GB2312
     * @param httpGet
     * @return
     */
    private static String sendGet(HttpGet httpGet,String charset){
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
            //gbk是gb2312的扩展字符集  用来防止某些字显示乱码的问题
            content = new String(outputStream.toByteArray(),charset);
            EntityUtils.consumeQuietly(entity);
        } catch (Exception e) {
            if(e instanceof SocketTimeoutException){
                try {
                    throw e;
                } catch (IOException e1) {}
            }
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

    /**
     * 专门用来获取汽车之家的请求
     * @param httpUrl
     * @return
     */
    public static String sendHttpsGet(String httpUrl) {

        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendGet(httpGet,"gbk");
    }

    /**
     * 用来处理通用的请求
     * @param url
     * @param charset
     * @return
     */
    public static String sendHttpGet(String url,String charset){
        HttpGet httpGet = new HttpGet(url);
        return sendGet(httpGet,charset);
    }

    /**
     * 发送post请求  提交表单数据
     * @param url
     * @param maps
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String sendHttpPost(String url, Map<String,String> maps) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = Lists.newArrayList();
        maps.forEach((k,v) -> nameValuePairs.add(new BasicNameValuePair(k,v)));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
        return sendHttpPost(httpPost);
    }

    private static String sendHttpPost(HttpPost httpPost) {
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try{
            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity,"UTF-8");
        } catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static void main(String[] args) {

        String content = sendHttpsGet("http://www.autohome.com.cn/grade/carhtml/C.html");
        System.out.println(content);
    }
}
