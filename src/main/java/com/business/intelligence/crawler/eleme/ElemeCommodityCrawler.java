package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.ElemeModel.ElemeCommodity;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
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
 * 商品分析 POST请求
 */
@Slf4j
@Component
public class ElemeCommodityCrawler extends ElemeCrawler{
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    private Date beginCrawlerDate  = org.apache.commons.lang3.time.DateUtils.addMonths(crawlerDate,-1);
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;

    private static final String URL ="https://app-api.shop.ele.me/stats/invoke/?method=foodSalesStats.getFoodSalesStatsV2";

    public void doRun(ElemeBean elemeBean) {
        log.info("开始爬取饿了么商品分析，日期： {}至{} ，URL： {} ，用户名： {}",DateUtils.date2String(beginCrawlerDate), DateUtils.date2String(crawlerDate),URL,username);
        List<LinkedHashMap<String, Object>> commodityText = getCommodityText(getClient(elemeBean));
        List<ElemeCommodity> elemeCommodityBeans = getElemeCommodityBeans(commodityText);
        for(ElemeCommodity elemeCommodity : elemeCommodityBeans){
            elemeDao.insertCommodity(elemeCommodity);
        }
        log.info("用户名为 {} 的商品分析已入库完毕",username);
    }


    /**
     * 通过爬虫获得所有的对应日期的商品分析
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getCommodityText(CloseableHttpClient client){
        log.info("ksid id {}",ksId);
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String endDate = DateUtils.date2String(crawlerDate);
        String beginDate = DateUtils.date2String(beginCrawlerDate);
        String json = "{\"id\":\"35d39394-86eb-4951-9ef5-493b8d265f64\",\"method\":\"getFoodSalesStatsV2\",\"service\":\"foodSalesStats\",\"params\":{\"shopId\":"+shopId+",\"foodSalesQuery\":{\"asc\":false,\"beginDate\":\""+beginDate+"\",\"endDate\":\""+endDate+"\",\"limit\":20,\"orderBy\":\"SALES_AMOUNT\",\"page\":1}},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        log.info("request json is {}",json);
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "35d39394-86eb-4951-9ef5-493b8d265f64");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("count result is {}",result);
            Object count = WebUtils.getOneByJsonPath(result, "$.result.totalRecord");
            json = "{\"id\":\"35d39394-86eb-4951-9ef5-493b8d265f64\",\"method\":\"getFoodSalesStatsV2\",\"service\":\"foodSalesStats\",\"params\":{\"shopId\":"+shopId+",\"foodSalesQuery\":{\"asc\":false,\"beginDate\":\""+beginDate+"\",\"endDate\":\""+endDate+"\",\"limit\":"+(Integer)count+",\"orderBy\":\"SALES_AMOUNT\",\"page\":1}},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
            jsonEntity = new StringEntity(json, "UTF-8");
            post.setEntity(jsonEntity);
            execute = client.execute(post);
            entity = execute.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            log.info("result is {}",result);
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result.foodSalesDetails");
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
     * @param commodityList
     * @return
     */
    public List<ElemeCommodity> getElemeCommodityBeans(List<LinkedHashMap<String, Object>> commodityList){
        List<ElemeCommodity> list = new ArrayList<>();
        //销售额
        int totalSalesAmount = 0;
        //销量
        int totalSalesVolume = 0;
        //排名
        int index = 0;
        //计算销售额和销量的总量
        for(LinkedHashMap<String,Object> map : commodityList){
            totalSalesAmount = totalSalesAmount +=(Double)map.getOrDefault("salesAmount",0);
            totalSalesVolume = totalSalesVolume +=(Integer)map.getOrDefault("salesVolume",0);
        }
        for(LinkedHashMap<String,Object> map : commodityList){
            index++;
            ElemeCommodity elemeCommodity = new ElemeCommodity();
            elemeCommodity.setPri(shopId+"~"+DateUtils.date2String(beginCrawlerDate)+" ~~ " + DateUtils.date2String(crawlerDate)+" ("+index+")");
            elemeCommodity.setMessageDate(DateUtils.date2String(beginCrawlerDate)+" ~~ " + DateUtils.date2String(crawlerDate)+" ("+index+")");
            elemeCommodity.setShopId(Long.valueOf(shopId));
            elemeCommodity.setFoodName(notNull((String)map.getOrDefault("foodName","")));
            elemeCommodity.setSalesAmount((Double)map.getOrDefault("salesAmount",0));
            String amountRate = String .valueOf((Double)map.getOrDefault("salesAmount",0)/totalSalesAmount*100)+"00";
            elemeCommodity.setSalesAmountRate(amountRate.substring(0,amountRate.indexOf(".")+3)+"%");
            elemeCommodity.setSalesVolume((Integer)map.getOrDefault("salesVolume",0));
            String volumeRate = String.valueOf((Integer)map.getOrDefault("salesVolume",0)*1.0/totalSalesVolume*100)+"00";
            elemeCommodity.setSalesVolumeRate(volumeRate.substring(0,volumeRate.indexOf(".")+3)+"%");
            list.add(elemeCommodity);
        }
        return list;
    }


    @Override
    public void doRun() {

    }
}
