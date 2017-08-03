package com.business.intelligence.crawler.eleme;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
import com.business.intelligence.util.WebUtils;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.response.Token;
import lombok.Setter;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by Tcqq on 2017/7/18.
 */
public abstract class ElemeCrawler extends BaseCrawler{

    //登录
    private static final String USERNAME = "hwfzhongke";
    private static final String PASSWORD = "abc123456";
    private static final String LOGINURL = "https://app-api.shop.ele.me/arena/invoke/?method=LoginService.loginByUsername";
    protected static final String SHOPID = "204666";

    //登出
    private static final String LOGOUTURL = "https://melody.shop.ele.me/app/shop/150148671/stats/business";


    /**
     * 登录
     */
    protected CloseableHttpClient login() {
        CloseableHttpResponse httpResponse = null;
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient client = HttpClientUtil.getHttpClient(cookieStore);
        String content=null;
        HttpPost httppost = new HttpPost(LOGINURL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"2bbb7b48-c428-4158-b30d-78dc93a8e6f1\",\"method\":\"loginByUsername\",\"service\":\"LoginService\",\"params\":{\"username\":\""+USERNAME+"\",\"password\":\""+PASSWORD+"\",\"captchaCode\":\"\",\"loginedSessionIds\":[]},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        httppost.setEntity(jsonEntity);
        httppost.setHeader("Content-type", "application/json;charset=utf-8");
        httppost.setHeader("Host", "app-api.shop.ele.me");
        httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        httppost.setHeader("Accept", "*/*");
        httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httppost.setHeader("Accept-Encodinge", "gzip, deflate, br");
        httppost.setHeader("X-Shard", "shopid="+SHOPID+"");
        httppost.setHeader("X-Eleme-RequestID", "1aa7820f-49cf-48bd-9e8a-e41e478528b8");
        httppost.setHeader("Referer", "https://melody.shop.ele.me/login");
        httppost.setHeader("origin", "https://melody.shop.ele.me");
        httppost.setHeader("Connection", "keep-alive");
        try{
            httpResponse = client.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();
            content = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                System.out.println(content);
            }
        }catch (IOException e){
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return client;

    }

    public static void main(String[] args) {
        ElemeCrawler elemeCrawler = new ElemeCrawler() {
            @Override
            public void doRun() {

            }
        };
       elemeCrawler.logOut(elemeCrawler.login());
    }

    /**
     *登出
     */
    public void logOut(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        HttpGet get = new HttpGet(LOGOUTURL);
        get.setHeader("Cookie","perf_ssid=x0ruhdmzvgqd999uv3wkznb7ett7bnbl_2017-07-20; ubt_ssid=umald6k97vny7o9pxha8pye7mxu8ijzt_2017-07-20; _utrace=8105d34303396caf9f636842a6e511b9_2017-07-20; _ga=GA1.2.1285076203.1501035869");
        try {
            execute = client.execute(get);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (execute != null){
                    execute.close();
                }
                if(client != null){
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置饿了么网页的header
     */
    protected void setElemeHeader(HttpPost post){
        post.setHeader("Content-type", "application/json;charset=utf-8");
        post.setHeader("Host", "app-api.shop.ele.me");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Accept-Encodinge", "gzip, deflate, br");
        post.setHeader("X-Shard", "shopid="+SHOPID+"");
        post.setHeader("Referer", "https://melody-stats.faas.ele.me/");
        post.setHeader("origin", "https://melody-stats.faas.ele.me");
        post.setHeader("Connection", "keep-alive");
    }
}
