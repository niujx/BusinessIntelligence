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
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
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
    private Date endCrawlerDate = crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;

    //通过post请求获得token的url
    private static final String GETURL = "https://app-api.shop.ele.me/arena/invoke/?method=TokenService.generateToken";
    //通过token进行get请求的url
    private static final String URL ="https://httpizza.ele.me/hydros/bill/list";


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
        //获得token
        HttpPost post = new HttpPost(GETURL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"7b898df4-ef06-40fc-a930-fcb839c63422\",\"method\":\"generateToken\",\"service\":\"TokenService\",\"params\":{\"shopId\":"+shopId+",\"appId\":\"base.rumble\"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "7b898df4-ef06-40fc-a930-fcb839c63422");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("token result is {}",result);
            String token = (String)WebUtils.getOneByJsonPath(result, "$.result");
            log.info("{} 爬取账单的 token 是 {}",username,token);
            //利用得到的token进行get请求
            Map<String,String> params = new HashMap<>();
            params.put("beginDate",String.valueOf(crawlerDate.getTime()));
            params.put("endDate",String.valueOf(endCrawlerDate.getTime()));
            params.put("limit","10");
            params.put("loginRestaurantId",shopId);
            params.put("offset","0");
            params.put("restaurantId",shopId);
            params.put("status","3");
            params.put("token",token);
            String url2 = URL+HttpClientUtil.buildParamString(params);
            HttpGet get = new HttpGet(url2);
            execute = client.execute(get);
            entity = execute.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
            log.info("result json is {}",result);
            List<LinkedHashMap<String, Object>> mapsByJsonPath = WebUtils.getMapsByJsonPath(result, "$.bills");
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
     * 获取ElemeBill实体类
     * @param billList
     * @return
     */
    public List<ElemeBill> getElemeBillBeans(List<LinkedHashMap<String, Object>> billList){
        List<ElemeBill> list = new ArrayList<>();
        for(LinkedHashMap<String,Object> map : billList){
            ElemeBill elemeBill = new ElemeBill();
            elemeBill.setPri(String.valueOf(DateUtils.long2Date((Long)map.get("closingDate"))+"~"+String.valueOf(shopId)));
            elemeBill.setClosingDate(String.valueOf(DateUtils.long2Date((Long)map.get("closingDate"))));
            elemeBill.setIncome(notNull((String)map.getOrDefault("income","无")));
            elemeBill.setExpense(notNull((String)map.getOrDefault("expense","无")));
            elemeBill.setDeductAmount(notNull((String)map.getOrDefault("deductAmount","无")));
            elemeBill.setDueAmount(notNull((String)map.getOrDefault("dueAmount","无")));
            elemeBill.setPayAmount(notNull((String)map.getOrDefault("payAmount","无")));
            elemeBill.setPaymentDate(DateUtils.long2Date((Long)map.getOrDefault("paymentDate",null)));
            elemeBill.setShopId(Long.valueOf(shopId));
            if(merchantId != null){
                elemeBill.setMerchantId(merchantId);
            }
            list.add(elemeBill);
        }
        return list;
    }


    @Override
    public void doRun() {

    }
}
