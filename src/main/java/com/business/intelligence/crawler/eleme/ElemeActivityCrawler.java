package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.ElemeModel.ElemeActivity;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.WebUtils;
import com.google.common.collect.Maps;
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
import java.util.*;

/**
 * Created by Tcqq on 2017/7/26.
 * 商店活动 POST请求
 */
@Slf4j
@Component
public class ElemeActivityCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;
    private static final String URL ="https://app-api.shop.ele.me/marketing/invoke/?method=applyActivityManage.getApplyActivity";
    private static final Map<Boolean, String> ISSHARE = Maps.newHashMap();

    static {
        ISSHARE.put(true,"与折扣、特价活动共享");
        ISSHARE.put(false,"不与折扣、特价活动共享");
        ISSHARE.put(null,"未知");
    }

    public void doRun(ElemeBean elemeBean) {
        //更新爬取状态为进行中
        int i = crawlerStatusDao.updateStatusING(CrawlerName.ELM_CRAWLER_ACTIVITY);
        if(i ==0){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
        //开始爬取
        log.info("开始爬取饿了么商店活动，URL： {} ，用户名： {}",URL,username);
        List<LinkedHashMap<String, Object>> activityText = getActivityText(getClient(elemeBean));
        List<ElemeActivity> elemeActivityBeans = getElemeActivityBeans(activityText);
        for(ElemeActivity elemeActivity : elemeActivityBeans){
            elemeDao.insertActivity(elemeActivity);
        }
        log.info("用户名为 {} 的商店活动已入库完毕",username);
        //更新爬取状态为完成
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.ELM_CRAWLER_ACTIVITY);
        if(f ==0){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
    }

    /**
     * 通过爬虫获得所有活动详情
     * @param client
     * @return
     */
    public List<LinkedHashMap<String, Object>> getActivityText(CloseableHttpClient client){
        log.info("ksid is {}",ksId);
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"9368dd8a-a6e9-4e6c-855c-3d2e29ff3498\",\"method\":\"getApplyActivity\",\"service\":\"applyActivityManage\",\"params\":{\"shopId\":"+shopId+"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "9368dd8a-a6e9-4e6c-855c-3d2e29ff3498");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("result is {}",result);
            List<LinkedHashMap<String, Object>> list = WebUtils.getMapsByJsonPath(result, "$.result");
            return list;
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
     * 获取ElemeActivity实体类
     * @param activityList
     * @return
     */
    public List<ElemeActivity> getElemeActivityBeans(List<LinkedHashMap<String, Object>> activityList){
        List<ElemeActivity> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : activityList){
            LinkedHashMap<String, Object> contentMap = (LinkedHashMap)map.get("content");
            ElemeActivity  elemeActivity= new ElemeActivity();
            String crawlerDateString = DateUtils.date2String(crawlerDate);
            elemeActivity.setPri(map.get("id")+"~"+shopId+"~"+crawlerDateString);
            elemeActivity.setCrawlerDate(crawlerDateString);
            elemeActivity.setId((Integer) map.get("id"));
            elemeActivity.setShopId(Long.valueOf(shopId));
            elemeActivity.setBeginDate(notNull((String)map.getOrDefault("beginDate","")));
            elemeActivity.setEndDate(notNull((String)map.getOrDefault("endDate","")));
            elemeActivity.setName(notNull((String)map.getOrDefault("name","")));
            elemeActivity.setStatus(notNull((String)map.getOrDefault("status","未知")));
            elemeActivity.setCreateTime(notNull((String)map.getOrDefault("createdAt","")));
            elemeActivity.setDescription(notNull((String)map.getOrDefault("description","无")));
            elemeActivity.setIsShare(ISSHARE.get((Boolean)contentMap.getOrDefault("shareWithOtherActivities",null)));
            //活动内容
            String content = "";
            switch ((String) map.getOrDefault("iconText","")){
                case "折":
                   content = getZhe((Integer)contentMap.get("minCategory"),(Integer)contentMap.get("maxCategory"),(Double)contentMap.get("originalLeastPrice"),(Double)contentMap.get("originalMostPrice"),(Double)contentMap.get("discount"));
                   break;
                case "减":
                   for(LinkedHashMap<String, Object> items : (List<LinkedHashMap<String, Object>>)contentMap.get("items")){
                       content = content+getJian((Integer)items.get("condition"),(Integer)items.get("discount"),(Double)items.get("subsidy"),(Integer)items.get("onlinePaymentDiscount"),(Double)items.get("onlinePaymentSubsidy"));
                   }
                   break;
                case "赠":
                    for(LinkedHashMap<String, Object> gift: (List<LinkedHashMap<String, Object>>)contentMap.get("giftContents")){
                        content = content+getZeng((Integer)gift.get("condition"),(String)((LinkedHashMap<String, Object>)gift.get("giftActivityBenefit")).get("name"),(Integer) ((LinkedHashMap<String, Object>)gift.get("giftActivityBenefit")).get("quantity"));
                    }
                   break;
                case "惠":
                    content = getHui((Integer) contentMap.get("minCategory"),(Integer) contentMap.get("maxCategory"),(Double) contentMap.get("originalLeastPrice"),(Double) contentMap.get("originalMostPrice"),(Double)contentMap.get("lockPrice"));
                    break;
                default:
                   content = "无";
            }
            elemeActivity.setContent(content);
            list.add(elemeActivity);
        }
        return list;
    }

    /**
     * 当优惠种类为"折"时，拼接折的活动内容
     * @param minCategory
     * @param maxCategory
     * @param originalLeastPrice
     * @param originalMostPrice
     * @param discount
     * @return
     */
    public String getZhe(Integer minCategory,Integer maxCategory,Double originalLeastPrice,Double originalMostPrice,Double discount){
        StringBuilder sb = new StringBuilder();
        sb.append("选择")
                .append(minCategory)
                .append("-")
                .append(maxCategory)
                .append("种原价为")
                .append(originalLeastPrice)
                .append("-")
                .append(originalMostPrice)
                .append("元的商品。统一")
                .append(discount*10)
                .append("折出售，平台按原价补贴1折（每单最多补贴20元）");
        return sb.toString();
    }

    /**
     * 当优惠种类为"减"时，拼接减的活动内容
     * @param condition
     * @param discount
     * @param subsidy
     * @param onlinePaymentDiscount
     * @param onlinePaymentSubsidy
     * @return
     */
    public String getJian(Integer condition,Integer discount,Double subsidy,Integer onlinePaymentDiscount,Double onlinePaymentSubsidy){
        StringBuilder sb = new StringBuilder();
        sb.append("满")
                .append(condition)
                .append("减")
                .append(discount)
                .append("，平台补贴")
                .append(subsidy)
                .append("元。在线支付再减")
                .append(onlinePaymentDiscount)
                .append("元，平台再补贴")
                .append(onlinePaymentSubsidy)
                .append("元");
        return sb.toString();
    }

    /**
     * 当优惠种类为"赠"时，拼接赠的活动内容
     * @param condition
     * @param name
     * @param quantity
     * @return
     */
    public String getZeng(Integer condition,String name,Integer quantity){
        StringBuilder sb = new StringBuilder();
        sb.append("下单满")
                .append(condition)
                .append("元，赠")
                .append(name)
                .append(quantity)
                .append("份");
        return sb.toString();
    }

    /**
     * 当优惠种类为"惠"时，拼接惠的活动内容
     * @param minCategory
     * @param maxCategory
     * @param originalLeastPrice
     * @param originalMostPrice
     * @param lockPrice
     * @return
     */
    public String getHui(Integer minCategory,Integer maxCategory,Double originalLeastPrice,Double originalMostPrice,Double lockPrice){
        StringBuilder sb = new StringBuilder();
        sb.append("选择")
                .append(minCategory)
                .append("-")
                .append(maxCategory)
                .append("种原价为")
                .append(originalLeastPrice)
                .append("-")
                .append(originalMostPrice)
                .append("元的商品。统一售价")
                .append(lockPrice)
                .append("元");
        return sb.toString();
    }


    @Override
    public void doRun() {

    }
}
