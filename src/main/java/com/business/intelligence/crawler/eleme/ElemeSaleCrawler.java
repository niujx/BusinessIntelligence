package com.business.intelligence.crawler.eleme;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeSale;
import com.business.intelligence.util.DateUtils;
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
        List<LinkedHashMap<String, Object>> saleList= getSaleText(login());
        getElemeSaleBeans(saleList);
    }

    /**
     * 通过爬虫获得所有的对应日期的营业统计
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getSaleText(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String date = DateUtils.date2String(crawlerDate);
        String json = "{\"id\":\"055c6d0f-dc56-4188-89c1-10c67963df8a\",\"method\":\"getHistoryBusinessStatisticsV3\",\"service\":\"saleStatsNew\",\"params\":{\"shopId\":150148671,\"startDate\":\""+date+"\",\"endDate\":\""+date+"\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\"ZGI4MGVlNDAtYTgyZC00OTM1LTg1NDZjRlOG\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "055c6d0f-dc56-4188-89c1-10c67963df8a");
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
     * 获取ElemeSale实体类
     * @param saleList
     * @return
     */
    public List<ElemeSale> getElemeSaleBeans(List<LinkedHashMap<String, Object>> saleList){
        List<ElemeSale> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : saleList){
            ElemeSale elemeSale = new ElemeSale();
            elemeSale.setDate((String)map.get("date"));
            elemeSale.setShop((String)map.get("shop"));
            elemeSale.setTotalOrderAmount((Double) map.get("totalOrderAmount)"));
            elemeSale.setFoodAmount((Double) map.get("foodAmount"));
            elemeSale.setBoxAmount((Double)map.get("boxAmount"));
            elemeSale.setDeliverAmount((Double)map.get("deliverAmount"));
            elemeSale.setOnlinePaymentAmount((Double)map.get("onlinePaymentAmount"));
            elemeSale.setOfflinePaymentAmount((Double)map.get("offlinePaymentAmount"));
            elemeSale.setRestaurantDiscount((Double)map.get("restaurantDiscount"));
            elemeSale.setElemeDiscount((Double)map.get("elemeDiscount"));
            elemeSale.setValidOrderCount((Double)map.get("validOrderCount"));
            elemeSale.setAveragePrice((Double)map.get("averagePrice"));
            elemeSale.setInvalidOrderCount((Double)map.get("invalidOrderCount"));
            elemeSale.setLossSaleAmount((Double)map.get("lossSaleAmount"));
            list.add(elemeSale);
        }
        return list;
    }
}
