package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.ElemeModel.*;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.WebUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Tcqq on 2017/7/25.
 * 账单记录 GET请求
 */
@Slf4j
@Component
public class ElemeBillCrawler extends ElemeCrawler{
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    private Date endCrawlerDate = crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;


    private static final String COUNTURL = "https://app-api.shop.ele.me/nevermore/invoke/?method=OrderService.countOrder";
    private static final String URL = "https://app-api.shop.ele.me/nevermore/invoke/?method=OrderService.queryOrder";
    private static final String ALIURL="https://app-api.shop.ele.me/alchemy/invoke/?method=AlipayPullNewStatsService.getStats";





    //通过post请求获得token的url
//    private static final String GETURL = "https://app-api.shop.ele.me/arena/invoke/?method=TokenService.generateToken";
    //通过token进行get请求的url
//    private static final String URL ="https://httpizza.ele.me/hydros/bill/list";
//    private static final String URL ="https://mdc-httpizza.ele.me/walletMerchantV2/account/queryTransLog";
//    //通过token获得sid
//    private static final String KISDURL = "https://httpizza.ele.me/fe.open_auth/getSid";
//
//    private static final String GETSOMEURL = "https://open-api.shop.ele.me/authorize";


    public void doRun(ElemeBean elemeBean,String startTime,String endTime) {
        //更新爬取状态为进行中
        int i = crawlerStatusDao.updateStatusING(CrawlerName.ELM_CRAWLER_BILL);
        if(i ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
        //装换前台输入的时间
        Date start = DateUtils.string2Date(startTime);
        Date end = DateUtils.string2Date(endTime);
        if(start != null && end != null ){
            this.crawlerDate =start;
            this.endCrawlerDate = end;
        }
        //开始爬取
        CloseableHttpClient client = getClient(elemeBean);
        if(client != null){
            log.info("开始爬取饿了么账单记录，日期： {} 到 {}，URL： {} ，用户名： {}", DateUtils.date2String(crawlerDate), DateUtils.date2String(endCrawlerDate),URL,elemeBean.getUsername());
            crawlerLogger.log("开始爬取饿了么用户名为"+username+"的账单记录");
            ElemeBillPro elemeBillPro = getBillText(client);
            List<ElemeBill> billList = getElemeBillBeans(elemeBillPro);
            for(ElemeBill elemeBill : billList){
                elemeDao.insertBill(elemeBill);
            }
            log.info("用户名为 {} 的账单记录已入库完毕",username);
            crawlerLogger.log("完成爬取饿了么用户名为"+username+"的账单记录");

        }
        //更新爬取状态为已完成
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.ELM_CRAWLER_BILL);
        if(f ==1){
            log.info("更新爬取状态成功");
        }else{
            log.info("更新爬取状态失败");
        }
    }

