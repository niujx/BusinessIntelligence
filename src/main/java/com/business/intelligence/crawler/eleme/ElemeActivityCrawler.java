package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.WebUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by Tcqq on 2017/7/26.
 */
@Component
public class ElemeActivityCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    private HttpClient httpClient = super.httpClient;

    private static final String URL ="https://app-api.shop.ele.me/marketing/invoke/?method=applyActivityManage.getApplyActivity";
    private static final String SOMEURL = "https://melody.shop.ele.me/app/shop/150148671/activity/plantform";


    @Override
    public void doRun() {

    }


    public static void main(String[] args) {
        ElemeActivityCrawler elemeActivityCrawler = new ElemeActivityCrawler();
        elemeActivityCrawler.getSome(elemeActivityCrawler.login());
    }

    
    /**
     * 通过爬虫获得所有活动详情
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getActivityText(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        Map<String,String> params = new HashMap<>();
        params.put("beginDate",String.valueOf(crawlerDate.getTime()));
        params.put("endDate",String.valueOf(crawlerDate.getTime()));
        params.put("limit","10");
        params.put("loginRestaurantId","150148671");
        params.put("offset","0");
        params.put("restaurantId","150148671");
        params.put("status","3");
        params.put("token","30a23e32be094eebfa1e93ddc59eed83");
        String url2 = URL+ HttpClientUtil.buildParamString(params);
        HttpGet get = new HttpGet(url2);
        try {
            execute = client.execute(get);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.bills");
            return mapsByJsonPath;
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
        return null;
    }

    /**
     * 抓取活动详情的个别内容
     */
    public void getSome(CloseableHttpClient client){
        Element element = WebUtils.getElement(SOMEURL);
        System.out.println(element.toString());
    }



}
