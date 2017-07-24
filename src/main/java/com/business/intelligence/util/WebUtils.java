package com.business.intelligence.util;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tcqq on 2017/7/17.
 */
public class WebUtils {

    /**根据url获得网页的body部分
     * @param url
     * @return
     */
    public static Element getElement(String url){
        Element element =null;
        try {
            Document doc = Jsoup.connect(url).get();
            element = doc.body();
            return element;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }

    /**利用Get提交根据url获得ajax返回内容
     * @param url
     * @param httpClient
     * @return
     */
    public static String getWebByGet(String url, HttpClient httpClient){
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse execute = httpClient.execute(httpGet);
            HttpEntity entity = execute.getEntity();
            String str = EntityUtils.toString(entity);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**利用Post提交根据url获得ajax返回内容
     * @param url
     * @param httpClient
     * @return
     */
    public static String getWebByPost(String url, HttpClient httpClient){
        HttpPost httpPost = new HttpPost(url);
        try {
            HttpResponse execute = httpClient.execute(httpPost);
            HttpEntity entity = execute.getEntity();
            String str = EntityUtils.toString(entity);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**通过json串和jsonPath格式获得具体值
     * @param json
     * @param jsonPath
     * @return
     */
    public static Object getOneByJsonPath(String json,String jsonPath){
        return JsonPath.read(json,jsonPath);
    }

    /**通过json串和jsonPath格式获得map
     * @param json
     * @param jsonPath
     * @return
     */
    public static List<LinkedHashMap<String,Object>> getMapsByJsonPath(String json,String jsonPath){
        return JsonPath.read(json,jsonPath);
    }

    /**通过json串和jsonPath格式获得具体指集合
     * @param json
     * @param jsonPath
     * @return
     */
    public static List<Object> getStringsByJsonPath(String json,String jsonPath){
        return JsonPath.read(json,jsonPath);
    }

}
