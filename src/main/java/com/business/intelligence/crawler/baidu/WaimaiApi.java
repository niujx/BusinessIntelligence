package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.dao.BDDao;
import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.baidu.Comment;
import com.business.intelligence.model.baidu.OrderDetails;
import com.business.intelligence.model.baidu.OrderUpList;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@EnableAsync
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WaimaiApi {

    private static final Logger logger = LoggerFactory.getLogger(WaimaiApi.class);

    private static CloseableHttpClient client;

    private static CookieStore cookieStore = new BasicCookieStore();

    @Autowired
    private BDDao bdDao;

    @Autowired
    private CrawlerStatusDao crawlerStatusDao;


    public WaimaiApi() {
        if (client == null) {
            client = HttpClientUtil.getHttpClient(cookieStore);
        }
    }

    public static void main(String[] args) {
        client = HttpClientUtil.getHttpClient(cookieStore);
    }

    /**
     * 获取上行订单列表
     *
     * @param source 商户编号
     * @param secret 商户秘钥
     * @param shopId 百度商户id
     * @return
     */
    public String ouderListGet(String source, String secret, String shopId, String merchantId, String star, String end) {
        logger.info("获取订单列表{}", merchantId);

        Map<String, Object> params = new HashMap<>();
        params.put("cmd", "order.list");
        params.put("version", 3);
        params.put("timestamp", (int) (System.currentTimeMillis() / 1000));
        params.put("ticket", UUID.randomUUID().toString().toUpperCase());
        params.put("source", source);
        params.put("encrypt", "");
        params.put("secret", secret);


        List<OrderUpList> allList = allOrderList(params, star, end, shopId);
        for (OrderUpList orderUpList : allList) {
            //此处由于百度不支持批量查询，只能循环查询
            orderGet(orderUpList.getOrderId(), source, secret, merchantId);
        }
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.BD_ORDERDETAILS);
        if (f == 1) {
            logger.info(CrawlerName.BD_ORDERDETAILS + " 状态更新成功，状态为：已入库");
        } else {
            logger.info(CrawlerName.BD_ORDERDETAILS + " <已入库> 更新状态失败");
        }

        return JSONObject.toJSONString(allList);
    }

    /**
     * 获取所有订单编号
     *
     * @param params
     * @param star
     * @param end
     * @param shopId
     * @return
     */
    private List<OrderUpList> allOrderList(Map<String, Object> params, String star, String end, String shopId) {
        List<OrderUpList> allList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("shop_id", shopId);
        map.put("start_time", DateUtils.timeStamp(star, "yyyy-MM-dd"));
        map.put("end_time", DateUtils.timeStamp(end, "yyyy-MM-dd"));
        int index = 1;//默认分页数
        for (int i = 1; i <= index; i++) {
            map.put("page", i);
            params.put("body", JSONObject.toJSON(map));
            logger.info("当前请求订单列表页数：{}", i);
            List<String> s = new ArrayList<>();
            params.keySet().stream().sorted().forEach(k -> s.add(k + "=" + params.get(k)));
            String join = StringUtils.join(s, "&");
            String sign = getMD5(chinaToUnicode(join.replace("/", "\\/")));
            params.put("sign", sign);
            HttpPost post = HttpClientUtil.post("http://api.waimai.baidu.com/", params);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                String content = HttpClientUtil.executePostWithResult(client, post);
                logger.info("获取百度上行订单列表{}", content);
                if (content.contains("order_id")) {
                    JSONObject json = JSONObject.parseObject(content);
                    JSONObject body = json.getJSONObject("body").getJSONObject("data");
                    JSONArray array = body.getJSONArray("list");
                    if (array.size() > 0) {
                        allList.addAll(array.toJavaList(OrderUpList.class));
                    }
                    if (i == 1) {
                        int total = body.getIntValue("total");//获取总条数
                        index = Math.round(total / 100);//获取总分页数
                        logger.info("商户id：《" + shopId + "》 共有 《" + total + "》 条订单；需要 《" + index + "》 次去执行");
                    }
                    if (i == index) {
                        logger.info("百度上行订单列表内容获取完成，共计: " + index + " 页，准备入库。。。");
                        //更新爬取状态为进行中
                        int ii = crawlerStatusDao.updateStatusING(CrawlerName.BD_ORDERDETAILS);
                        if (ii == 1) {
                            logger.info(CrawlerName.BD_ORDERDETAILS + " 状态更新成功，状态为：进行中");
                        } else {
                            logger.info(CrawlerName.BD_ORDERDETAILS + " <进行中>状态更新失败");
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                logger.error("获取上行订单列表出错", e);
            }

        }
        return allList;
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单编号
     * @param source  商户编号
     * @param secret  商户秘钥
     * @return
     */
    @Async
    private String orderGet(String orderId, String source, String secret, String merchantId) {
        logger.info("获取订单详情{}", orderId);
        Map<String, Object> params = new HashMap<>();
        params.put("cmd", "order.get");
        params.put("version", 3);
        params.put("timestamp", (int) (System.currentTimeMillis() / 1000));
        params.put("ticket", UUID.randomUUID().toString().toUpperCase());
        params.put("source", source);
        params.put("encrypt", "");
        params.put("secret", secret);
        Map<String, Object> map = new HashMap<>();

        map.put("order_id", orderId);
        params.put("body", JSONObject.toJSON(map));

        List<String> s = new ArrayList<>();
        params.keySet().stream().sorted().forEach(k -> s.add(k + "=" + params.get(k)));
        String join = StringUtils.join(s, "&");
//        logger.info("join============>" + join);
        String sign = getMD5(chinaToUnicode(join.replace("/", "\\/")));
        params.put("sign", sign);
        HttpPost post = HttpClientUtil.post("http://api.waimai.baidu.com/", params);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        String content = null;
        try {
            content = HttpClientUtil.executePostWithResult(client, post);
            if (StringUtils.isNotEmpty(content)) {
                List<OrderDetails> odList = Parser.odParser(content, merchantId);
                for (OrderDetails od : odList) {
                    bdDao.insertOrderDetails(od);
                    logger.info("订单详情入库成功{}", orderId);
                }
            }

        } catch (IOException e) {
            logger.error("获取上行订单详情出错", e);
        }
        return content;
    }

    /**
     * 获取评论
     *
     * @param source 商户编号
     * @param secret 秘钥
     * @param shopId 百度商户id
     * @return
     */
    public String commentGet(String source, String secret, String shopId, String merchantId, String star, String end) {
        logger.info("获取用户评论列表{}", merchantId);

        Map<String, Object> params = new HashMap<>();
        params.put("cmd", "shop.comment.get");
        params.put("version", 3);
        params.put("timestamp", (int) (System.currentTimeMillis() / 1000));
        params.put("ticket", UUID.randomUUID().toString().toUpperCase());
        params.put("source", source);
        params.put("encrypt", "");
        params.put("secret", secret);

        List<String> listCtx = getCommentCtx(params, shopId, star, end);
        for (String ctx : listCtx) {
            List<Comment> ctList = Parser.ctParser(ctx, merchantId);
            for (Comment ct : ctList) {
                bdDao.insertComment(ct);
                logger.info("用户评论入库成功{}", merchantId);
            }
            int f = crawlerStatusDao.updateStatusFinal(CrawlerName.BD_COMMENT);
            if (f == 1) {
                logger.info(CrawlerName.BD_COMMENT + " 状态更新成功，状态为：已入库");
            } else {
                logger.info(CrawlerName.BD_COMMENT + " <已入库> 更新状态失败");
            }
        }
        return "shop.comment.get is ok";
    }

    private List<String> getCommentCtx(Map<String, Object> params, String shopId, String star, String end) {
        List<String> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("shop_id", shopId);
        map.put("start_time", DateUtils.timeStamp(star, "yyyy-MM-dd"));
        map.put("end_time", DateUtils.timeStamp(end, "yyyy-MM-dd"));
        int index = 1;//默认分页数
        for (int i = 1; i <= index; i++) {
            logger.info("当前请求评论列表页数：{}", i);
            map.put("curr_page", i);
            params.put("body", JSONObject.toJSON(map));
            List<String> s = new ArrayList<>();
            params.keySet().stream().sorted().forEach(k -> s.add(k + "=" + params.get(k)));
            String join = StringUtils.join(s, "&");
            logger.info("join============>" + join);
            String sign = getMD5(chinaToUnicode(join.replace("/", "\\/")));
            params.put("sign", sign);
            HttpPost post = HttpClientUtil.post("http://api.waimai.baidu.com/", params);
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            try {
                String content = HttpClientUtil.executePostWithResult(client, post);
                logger.info("获取百度评论列表{}", content);
                if (content.contains("comment_id")) {
                    list.add(content);
                    if (i == 1) {
                        JSONObject json = JSONObject.parseObject(content);
                        JSONObject body = json.getJSONObject("body");
                        JSONObject data = body.getJSONObject("data");
                        JSONObject page_info = data.getJSONObject("page_info");
                        index = page_info.getIntValue("page_count");
                        int total = page_info.getIntValue("total");
                        logger.info("商户id：《" + shopId + "》 共有 《" + total + "》 条评论；需要 《" + index + "》 次去执行");
                    }
                    if (i == index) {
                        logger.info("评论列表页内容获取完成，共计: " + index + " 页，准备入库。。。");
                        //更新爬取状态为进行中
                        int ii = crawlerStatusDao.updateStatusING(CrawlerName.BD_COMMENT);
                        if (ii == 1) {
                            logger.info(CrawlerName.BD_COMMENT + " 状态更新成功，状态为：进行中");
                        } else {
                            logger.info(CrawlerName.BD_COMMENT + " <进行中>状态更新失败");
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("获取商户评论列表出错", e);
            }
        }


        return list;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
}
