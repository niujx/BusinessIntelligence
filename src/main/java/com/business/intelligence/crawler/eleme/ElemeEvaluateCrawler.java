package com.business.intelligence.crawler.eleme;

import com.business.intelligence.util.WebUtils;
import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.ElemeModel.ElemeEvaluate;
import com.business.intelligence.dao.ElemeDao;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tcqq on 2017/7/17.
 * 顾客评价
 */
@Component
public class ElemeEvaluateCrawler extends ElemeCrawler {
    //默认抓取前一天的，具体值已经在父类设置
    private Date crawlerDate = super.crawlerDate;
    //用户信息
    private Authenticate authenticate;
    @Autowired
    private ElemeDao elemeDao;

    private HttpClient httpClient = super.httpClient;
    private static final String HEADELEMEJSON = "https://www.ele.me/restapi/ugc/v1/restaurant/204666/ratings?limit=40&offset=";
    private static final String ENDELEMEJSON = "&record_type=1";
    private static final String COUNTJSON = "https://www.ele.me/restapi/ugc/v1/restaurant/204666/rating_categories";

    //测试用
    private static int index = 0;

    @Override
    public void doRun() {
        int count = getCount(COUNTJSON);
        System.out.println(count);
        List<ElemeEvaluate> allPageEvaluateByCustomer = getAllPageEvaluateByCustomer(count, 20466, HEADELEMEJSON, ENDELEMEJSON);
        //测试
        for(ElemeEvaluate elemeEvaluate : allPageEvaluateByCustomer){
            System.out.println(elemeEvaluate);
        }
        for (ElemeEvaluate elemeEvaluate : allPageEvaluateByCustomer){
            elemeDao.insertEvaluate(elemeEvaluate);
        }
    }

    /**
     * 获取评价个数
     */
    public int getCount(String url){
        String json = WebUtils.getWebByGet(url,httpClient);
        json = "{\"result\":{\"list\":"+json+"}}";
        Integer count = (Integer) WebUtils.getOneByJsonPath(json,"$.result.list[0].amount");
        return count;
    }

    /**
     * 获得单页（最多40个）的所有商品的顾客评价
     * @param shopId 商铺的ID
     * @param url 评论的地址
     */
    public List<ElemeEvaluate> getOnePageEvaluateByCustomer(long shopId, String url){
        List<ElemeEvaluate> list = new ArrayList<ElemeEvaluate>();
        String json = WebUtils.getWebByGet(url,httpClient);
        json = "{\"result\":{\"list\":"+json+"}}";
        List<LinkedHashMap<String, Object>> maps = WebUtils.getMapsByJsonPath(json, "$.result.list[*]");
        //取得所有订单评论
        for(LinkedHashMap map:maps){
            index++;
            ElemeEvaluate orderElemeEvaluate = new ElemeEvaluate();
            orderElemeEvaluate.setNum(index);
            orderElemeEvaluate.setType("订单评论");
            orderElemeEvaluate.setShopId(shopId);
            orderElemeEvaluate.setEvaValue(map.get("rating_text").toString());
            orderElemeEvaluate.setStar(Integer.valueOf(map.get("rating_star").toString()));
            orderElemeEvaluate.setDate(map.get("rated_at").toString());
            StringBuilder sb =new StringBuilder();
            List<LinkedHashMap<String, Object>> itemMaps = (List<LinkedHashMap<String, Object>>)map.get("item_rating_list");
            if(itemMaps.isEmpty()){
                orderElemeEvaluate.setGoods("顾客没有对任何具体的菜品进行评价");
            }
            for(LinkedHashMap<String, Object>  itemMap:itemMaps){
                sb.append(itemMap.get("rate_name").toString()+" ");
                orderElemeEvaluate.setGoods(sb.toString().trim());
                ElemeEvaluate elemeEvaluate = new ElemeEvaluate();
                elemeEvaluate.setNum(index);
                elemeEvaluate.setType("商品评价");
                elemeEvaluate.setShopId(shopId);
                elemeEvaluate.setDate(map.get("rated_at").toString());
                elemeEvaluate.setGoods(itemMap.get("rate_name").toString());
                elemeEvaluate.setStar(Integer.valueOf(itemMap.get("rating_star").toString()));
                elemeEvaluate.setEvaValue(itemMap.get("rating_text").toString());
                list.add(elemeEvaluate);
            }
            list.add(orderElemeEvaluate);
        }
        return list;
    }

    /**
     * 获得所有页的所有商品的顾客评价
     * @param evaluateCount 总评论个数
     * @param shopId 商铺的ID
     * @param headOfUrl 评论地址的前半部分
     * @param endOfUrl 评论地址的后半部分
     */
    public List<ElemeEvaluate> getAllPageEvaluateByCustomer(int evaluateCount, long shopId, String headOfUrl, String endOfUrl){
        int count = evaluateCount%40;
        List<ElemeEvaluate> allList = new ArrayList<ElemeEvaluate>();
        String url = "";
        for(int i = 0 ; i< count+1;i++){
            //  https://www.ele.me/restapi/ugc/v1/restaurant/204666/ratings?limit=40&offset=   ?    &record_type=1
            url = headOfUrl+i*40+endOfUrl;
            List<ElemeEvaluate> list = getOnePageEvaluateByCustomer(shopId, url);
            allList.addAll(list);
        }
        return allList;
    }

}
