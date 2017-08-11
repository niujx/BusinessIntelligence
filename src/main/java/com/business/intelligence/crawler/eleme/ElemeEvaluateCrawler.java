package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.ElemeModel.ElemeBill;
import com.business.intelligence.model.ElemeModel.ElemeEvaluate;
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
 * Created by Tcqq on 2017/7/17.
 * 顾客评价 POST请求
 */
@Slf4j
@Component
public class ElemeEvaluateCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    private Date endCrawlerDate = org.apache.commons.lang3.time.DateUtils.addDays(crawlerDate,1);
    @Autowired
    private ElemeDao elemeDao;

    private static final String URL = "https://app-api.shop.ele.me/ugc/invoke?method=shopRating.querySingleShopRating";

    public void doRun(ElemeBean elemeBean,String startTime,String endTime) {
        Date start = DateUtils.string2Date(startTime);
        Date end = DateUtils.string2Date(endTime);
        if(start != null && end != null ){
            this.crawlerDate =start;
            this.endCrawlerDate = org.apache.commons.lang3.time.DateUtils.addDays(end,1);
        }
        log.info("开始爬取饿了么顾客评价，日期： {} 到 {} ， 最后一天不算，URL： {} ，用户名： {}",DateUtils.date2String(crawlerDate),DateUtils.date2String(endCrawlerDate),URL,username);
        String evaluateText = getEvaluateText(getClient(elemeBean));
        List<LinkedHashMap<String, Object>> orderList = getOrderList(evaluateText);
        List<LinkedHashMap<String, Object>> foodList = getFoodList(evaluateText);
        List<ElemeEvaluate> elemeEvaluateBeans = getElemeEvaluateBeans(orderList, foodList);
        for (ElemeEvaluate elemeEvaluate : elemeEvaluateBeans){
            elemeDao.insertEvaluate(elemeEvaluate);
        }
        log.info("用户名为 {} 的顾客评价已入库完毕",username);

    }

    /**
     * 通过爬虫获得所有的对应日期的顾客评论
     * @param client
     * @return
     */
    public String getEvaluateText(CloseableHttpClient client){
        log.info("ksid id {}",ksId);
        //获得爬取个数
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String beginDate = DateUtils.date2String(crawlerDate);
        String endDate = DateUtils.date2String(endCrawlerDate);
        String json = "{\"id\":\"4b6e096e-0d39-49a6-adb7-c6fe3b6583b4\",\"method\":\"querySingleShopRating\",\"service\":\"shopRating\",\"params\":{\"shopId\":"+shopId+",\"query\":{\"beginDate\":\""+beginDate+"T00:00:00\",\"endDate\":\""+endDate+"T00:00:00\",\"hasContent\":null,\"level\":null,\"replied\":null,\"tag\":null,\"limit\":20,\"offset\":0,\"state\":null,\"deadline\":{\"name\":\"昨日\",\"count\":1,\"value\":\"-1\",\"$$hashKey\":\"object:2467\"}}},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        log.info("count request json is {}",json);
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "4b6e096e-0d39-49a6-adb7-c6fe3b6583b4");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            Object count = WebUtils.getOneByJsonPath(result, "$.result.total");
            log.info("count result {}",count);
            //开始爬取内容
            json = "{\"id\":\"4b6e096e-0d39-49a6-adb7-c6fe3b6583b4\",\"method\":\"querySingleShopRating\",\"service\":\"shopRating\",\"params\":{\"shopId\":"+shopId+",\"query\":{\"beginDate\":\""+beginDate+"T00:00:00\",\"endDate\":\""+endDate+"T00:00:00\",\"hasContent\":null,\"level\":null,\"replied\":null,\"tag\":null,\"limit\":"+(Integer)count+",\"offset\":0,\"state\":null,\"deadline\":{\"name\":\"昨日\",\"count\":1,\"value\":\"-1\",\"$$hashKey\":\"object:2467\"}}},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
            jsonEntity = new StringEntity(json, "UTF-8");
            post.setEntity(jsonEntity);
            execute = client.execute(post);
            entity = execute.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            log.info("result is {}",result);
            return result;
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
     * 通过爬取的json获得订单的评论详情
     * @param json
     * @return
     */
    public List<LinkedHashMap<String, Object>> getOrderList(String json){
        List<LinkedHashMap<String, Object>> foodList = WebUtils.getMapsByJsonPath(json, "$.result.orderRatingList[*].orderCommentList[*]");
        return foodList;
    }

    /**
     * 通过爬取的json获得具体商品的评论详情
     * @param json
     * @return
     */
    public List<LinkedHashMap<String, Object>> getFoodList(String json){
        List<LinkedHashMap<String, Object>> foodList = WebUtils.getMapsByJsonPath(json, "$.result.orderRatingList[*].foodCommentList[*]");
        return foodList;
    }

    /**
     * 获取ElemeEvaluate实体类
     * @param orderList
     * @param foodList
     * @return
     */
    public List<ElemeEvaluate> getElemeEvaluateBeans(List<LinkedHashMap<String, Object>> orderList , List<LinkedHashMap<String, Object>> foodList){
        List<ElemeEvaluate> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : orderList){
            ElemeEvaluate elemeEvaluate = new ElemeEvaluate();
            elemeEvaluate.setId(map.getOrDefault("ratingId",null) == null ? null : (Long)map.getOrDefault("ratingId",null));
            elemeEvaluate.setShopId(Long.valueOf(shopId));
            elemeEvaluate.setCrawlerDate(DateUtils.date2String(crawlerDate));
            elemeEvaluate.setEvaValue(notNull((String)map.getOrDefault("ratingContent","无评论")));
            elemeEvaluate.setQuality(String.valueOf(map.getOrDefault("ratingStar","无")));
            elemeEvaluate.setGoods("本条为订单评论");
            list.add(elemeEvaluate);
        }
        for(LinkedHashMap<String,Object> map : foodList){
            ElemeEvaluate elemeEvaluate = new ElemeEvaluate();
            elemeEvaluate.setId((Long)map.getOrDefault("ratingId",null));
            elemeEvaluate.setShopId(Long.valueOf(shopId));
            elemeEvaluate.setCrawlerDate(DateUtils.date2String(crawlerDate));
            elemeEvaluate.setEvaValue(notNull((String)map.getOrDefault("foodRatingContent","无评论")));
            elemeEvaluate.setQuality(notNull((String)map.getOrDefault("quality","无")));
            elemeEvaluate.setGoods(notNull((String)map.getOrDefault("foodName","无")));
            list.add(elemeEvaluate);
        }
        return list;
    }


    @Override
    public void doRun() {

    }
}
