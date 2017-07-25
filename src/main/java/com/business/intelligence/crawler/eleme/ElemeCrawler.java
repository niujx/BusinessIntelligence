package com.business.intelligence.crawler.eleme;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Tcqq on 2017/7/18.
 */
public abstract class ElemeCrawler extends BaseCrawler{
    @Setter
    protected Token token = getToken();
    @Setter
    protected Config config = getConfig();

    //获得Token
    private static final String ACCESSTOKEN = "d6962561958e53cd6dbb724d38e8ec12";
    private static final String TOKENTYPE = "Bearer";
    private static final long EXPIRES = 86400;
    private static final String REFRESHTOKEN = "695aea84c6b186a10d1f32dfe9a0d858";

    //配置Config
    private static final boolean ISSANDBOX= true;
    private static final String APPKEY = "l6kCfCKiDI";
    private static final String APPSECRET = "dc24f210a1ca72364b726b230c5672115d87f6bf";

    //登录
    private static final String USERNAME = "15201633321_eleme";
    private static final String PASSWORD = "yang170106";
    private static final String LOGINURL = "https://app-api.shop.ele.me/arena/invoke/?method=LoginService.loginByUsername";

    /**
     * 获得Token
     * @return
     */
    private Token getToken(){
        Token token = new Token();
        token.setAccessToken(ACCESSTOKEN);
        token.setTokenType(TOKENTYPE);
        token.setExpires(EXPIRES);
        token.setRefreshToken(REFRESHTOKEN);
        return token;
    }

    /**
     * 配置Config
     * @return
     */
    private Config getConfig(){
        return new Config(ISSANDBOX,APPKEY,APPSECRET);
    }

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
        String json = "{\"id\":\"22f73c49-f96f-42b7-8716-ec4177a4d25a\",\"method\":\"loginByUsername\",\"service\":\"LoginService\",\"params\":{\"username\":15201633321_eleme,\"password\":"+PASSWORD+",\"captchaCode\":\"\",\"loginedSessionIds\":[]},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        httppost.setEntity(jsonEntity);
        httppost.setHeader("Content-type", "application/json;charset=utf-8");
        httppost.setHeader("Host", "app-api.shop.ele.me");
        httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        httppost.setHeader("Accept", "*/*");
        httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httppost.setHeader("Accept-Encodinge", "gzip, deflate, br");
        httppost.setHeader("X-Shard", "shopid=150148671");
        httppost.setHeader("X-Eleme-RequestID", "22f73c49-f96f-42b7-8716-ec4177a4d25a");
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

    /**
     * 设置饿了么网页跳掉的header
     */
    protected void setElemeHeader(HttpPost post){
        post.setHeader("Content-type", "application/json;charset=utf-8");
        post.setHeader("Host", "app-api.shop.ele.me");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Accept-Encodinge", "gzip, deflate, br");
        post.setHeader("X-Shard", "shopid=150148671");
        post.setHeader("Referer", "https://melody-stats.faas.ele.me/");
        post.setHeader("origin", "https://melody-stats.faas.ele.me");
        post.setHeader("Connection", "keep-alive");
    }
}