    /**
     * 通过爬虫获得所有的对应日期的账单分析
     * @param client
     * @return
     */
    public ElemeBillPro getBillText(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        try {
            List<LinkedHashMap<String, Object>> resultList = Lists.newArrayList();
            log.info("ksid id {}",ksId);
            HttpPost countpost = new HttpPost(COUNTURL);
            StringEntity jsonEntity = null;
            String begin;
            String end;
            Date b = org.apache.commons.lang3.time.DateUtils.addDays(crawlerDate,-1);
            Date e = org.apache.commons.lang3.time.DateUtils.addDays(crawlerDate,-1);
            while(true){
                begin = DateUtils.date2String(b);
                end = DateUtils.date2String(e);
                String json ="{\"id\":\"ea44935a-91db-41af-ba4f-1270055dccda\",\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\",\"key\":\"1.0.0\"},\"ncp\":\"2.0.0\",\"service\":\"OrderService\",\"method\":\"countOrder\",\"params\":{\"shopId\":"+shopId+",\"orderFilter\":\"ORDER_QUERY_ALL\",\"condition\":{\"page\":1,\"beginTime\":\""+begin+"T00:00:00\",\"endTime\":\""+end+"T23:59:59\",\"offset\":0,\"limit\":20,\"bookingOrderType\":null}}}";
                jsonEntity = new StringEntity(json, "UTF-8");
                countpost.setEntity(jsonEntity);
                setElemeHeader(countpost);
                countpost.setHeader("X-Eleme-RequestID","ea44935a-91db-41af-ba4f-1270055dccda");
                execute = client.execute(countpost);
                HttpEntity entity = execute.getEntity();
                String result = EntityUtils.toString(entity, "UTF-8");
                Object count = WebUtils.getOneByJsonPath(result,"$.result");
                log.info("count result is {}",count);
                HttpPost post = new HttpPost(URL);
                json = "{\"id\":\"626ffc6e-9d1b-4d16-9eea-97d8bd0e16df\",\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\",\"key\":\"1.0.0\"},\"ncp\":\"2.0.0\",\"service\":\"OrderService\",\"method\":\"queryOrder\",\"params\":{\"shopId\":"+shopId+",\"orderFilter\":\"ORDER_QUERY_ALL\",\"condition\":{\"page\":1,\"beginTime\":\""+begin+"T00:00:00\",\"endTime\":\""+end+"T23:59:59\",\"offset\":0,\"limit\":"+(Integer)count+",\"bookingOrderType\":null}}}";
                jsonEntity = new StringEntity(json, "UTF-8");
                post.setEntity(jsonEntity);
                setElemeHeader(post);
                post.setHeader("X-Eleme-RequestID", "626ffc6e-9d1b-4d16-9eea-97d8bd0e16df");
                execute = client.execute(post);
                entity = execute.getEntity();
                result = EntityUtils.toString(entity,"UTF-8");
//                log.info("result is {}",result);
                List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.result");
                resultList.addAll(mapsByJsonPath);
                if(org.apache.commons.lang3.time.DateUtils.isSameDay(e,endCrawlerDate)){
                    break;
                }
                b = org.apache.commons.lang3.time.DateUtils.addDays(b,1);
                e = org.apache.commons.lang3.time.DateUtils.addDays(e,1);
            }


            HttpPost aliPost = new HttpPost(ALIURL);
            String aliJson = "{\"id\":\"ab635687-99ac-4584-b604-e373eda728c0\",\"method\":\"getStats\",\"service\":\"AlipayPullNewStatsService\",\"params\":{\"shopId\":"+shopId+",\"dateType\":\"LAST_30_DAYS\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
            jsonEntity = new StringEntity(aliJson, "UTF-8");
            aliPost.setEntity(jsonEntity);
            setElemeHeader(aliPost);
            aliPost.setHeader("X-Eleme-RequestID", "626ffc6e-9d1b-4d16-9eea-97d8bd0e16df");
            execute = client.execute(aliPost);
            HttpEntity aliEntity = execute.getEntity();
            String aliResult = EntityUtils.toString(aliEntity,"UTF-8");
            Map<String,Integer> map = Maps.newHashMap();
            List<LinkedHashMap<String, Object>> aliMapList = WebUtils.getMapsByJsonPath(aliResult, "$.result.details");
            for(LinkedHashMap<String,Object> aliMap : aliMapList){
                String touchDate =(String) aliMap.getOrDefault("touchDate", null);
                Integer orderNum =(Integer) aliMap.getOrDefault("orderNum", null);
                if(touchDate != null && orderNum != null){
                    map.put(touchDate,orderNum);
                }
            }

            ElemeBillPro elemeBillPro = new ElemeBillPro();
            elemeBillPro.setMap(map);
            elemeBillPro.setResultList(resultList);
            return elemeBillPro;
        } catch (Exception e) {
            log.error("饿了么服务器异常、请稍后重试：用户名： {}",username);
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








//        log.info("ksid is {}",ksId);
//        CloseableHttpResponse execute = null;
//        CloseableHttpResponse sidExecute = null;
//        try {
//            //获得token
//            HttpPost post = new HttpPost(GETURL);
//            StringEntity jsonEntity = null;
//            String json = "{\"id\":\"7b898df4-ef06-40fc-a930-fcb839c63422\",\"method\":\"generateToken\",\"service\":\"TokenService\",\"params\":{\"shopId\":"+shopId+",\"appId\":\"base.rumble\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
//            jsonEntity = new StringEntity(json, "UTF-8");
//            post.setEntity(jsonEntity);
//            setElemeHeader(post);
//            post.setHeader("X-Eleme-RequestID", "7b898df4-ef06-40fc-a930-fcb839c63422");
//            execute = client.execute(post);
//            HttpEntity entity = execute.getEntity();
//            String result = EntityUtils.toString(entity, "UTF-8");
//            log.info("token result is {}",result);
//            String token = (String)WebUtils.getOneByJsonPath(result, "$.result");
//            log.info("{} 爬取账单的 token 是 {}",username,token);

//            HttpPost somePost = new HttpPost(GETURL);
//            StringEntity someJsonEntity = null;
//            String someJson = "response_type=code&client_id=M8EmQRYVIG&ksid="+token+"&scope=all&state=naposoauthmiddle&redirect_uri=https%3A%2F%2Fnapos-oauth-middleware.faas.ele.me%2Fv2%2F%3Fredirect_uri%3Dhttps%253A%252F%252Fhydrosweb.faas.ele.me%252Fbill%26env%3Dprod&action=SUBMIT&appStatus=ONLINE&loginUri=https%3A%2F%2Fapp-api.shop.ele.me%2Farena%2Finvoke%2F";
//            someJsonEntity = new StringEntity(someJson,"UTF-8");
//            somePost.setEntity(someJsonEntity);
//            execute = client.execute(somePost);
//            HttpEntity someEntity = execute.getEntity();
//            String someResult = EntityUtils.toString(someEntity, "UTF-8");


//
//            //获得sid
//            HttpPost sidPost = new HttpPost(KISDURL);
//            StringEntity sidJsonEntity = null;
//            String sidJson = "{\"code\":\""+token+"\",\"id\":"+shopId+"}";
//            sidJsonEntity = new StringEntity(sidJson, "UTF-8");
//            sidPost.setEntity(sidJsonEntity);
//            setElemeHeader(sidPost);
//            sidPost.setHeader("Referer","https://napos-oauth-middleware.faas.ele.me/v2/?redirect_uri=https%3A%2F%2Fhydrosweb.faas.ele.me%2Fbill&env=prod&code="+token+"&state=naposoauthmiddle");
//            sidPost.setHeader("Host","httpizza.ele.me");
//            sidExecute = client.execute(sidPost);
//            HttpEntity sidEntity = sidExecute.getEntity();
//            String sidResult = EntityUtils.toString(sidEntity, "UTF-8");
//            log.info("ksid result is {}",sidResult);
//            String sid = (String)WebUtils.getOneByJsonPath(sidResult, "$.value");
//            log.info("{} 爬取账单的 sid 是 {}",username,sid);


//            long beginTime = crawlerDate.getTime();
//            long endTime = endCrawlerDate.getTime();
//            String limit = String.valueOf((endTime-beginTime)/1000/60/60/24+1);
//            //利用得到的token进行get请求
//            Map<String,String> params = new HashMap<>();
//            params.put("beginDate",String.valueOf(beginTime));
//            params.put("endDate",String.valueOf(endTime));
//            params.put("limit",limit);
//            params.put("loginRestaurantId",shopId);
//            params.put("offset","0");
//            params.put("restaurantId",shopId);
//            params.put("status","3");
////            params.put("token",token);
//            String url2 = URL+HttpClientUtil.buildParamString(params);
//            HttpGet get = new HttpGet(url2);
//            client = HttpClientUtil.getHttpClient(cookieStore);
//            execute = client.execute(get);
//            entity = execute.getEntity();
//            result = EntityUtils.toString(entity, "UTF-8");
//            log.info("result json is {}",result);
//            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.bills");
//            return mapsByJsonPath;



//            long beginTime = crawlerDate.getTime();
//            long endTime = endCrawlerDate.getTime();
//            //利用得到的token进行get请求
//            Map<String,String> params = new HashMap<>();
//            params.put("beginDate",String.valueOf(beginTime));
//            params.put("endDate",String.valueOf(endTime));
//            params.put("type","0");
//            params.put("pageIndex","1");
//            params.put("pageSize","400");
//            params.put("merchantId",shopId);
//            params.put("loginRestaurantId",shopId);
//            params.put("token",token);
//            String url2 = URL+HttpClientUtil.buildParamString(params);
//            HttpGet get = new HttpGet(url2);
//            execute = client.execute(get);
//            entity = execute.getEntity();
//            result = EntityUtils.toString(entity, "UTF-8");
//            log.info("result json is {}",result);
//            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.accountWalletTransLog");
//            return mapsByJsonPath;
//        } catch (Exception e) {
//            log.error("io or json error");
//        }finally {
//            try {
//                if (execute != null){
//                    execute.close();
//                }
////                if(client != null){
////                    client.close();
////                }
//            } catch (IOException e) {
//            }
//        }
//        return null;
    }


    /**
     * 获取ElemeBill实体类
     * @param elemeBillPro
     * @return
     */
    public List<ElemeBill> getElemeBillBeans(ElemeBillPro elemeBillPro){
        List<LinkedHashMap<String, Object>> billList = elemeBillPro.getResultList();
        Map<String, Integer> aliMap = elemeBillPro.getMap();
        List<ElemeBill> list = new ArrayList<>();
        Map<String,ElemeBillMessage> countMap = Maps.newHashMap();
        for(LinkedHashMap<String,Object> map : billList){
            if("订单完成".equals(map.get("orderLatestStatus")) || "已部分退款".equals(map.get("orderLatestStatus"))){
//                int refund = 0;
//                Map refundOrderTraceView =(Map) map.get("refundOrderTraceView");
//                if(refundOrderTraceView != null){
//                    refundOrderTraceView.get("timeLines");
//                }
                String activeTime = (String)map.getOrDefault("settledTime", "1");
                Double e1 = (Double) map.getOrDefault("restaurantPart",0);
                Double e2 =(Double) map.getOrDefault("serviceFee", 0);
                Double e3 = getFreight(map);
                Double p1 = (Double)map.getOrDefault("income","0");
                Double i = p1-e1-e2;
                activeTime = activeTime.substring(0,10);
                ElemeBillMessage e = countMap.get(activeTime);
                if(e == null){
                    ElemeBillMessage elemeBillMessage = new ElemeBillMessage();
                    elemeBillMessage.setIncome(i);
                    elemeBillMessage.setExpense(e1+e2-e3);
                    elemeBillMessage.setPayAmount(p1-e3);
                    countMap.put(activeTime,elemeBillMessage);
                }else {
                    e.setIncome(e.getIncome()+i);
                    e.setExpense(e.getExpense()+e1+e2-e3);
                    e.setPayAmount(e.getPayAmount()+p1-e3);
                }
            }
        }
        for(String str : countMap.keySet()){
            Date date = DateUtils.string2Date(str);
            date = org.apache.commons.lang3.time.DateUtils.addDays(date,2);
            String str2 = DateUtils.date2String(date);
            if(aliMap.keySet().contains(str2)){
                ElemeBillMessage elemeBillMessage = countMap.get(str);
                elemeBillMessage.setPayAmount(elemeBillMessage.getPayAmount()-5.0*aliMap.get(str2));
                elemeBillMessage.setExpense(elemeBillMessage.getExpense()-5.0*aliMap.get(str2));
            }
        }


        for(String key : countMap.keySet()){
            long time = DateUtils.string2Date(key).getTime();
            long begin = crawlerDate.getTime();
            long end = endCrawlerDate.getTime();
            if(begin<=time && time<=end){
                ElemeBill elemeBill = new ElemeBill();
                ElemeBillMessage elemeBillMessage = countMap.get(key);
                elemeBill.setPri(key+"~"+String.valueOf(shopId));
                elemeBill.setClosingDate(key);
                elemeBill.setIncome(new DecimalFormat("#.00").format(elemeBillMessage.getIncome()));
                elemeBill.setExpense(new DecimalFormat("#.00").format(elemeBillMessage.getExpense()));
                elemeBill.setDeductAmount("0");
                elemeBill.setDueAmount("0");
                elemeBill.setPayAmount((new DecimalFormat("#.00").format(elemeBillMessage.getPayAmount())));
                String payMentDate = DateUtils.date2String(org.apache.commons.lang3.time.DateUtils.addDays( DateUtils.string2Date(key),3));
                elemeBill.setPaymentDate(payMentDate);
                elemeBill.setShopId(Long.valueOf(shopId));
                if(merchantId != null){
                    elemeBill.setMerchantId(merchantId);
                }
                list.add(elemeBill);
            }
        }
        return list;
    }

    public double getFreight(Map map){
        double a =0.0;
        double b =0.0;
        try{
            LinkedHashMap<String, Object> distTraceView = (LinkedHashMap<String, Object>) map.getOrDefault("distTraceView", null);
            if(distTraceView != null){
                LinkedHashMap<String, Object> traceView = (LinkedHashMap<String, Object>) distTraceView.getOrDefault("traceView",null);
                if(traceView != null){
                    LinkedHashMap<String, Object> statusDesc = (LinkedHashMap<String, Object>)traceView.getOrDefault("statusDesc",null);
                    if(statusDesc != null){
                        String message = (String) statusDesc.getOrDefault("message", null);
                        if(message != null){
                            a = Double.valueOf(message.substring(5,9));
                            b = Double.valueOf(message.substring(14,18));
                        }
                    }
                }
            }

        }catch (Exception e ){

        }
        return a+b;

    }


    @Override
    public void doRun() {

    }
}
