package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.ElemeModel.ElemeMessage;
import com.business.intelligence.model.ElemeModel.ElemeOrder;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.WebUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by Tcww on 2017/7/18.
 * 查询全部订单  POST提交
 */
@Slf4j
@Component
public class ElemeOrderCrawler extends ElemeCrawler{
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    private Date endCrawlerDate = crawlerDate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;

    private static final String COUNTURL = "https://app-api.shop.ele.me/nevermore/invoke/?method=OrderService.countOrder";
    private static final String URL = "https://app-api.shop.ele.me/nevermore/invoke/?method=OrderService.queryOrder";

    private static final String REGEX= "\t";

    private static final Map<String, String> STATUS = Maps.newHashMap();
    private static final Map<String, String> REFUNDSTATUS = Maps.newHashMap();
    private static final Map<String, String> INVOICETYPE = Maps.newHashMap();

    static {
        STATUS.put("PENDING","未生效订单");
        STATUS.put("UNPROCESSED","未处理订单");
        STATUS.put("REFUNDING","退单处理中");
        STATUS.put("VALID","已处理的有效订单");
        STATUS.put("INVALID","无效订单");
        STATUS.put("SETTLED","已完成订单");

        REFUNDSTATUS.put("NO_REFUND","未申请退单");
        REFUNDSTATUS.put("APPLIED","用户申请退单");
        REFUNDSTATUS.put("REJECTED","店铺拒绝退单");
        REFUNDSTATUS.put("ARBITRATING","客服仲裁中");
        REFUNDSTATUS.put("FAILED","退单失败");
        REFUNDSTATUS.put("SUCCESSFUL","退单成功");

        INVOICETYPE.put("PERSONAL","个人");
        INVOICETYPE.put("COMPANY","企业");

    }

