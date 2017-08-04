package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeFlow;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.WebUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tcqq on 2017/7/24.
 * 排名流量 POST请求
 */
@Component
public class ElemeFlowCrawler extends ElemeCrawler{
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;

    private static final String URL = "https://app-api.shop.ele.me/stats/invoke/?method=trafficStats.getTrafficStatsV2";

    @Override
    public void doRun() {
        List<LinkedHashMap<String, Object>> flowList = getFlowText(getClient());
        List<ElemeFlow> elemeFlowBeans = getElemeFlowBeans(flowList);
        for(ElemeFlow elemeFlow : elemeFlowBeans){
            elemeDao.insertFlow(elemeFlow);
        }
    }

    /**
     * 通过爬虫获得所有的对应日期的流量统计
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getFlowText(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String date = DateUtils.date2String(crawlerDate);
        String json = "{\"id\":\"bce6735e-27dd-441b-982c-19b6422327b3\",\"method\":\"getTrafficStatsV2\",\"service\":\"trafficStats\",\"params\":{\"shopId\":"+SHOPID+",\"beginDate\":\""+date+"\",\"endDate\":\""+date+"\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\"ZGI4MGVlNDAtYTgyZC00OTM1LTg1NDZjRlOG\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "bce6735e-27dd-441b-982c-19b6422327b3");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result.restaurantTrafficStatsList");
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
     * 获取ElemeFlow实体类
     * @param flowList
     * @return
     */
    public List<ElemeFlow> getElemeFlowBeans(List<LinkedHashMap<String, Object>> flowList){
        List<ElemeFlow> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : flowList){
            ElemeFlow elemeFlow = new ElemeFlow();
            elemeFlow.setFlowId((String)map.getOrDefault("shopName","")+"~"+(String)map.getOrDefault("statsDate",""));
            elemeFlow.setCrawlerDate(notNull((String)map.getOrDefault("statsDate","")));
            elemeFlow.setShopName(notNull((String)map.getOrDefault("shopName","")));
            elemeFlow.setExposureTotalCount((Integer)map.getOrDefault("exposureTotalCount",0));
            elemeFlow.setVisitorNum((Integer)map.getOrDefault("visitorNum",0));
            elemeFlow.setBuyerNum((Integer)map.getOrDefault("buyerNum",0));
            list.add(elemeFlow);
        }
        return list;
    }
}
