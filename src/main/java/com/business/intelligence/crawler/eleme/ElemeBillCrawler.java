package com.business.intelligence.crawler.eleme;

import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.ElemeDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.model.ElemeModel.ElemeBill;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    private Date endCrawlerDate = org.apache.commons.lang3.time.DateUtils.addDays(crawlerDate,1);
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;

    //通过post请求获得token的url
    private static final String GETURL = "https://app-api.shop.ele.me/arena/invoke/?method=TokenService.generateToken";
    //通过token进行get请求的url
//    private static final String URL ="https://httpizza.ele.me/hydros/bill/list";
    private static final String URL ="https://mdc-httpizza.ele.me/walletMerchantV2/account/queryTransLog";
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
            this.endCrawlerDate = org.apache.commons.lang3.time.DateUtils.addDays(end,1);
        }
        //开始爬取
        CloseableHttpClient client = getClient(elemeBean);
        if(client != null){
            log.info("开始爬取饿了么账单记录，日期： {} 到 {}，URL： {} ，用户名： {}", DateUtils.date2String(crawlerDate), DateUtils.date2String(endCrawlerDate),URL,elemeBean.getUsername());
            List<LinkedHashMap<String, Object>> billText = getBillText(client);
            List<ElemeBill> billList = getElemeBillBeans(billText);
            for(ElemeBill elemeBill : billList){
                elemeDao.insertBill(elemeBill);
            }
            log.info("用户名为 {} 的账单记录已入库完毕",username);

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
    public List<LinkedHashMap<String, Object>> getBillText(CloseableHttpClient client){
        log.info("ksid is {}",ksId);
        CloseableHttpResponse execute = null;
        CloseableHttpResponse sidExecute = null;
        try {
            //获得token
            HttpPost post = new HttpPost(GETURL);
            StringEntity jsonEntity = null;
            String json = "{\"id\":\"7b898df4-ef06-40fc-a930-fcb839c63422\",\"method\":\"generateToken\",\"service\":\"TokenService\",\"params\":{\"shopId\":"+shopId+",\"appId\":\"base.rumble\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
            jsonEntity = new StringEntity(json, "UTF-8");
            post.setEntity(jsonEntity);
            setElemeHeader(post);
            post.setHeader("X-Eleme-RequestID", "7b898df4-ef06-40fc-a930-fcb839c63422");
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("token result is {}",result);
            String token = (String)WebUtils.getOneByJsonPath(result, "$.result");
            log.info("{} 爬取账单的 token 是 {}",username,token);

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



            long beginTime = crawlerDate.getTime();
            long endTime = endCrawlerDate.getTime();
            //利用得到的token进行get请求
            Map<String,String> params = new HashMap<>();
            params.put("beginDate",String.valueOf(beginTime));
            params.put("endDate",String.valueOf(endTime));
            params.put("type","0");
            params.put("pageIndex","1");
            params.put("pageSize","400");
            params.put("merchantId",shopId);
            params.put("loginRestaurantId",shopId);
            params.put("token",token);
            String url2 = URL+HttpClientUtil.buildParamString(params);
            HttpGet get = new HttpGet(url2);
            execute = client.execute(get);
            entity = execute.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            log.info("result json is {}",result);
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.accountWalletTransLog");
            return mapsByJsonPath;
        } catch (Exception e) {
            log.error("io or json error");
        }finally {
            try {
                if (execute != null){
                    execute.close();
                }
//                if(client != null){
//                    client.close();
//                }
            } catch (IOException e) {
            }
        }
        return null;
    }


    /**
     * 获取ElemeBill实体类
     * @param billList
     * @return
     */
    public List<ElemeBill> getElemeBillBeans(List<LinkedHashMap<String, Object>> billList){
        List<ElemeBill> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : billList){
            if("收入".equals(map.getOrDefault("displayTransType","支出"))){
                ElemeBill elemeBill = new ElemeBill();
                String closingTime = (String)map.get("displayRemark");
                closingTime = closingTime.replaceAll("账单结算入余额，已成功","");
                elemeBill.setPri(closingTime+"~"+String.valueOf(shopId));
                elemeBill.setClosingDate(closingTime);
                elemeBill.setIncome("0");
                elemeBill.setExpense("0");
                elemeBill.setDeductAmount("0");
                elemeBill.setDueAmount("0");
                elemeBill.setPayAmount(String .valueOf(NumberUtils.toLong((String)map.get("transAmount"))/1000000.0));
                String paymentTime =(String) map.getOrDefault("transLogId","1970010100");
                paymentTime = paymentTime.substring(0,8);
                elemeBill.setPaymentDate(paymentTime);
                elemeBill.setShopId(Long.valueOf(shopId));
                if(merchantId != null){
                    elemeBill.setMerchantId(merchantId);
                }
                list.add(elemeBill);
            }
        }
        return list;
    }


    @Override
    public void doRun() {

    }
}