    public void doRun(ElemeBean elemeBean,String startTime,String endTime) {
        //更新爬取状态为进行中
        int i = crawlerStatusDao.updateStatusING(CrawlerName.ELM_CRAWLER_ORDER);
        if(i ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
        //开始转换前台转入的时间
        Date start = DateUtils.string2Date(startTime);
        Date end = DateUtils.string2Date(endTime);
        if(start != null && end != null ){
            this.crawlerDate =start;
            this.endCrawlerDate = end;
        }
        //开始爬取
        log.info("开始爬取饿了么订单，日期： {} 到 {} ，URL： {} ，用户名： {}", DateUtils.date2String(crawlerDate),DateUtils.date2String(endCrawlerDate),URL,elemeBean.getUsername());
        ElemeMessage orderText = getOrderText(getClient(elemeBean));
        List<ElemeOrder> elemeOrderBeans = getElemeOrderBeans(orderText);
        for(ElemeOrder elemeOrder : elemeOrderBeans){
           elemeDao.insertOrder(elemeOrder);
        }
        log.info("用户名为 {} 的订单已入库完毕",username);
        //更新爬取状态为已完成
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.ELM_CRAWLER_ORDER);
        if(f ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
    }


    /**
     * 通过爬虫获得所有的对应日期的订单信息
     * @param client
     * @return
     */
    public ElemeMessage getOrderText(CloseableHttpClient client){
        log.info("ksid id {}",ksId);
        CloseableHttpResponse execute = null;
        HttpPost countpost = new HttpPost(COUNTURL);
        StringEntity jsonEntity = null;
        String date = DateUtils.date2String(crawlerDate);
        String endDate = DateUtils.date2String(endCrawlerDate);
        String json ="{\"id\":\"ea44935a-91db-41af-ba4f-1270055dccda\",\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\",\"key\":\"1.0.0\"},\"ncp\":\"2.0.0\",\"service\":\"OrderService\",\"method\":\"countOrder\",\"params\":{\"shopId\":"+shopId+",\"orderFilter\":\"ORDER_QUERY_ALL\",\"condition\":{\"page\":1,\"beginTime\":\""+date+"T00:00:00\",\"endTime\":\""+endDate+"T23:59:59\",\"offset\":0,\"limit\":20,\"bookingOrderType\":null}}}";
        jsonEntity = new StringEntity(json, "UTF-8");
        countpost.setEntity(jsonEntity);
        setElemeHeader(countpost);
        countpost.setHeader("X-Eleme-RequestID","ea44935a-91db-41af-ba4f-1270055dccda");
        try {
            execute = client.execute(countpost);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            Object count = WebUtils.getOneByJsonPath(result,"$.result");
            log.info("count result is {}",count);
            HttpPost post = new HttpPost(URL);
            json = "{\"id\":\"626ffc6e-9d1b-4d16-9eea-97d8bd0e16df\",\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\",\"key\":\"1.0.0\"},\"ncp\":\"2.0.0\",\"service\":\"OrderService\",\"method\":\"queryOrder\",\"params\":{\"shopId\":"+shopId+",\"orderFilter\":\"ORDER_QUERY_ALL\",\"condition\":{\"page\":1,\"beginTime\":\""+date+"T00:00:00\",\"endTime\":\""+endDate+"T23:59:59\",\"offset\":0,\"limit\":"+(Integer)count+",\"bookingOrderType\":null}}}";
            jsonEntity = new StringEntity(json, "UTF-8");
            post.setEntity(jsonEntity);
            setElemeHeader(post);
            post.setHeader("X-Eleme-RequestID", "626ffc6e-9d1b-4d16-9eea-97d8bd0e16df");
            execute = client.execute(post);
            entity = execute.getEntity();
            result = EntityUtils.toString(entity,"UTF-8");
            log.info("result is {}",result);
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result");
            ElemeMessage elemeMessage = new ElemeMessage();
            elemeMessage.setList(mapsByJsonPath);
            elemeMessage.setJson(result);
            return elemeMessage;
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
     * 获取ElemeOrder实体类
     * @param elemeMessage
     * @return
     */
    public List<ElemeOrder> getElemeOrderBeans(ElemeMessage elemeMessage){
        List<ElemeOrder> list = new ArrayList<>();
        String json = elemeMessage.getJson();
        List<LinkedHashMap<String, Object>> elemeList = elemeMessage.getList();
        for(int i =0;i<elemeList.size();i++){
            LinkedHashMap<String, Object> map = elemeList.get(i);
            ElemeOrder elemeOrder = new ElemeOrder();
            elemeOrder.setOrderId(notNull((String)map.get("id")));
            elemeOrder.setShopId((Integer)map.getOrDefault("shopId","0"));
            elemeOrder.setAddress(notNull((String)map.getOrDefault("consigneeAddress","未知")));
            elemeOrder.setCreatedAt(notNull((String)map.getOrDefault("activeTime","未知")));
            elemeOrder.setActiveAt(notNull((String)map.getOrDefault("activeTime","未知")));
            elemeOrder.setDeliverFee((Double)map.getOrDefault("deliveryFee",0));
            elemeOrder.setDeliverTime(notNull((String)map.getOrDefault("bookedTime","无")));
            elemeOrder.setDescription(notNull((String)map.getOrDefault("remark","未知")));
            List<String> groups = getJsonText(json,"groups","paymentStatus");
            for(int groupCount =0;groupCount<groups.size();groupCount++){
                if(groupCount == i)
                    elemeOrder.setGroups(groups.get(groupCount));
            }
            elemeOrder.setInvoice(notNull((String)map.getOrDefault("invoiceTitle","无")));
            elemeOrder.setBook((map.get("bookedTime") != null) ? "预定" : "非预定");
            elemeOrder.setOnlinePaid(("ONLINE".equals((String)map.getOrDefault("payment",""))) ? "在线支付" : "非在线支付");
            Object phones = map.getOrDefault("consigneePhones", new ArrayList<>());
            elemeOrder.setPhoneList(getTextByList((List<String>)phones));
            elemeOrder.setOpenId(notNull((String)map.getOrDefault("openId","无")));
            elemeOrder.setShopName(notNull((String)map.getOrDefault("shopName","未知")));
            elemeOrder.setDaySn((Integer)map.getOrDefault("daySn",0));
            elemeOrder.setStatus(STATUS.getOrDefault((String)map.getOrDefault("status",""),"未知"));
            elemeOrder.setRefundStatus(REFUNDSTATUS.getOrDefault((String)map.getOrDefault("refundStatus",""),"未知"));
            elemeOrder.setUserId((Integer)map.getOrDefault("userId",0));
            elemeOrder.setTotalPrice((Double)map.getOrDefault("payAmount","0"));
            elemeOrder.setOriginalPrice((Double)map.getOrDefault("goodsTotal",0));
            elemeOrder.setConsignee(notNull((String)map.getOrDefault("consigneeName","未知")));
            elemeOrder.setDeliveryGeo(notNull((String)map.getOrDefault("deliveryGeo","无")));
            elemeOrder.setDeliveryPoiAddress(notNull((String)map.getOrDefault("consigneeAddress","未知")));
            elemeOrder.setInvoiced((map.get("invoiceTitle") != null) ? "需要发票" : "不需要发票");
            elemeOrder.setIncome((Double)map.getOrDefault("income",0));
            elemeOrder.setServiceRate((Double)map.getOrDefault("serviceRate",0));
            elemeOrder.setServiceFee((Double)map.getOrDefault("serviceFee",0));
            elemeOrder.setHongbao((Double)map.getOrDefault("hongbao",0));
            elemeOrder.setPackageFee((Double)map.getOrDefault("packageFee",0));
            elemeOrder.setActivityTotal((Double)map.getOrDefault("activityTotal",0));
            elemeOrder.setShopPart((Double)map.getOrDefault("merchantActivityPart",0));
            elemeOrder.setElemePart((Double)map.getOrDefault("elemeActivityPart",0));
            elemeOrder.setDowngraded((Boolean)map.getOrDefault("downgraded",false) ? "降级" : "非降级");
            elemeOrder.setSecretPhoneExpireTime(notNull((String)map.getOrDefault("secretPhoneExpireTime","未知")));
            List<String> activities = getJsonText(json,"activities","merchantActivities");
            for(int activityCount =0;activityCount<activities.size();activityCount++){
                if(activityCount == i)
                    elemeOrder.setOrderActivities(activities.get(activityCount));
            }
            elemeOrder.setInvoiceType(INVOICETYPE.getOrDefault(map.getOrDefault("invoiceType",""),"无"));
            elemeOrder.setTaxpayerId(notNull((String)map.getOrDefault("taxpayerId","无")));
            list.add(elemeOrder);
        }
        return list;
    }

    /**
     * 提取json数据
     * @param json
     * @param key
     * @param endKey
     * @return
     */
    public static List<String> getJsonText(String json,String key,String endKey){
        List<String> list = new ArrayList<>();
        if(json != null) {
            String[] splitByKey = json.split(key + "\":");
            for (int i = 1; i < splitByKey.length; i++) {
                String splitByEnd = splitByKey[i].split(",\"" + endKey)[0];
                list.add(splitByEnd);
            }
        }
        return list;
    }

    /**
     * 将List拼接字符串
     * @param list
     * @return
     */
    public static String getTextByList(List<String> list){
        StringBuilder sb = new StringBuilder();
        for(String str : list){
            sb.append(str);
            sb.append(REGEX);
        }
        return  sb.toString().trim();
    }

    @Override
    public void doRun() {

    }
}
