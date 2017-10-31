package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.ElemeModel.ElemeSale;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
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
 * Created by Tcqq on 2017/7/19.
 * 营业统计 POST请求
 */
@Slf4j
@Component
public class ElemeSaleCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    private Date endCrawlerDate = crawlerDate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;

    private static final String URL = "https://app-api.shop.ele.me/stats/invoke/?method=saleStatsNew.getHistoryBusinessStatisticsV3";
    public void doRun(ElemeBean elemeBean,String startTime,String endTime) {
        //更新爬取状态为进行中
        int i = crawlerStatusDao.updateStatusING(CrawlerName.ELM_CRAWLER_SALE);
        if(i ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
        //转换前台传入时间
        Date start = DateUtils.string2Date(startTime);
        Date end = DateUtils.string2Date(endTime);
        if(start != null && end != null ){
            this.crawlerDate =start;
            this.endCrawlerDate = end;
        }
        //开始爬取
        CloseableHttpClient getClient = getClient(elemeBean);
        if(getClient != null){
            log.info("开始爬取饿了么经营统计，日期： {} 至 {} ，URL： {} ，用户名： {}", DateUtils.date2String(crawlerDate), DateUtils.date2String(endCrawlerDate),URL,username);
            crawlerLogger.log("开始爬取饿了么用户名为"+username+"的经营统计");
            List<LinkedHashMap<String, Object>> saleList= getSaleText(getClient);
            List<ElemeSale> elemeSaleBeans = getElemeSaleBeans(saleList);
            for(ElemeSale elemeSale : elemeSaleBeans){
                elemeDao.insertSale(elemeSale);
            }
            log.info("用户名为 {} 的经营统计已入库完毕",username);
            crawlerLogger.log("完成爬取饿了么用户名为"+username+"的经营统计");
        }
        //更新爬取状态为已完成
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.ELM_CRAWLER_SALE);
        if(f ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
    }

    /**
     * 通过爬虫获得所有的对应日期的营业统计
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getSaleText(CloseableHttpClient client){
        log.info("ksid id {}",ksId);
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String date = DateUtils.date2String(crawlerDate);
        String endDate = DateUtils.date2String(endCrawlerDate);
        String json = "{\"id\":\"055c6d0f-dc56-4188-89c1-10c67963df8a\",\"method\":\"getHistoryBusinessStatisticsV3\",\"service\":\"saleStatsNew\",\"params\":{\"shopId\":"+shopId+",\"startDate\":\""+date+"\",\"endDate\":\""+endDate+"\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "055c6d0f-dc56-4188-89c1-10c67963df8a");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("result is {}",result);
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result.restaurantSaleDetailV3List");
            return mapsByJsonPath;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (execute != null){
                    execute.close();
                }
//                if(client != null){
//                    client.close();
//                }
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
            elemeSale.setSaleId(map.getOrDefault("restaurantId","")+"~"+map.getOrDefault("orderDate",""));
            elemeSale.setOrderDate((String)map.getOrDefault("orderDate",""));
            elemeSale.setShop((Integer)map.getOrDefault("restaurantId",0));
            elemeSale.setTotalOrderAmount((Double) map.getOrDefault("totalOrderAmount", 0));
            elemeSale.setFoodAmount((Double) map.getOrDefault("foodAmount",0));
            elemeSale.setBoxAmount((Double)map.getOrDefault("boxAmount",0));
            elemeSale.setDeliverAmount((Double)map.getOrDefault("deliverAmount",0));
            elemeSale.setOnlinePaymentAmount((Double)map.getOrDefault("onlinePaymentAmount",0));
            elemeSale.setOfflinePaymentAmount((Double)map.getOrDefault("offlinePaymentAmount",0));
            elemeSale.setRestaurantDiscount((Double)map.getOrDefault("restaurantDiscount",0));
            elemeSale.setElemeDiscount((Double)map.getOrDefault("elemeDiscount",0));
            elemeSale.setValidOrderCount((Integer)map.getOrDefault("validOrderCount",0));
            elemeSale.setAveragePrice((Double)map.getOrDefault("averagePrice",0));
            elemeSale.setInvalidOrderCount((Integer)map.getOrDefault("invalidOrderCount",0));
            elemeSale.setLossSaleAmount((Double)map.getOrDefault("lossSaleAmount",0));
            if(merchantId != null){
                elemeSale.setMerchantId(merchantId);
            }
            list.add(elemeSale);
        }
        return list;
    }

    @Override
    public void doRun() {

    }
}
