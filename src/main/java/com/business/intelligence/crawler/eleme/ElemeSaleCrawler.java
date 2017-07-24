package com.business.intelligence.crawler.eleme;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeSale;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.WebUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Tcqq on 2017/7/19.
 */
@Component
public class ElemeSaleCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    private HttpClient httpClient = super.httpClient;

    private static final String URL = "https://app-api.shop.ele.me/stats/invoke/?method=saleStatsNew.getHistoryBusinessStatisticsV3";
    @Override
    public void doRun() {

    }

    public static void main(String[] args) {
        ElemeSaleCrawler elemeSaleCrawler  =new ElemeSaleCrawler();
        CloseableHttpClient client= elemeSaleCrawler.login();
        elemeSaleCrawler.getSaleText(client);
    }

    /**
     * 通过爬虫获得所有的对应日期的营业统计
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getSaleText(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        String content=null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"055c6d0f-dc56-4188-89c1-10c67963df8a\",\"method\":\"getHistoryBusinessStatisticsV3\",\"service\":\"saleStatsNew\",\"params\":{\"shopId\":150148671,\"startDate\":\"2017-07-20\",\"endDate\":\"2017-07-20\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\"ZGI4MGVlNDAtYTgyZC00OTM1LTg1NDZjRlOG\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        post.setHeader("Content-type", "application/json;charset=utf-8");
        post.setHeader("Host", "app-api.shop.ele.me");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Accept-Encodinge", "gzip, deflate, br");
        post.setHeader("X-Shard", "shopid=150148671");
        post.setHeader("X-Eleme-RequestID", "055c6d0f-dc56-4188-89c1-10c67963df8a");
        post.setHeader("Referer", "https://melody-stats.faas.ele.me/");
        post.setHeader("origin", "https://melody-stats.faas.ele.me");
        post.setHeader("Connection", "keep-alive");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result.restaurantSaleDetailV3List");
            for (LinkedHashMap<String,Object> map:mapsByJsonPath){
                for(String key : map.keySet()){
                    System.out.println("key="+key+"-------value="+map.get(key));
                }
            }
            return mapsByJsonPath;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                execute.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 将获取的数据组成javaBeans
     * @param saleList
     * @return
     */
    public List<ElemeSale> getBeans(List<LinkedHashMap<String, Object>> saleList){
        List<ElemeSale> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : saleList){
            ElemeSale elemeSale = new ElemeSale();
            elemeSale.setDate((String)map.get("date"));
            elemeSale.setShop((String)map.get("shop"));
            elemeSale.setTotalOrderAmount((Long)map.get("totalOrderAmount)"));
            elemeSale.setFoodAmount((Long)map.get("foodAmount"));
            // TODO: 2017/7/21  
        }
        return list;
    }
}
