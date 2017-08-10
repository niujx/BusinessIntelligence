package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.dao.BDDao;
import com.business.intelligence.model.baidu.BookedTable;
import com.business.intelligence.model.baidu.BusinessData;
import com.business.intelligence.model.baidu.HotDishes;
import com.business.intelligence.model.baidu.ShopWthdrawal;
import com.business.intelligence.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAsync
@Service
public class WaimaiCrawler {

    private static final Logger log = LoggerFactory.getLogger(WaimaiCrawler.class);

    private String token;

    private CloseableHttpClient client;

    @Autowired
    private YmlConfig config;

    @Autowired
    private BDDao bdDao;

    private CookieStore cookieStore = new BasicCookieStore();

    private String shopId;

    public WaimaiCrawler() {
        if (client == null) {
            client = HttpClientUtil.getHttpClient(cookieStore);
        }
    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param passWord 密码
     * @param start    开始时间
     * @param end      结束时间
     * @param shopId   商户id
     */
    public String logins(String userName, String passWord, String start, String end, String shopId) {
        this.shopId = shopId;
        boolean tag = getCookiestores(userName, passWord, start, end);
        if (tag) {
            return "{errno:0000,errmsg:抓取进行中,data:null}";
        }
        getToken();
        String loginUrl = "https://wmpass.baidu.com/api/login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "1");
        map.put("channel", "pc");
        map.put("account", userName);
        map.put("upass", getPassWord(passWord));
        map.put("captcha", getCaptcha());
        map.put("token", token);
        map.put("redirect_url", "https://wmcrm.baidu.com/");
        map.put("return_url", "https://wmcrm.baidu.com/crm/setwmstoken");
        HttpPost post = HttpClientUtil.post(loginUrl, map);
        String content = null;
        try {
            content = HttpClientUtil.executePostWithResult(client, post);
            log.info(content);
            JSONObject json = JSONObject.parseObject(content);
            JSONObject data = json.getJSONObject("data");
            if (data != null) {
                String return_url = data.getString("return_url");
                HttpGet rget = HttpClientUtil.get(return_url);
                HttpClientUtil.executeGetWithResult(client, rget);
                HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/");

                String shouye = "https://wmcrm.baidu.com/crm?qt=neworderlist";
                rget = HttpClientUtil.get(shouye);
                content = HttpClientUtil.executeGetWithResult(client, rget);
                if (content.contains("百度商户")) {
                    loadBills(start, end);
                    setCookieStores(userName, passWord);
                }
            }

        } catch (IOException e) {
            log.error("登录百度失败", e);
        }
        return content;
    }

    /**
     * 统一下载
     */
    private void loadBills(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            startTime = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            endTime = DateUtils.formatDate(DateUtils.someMonthAgo(1), "yyyy-MM-dd");
        }
        dowShopdata(startTime, endTime);
        dowShophotsaledish(startTime, endTime);
        dowAllcashtradelist(startTime, endTime);
        dowWthdrawlist(startTime, endTime);
    }

    /**
     * 下载曝光数据
     *
     * @return
     */
//    @Async
    private void dowShopdata(String startTime, String endTime) {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm?qt=shopdata");
           // https://wmcrm.baidu.com/crm/shopdata?display=json&type=1&startTime=20170803&endTime=20170809
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm/shopdata?display=json&type=1&startTime=" + startTime.replace("-", "") + "&endTime=" + endTime.replace("-", "") + "&act=export");
            dwd.addHeader("referer", "https://wmcrm.baidu.com/crm?qt=shopdata");
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            dwd.addHeader("Upgrade-Insecure-Requests", "1");
            dwd.addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:54.0) Gecko/20100101 Firefox/54.0");
            CloseableHttpResponse response = client.execute(dwd);
            InputStream in = response.getEntity().getContent();
            File file = new File("/Users/wangfukun/other/img/");
            File target = new File(file.getParentFile(), "曝光数据表_" + startTime + "_" + System.currentTimeMillis() + ".csv");
            FileUtils.copyInputStreamToFile(in, target);

            try {
                List<String> list = CSVFileUtil.importCsv(target);
                List<BusinessData> bdList = Parser.bdParser(list, shopId);
                for (BusinessData bd : bdList) {
                    bdDao.insertBusinessData(bd);
                }
            } catch (Exception e) {
                log.error("解析曝光数据失败", e);
            }
        } catch (IOException e) {
            log.error("下载曝光数据失败", e);
        }
    }

    /**
     * 下载热销菜品
     *
     * @return
     */
//    @Async
    private void dowShophotsaledish(String startTime, String endTime) {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm?qt=shophotsaledish");
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm?qt=exportshophotsaledishtask&from=pc&start_time=" + startTime.replace("-", "") + "&end_time=" + endTime.replace("-", "") + "&orderby=xl&display=json&");
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            CloseableHttpResponse response = client.execute(dwd);
            try {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "UTF-8");
                log.info(content);
                if (isErrorCode(content)) {
                    getExporthistory("热销菜品导出");
                }
            } catch (Exception e1) {
                log.error("解析热销菜品导出页面出错", e1);
            } finally {
                response.close();
            }
        } catch (IOException e) {
            log.error("请求热销菜品导出页面出错", e);
        }
    }

    /**
     * 下载所有现金账户流水明细导出
     *
     * @param startTime
     * @param endTime
     */
//    @Async
    private void dowAllcashtradelist(String startTime, String endTime) {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm/settlement/balanceaccounttpl");
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm/settlement/exportallcashtradelist?begin_time=" + startTime + "&end_time=" + endTime + "&display=json");
            dwd.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            dwd.addHeader("Referer", "https://wmcrm.baidu.com/crm/settlement/balanceaccounttpl");
            dwd.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:54.0) Gecko/20100101 Firefox/54.0");
            dwd.addHeader("X-Requested-With", "XMLHttpRequest");
            CloseableHttpResponse response = client.execute(dwd);
            InputStream in = response.getEntity().getContent();
            try {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "UTF-8");
                if (isErrorCode(content)) {
                    getExporthistory("所有现金账户流水明细导出");
                }
            } catch (Exception e1) {
                log.error("解析所有现金账户流水明细导出页面出错", e1);
            } finally {
                response.close();
            }
        } catch (IOException e) {
            log.error("请求所有现金账户流水明细导出页面出错", e);
        }
    }

    /**
     * 下载自动提现账户
     *
     * @param startTime
     * @param endTime
     */
//    @Async
    private void dowWthdrawlist(String startTime, String endTime) {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm?qt=getstaterecordlist");
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm/settlement/exportwithdrawlist?trade_begin_time=" + startTime + "&trade_end_time=" + endTime + "&display=json");
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            CloseableHttpResponse response = client.execute(dwd);
            try {
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity, "UTF-8");
                if (isErrorCode(content)) {
                    getExporthistory("自动提现账户页面导出");
                }
            } catch (Exception e1) {
                log.error("解析自动提现账户页面导出页面出错", e1);
            } finally {
                response.close();
            }
        } catch (IOException e) {
            log.error("请求自动提现账户页面导出页面出错", e);
        }
    }

    /**
     * 查询导出历史页面
     *
     * @param name 导出类型
     */
    public void getExporthistory(String name) {
        boolean flag = true;
        String now = DateUtils.formatDate(new Date(), "yyyy-MM-dd");//记录当前日期，用于下载判断
        while (flag) {
            String url = "https://wmcrm.baidu.com/crm?qt=exporthistory";
            if (name.equals("热销菜品导出")) {
                //热销菜品的请求历史链接与账单的不一致
                url = "https://wmcrm.baidu.com/crm?qt=crmexporthistory&version=soa";
            }
            try {
                String content = HttpClientUtil.executeGetWithResult(client, url);
                Map<String, Boolean> map = new HashMap<String, Boolean>();
                if (content.contains("content")) {
                    String contentList = Extracter.matchFirst(content.replace(" ", "").replaceAll("\n", ""), "content\":(.*),shop_user:");
                    if (contentList.contains("download_url")) {
                        if (name.equals("热销菜品导出")) {
                            //热销菜品的返回内容需要重组
                            contentList = "{\"list\":" + contentList + "}";
                        }
                        log.info(contentList);
                        JSONObject json = JSONObject.parseObject(contentList);
                        JSONArray list = json.getJSONArray("list");
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject j = list.getJSONObject(i);
                            String dow = j.getString("download_url");//下载链接
                            String create_time = j.getString("create_time");//导出时间
                            if (StringUtils.isNotEmpty(dow) && StringUtils.isNotEmpty(create_time)) {
                                StringBuilder key = new StringBuilder(name).append("_").append(create_time);
                                String rowKey = MD5.md5(key.toString());
                                String day = create_time.substring(0, 10);
                                //下载日期是今天并且map没有该rowkey时，说明是第一次下载，初始化map，并赋值false
                                if (day.equals(now) && map.get(rowKey) == null) {
                                    map.put(rowKey, false);
                                }
                                if (map.size() > 0) {
                                    //当map中有值时，并且下载链接不为空、value为false时进行下载
                                    if (StringUtils.isNotEmpty(dow) && !map.get(rowKey)) {
                                        dowCsv(name, dow);
                                        map.put(rowKey, true);
                                    }
                                }
                                if (!map.containsValue(false)) {
                                    //当map中不存在值为false时，说明全部下载完毕
                                    map.clear();
                                    flag = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                log.error("请求导出进度查询页面出错", e);
            }
        }

    }

    /**
     * 下载csv并入库
     *
     * @param name
     * @param url
     */
    @Async
    private void dowCsv(String name, String url) {
        try {
            HttpGet dwd = HttpClientUtil.get(url);
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            CloseableHttpResponse response = client.execute(dwd);
            InputStream in = response.getEntity().getContent();
//            File file = new File("/Users/wangfukun/other/img/"+"baidu/");
            File file = new File(config.getCsvPath() + "baidu/");
            File target = new File(file.getParentFile(), name + "_" + DateUtils.formatDate(new Date(), "yyyyMMdd") + "_" + System.currentTimeMillis() + ".csv");
            FileUtils.copyInputStreamToFile(in, target);
            //此处去解析入库
            List<String> list = CSVFileUtil.importCsv(target);
            if (list.size() <= 0) {
                log.info("{0}没有需要的数据", name);
                return;
            }
            switch (name) {
                case "热销菜品导出":
                    List<HotDishes> hotList = Parser.hotParser(list, shopId);
                    for (HotDishes hot : hotList) {
                        bdDao.insertHotDishes(hot);
                    }
                    break;
                case "所有现金账户流水明细导出":
                    List<BookedTable> btList = Parser.btParser(list, shopId);
                    for (BookedTable bt : btList) {
                        bdDao.insertBookedTable(bt);
                    }
                    break;
                case "自动提现账户页面导出":
                    List<ShopWthdrawal> swList = Parser.swParser(list, shopId);
                    for (ShopWthdrawal sw : swList) {
                        bdDao.insertShopWthdrawal(sw);
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("下载 【{0}】 csv失败", name, e);
        }
    }

    /**
     * 获取图片验证token
     *
     * @return
     */
    public void getToken() {
        try {
            String url = "https://wmpass.baidu.com/wmpass/openservice/captchapair?protocal=https&callback=jQuery111003751282313330697_1500275164771&_=" + System.currentTimeMillis();
            HttpGet get = HttpClientUtil.get(url);
            String content = HttpClientUtil.executeGetWithResult(client, get);

            content = Extracter.matchFirst(content, "\\((.*)\\)");
            if (content.contains("token")) {
                JSONObject json = JSONObject.parseObject(content);
                JSONObject data = json.getJSONObject("data");
                token = data.getString("token");
            }
        } catch (Exception e) {
            log.error("获取图片验证token出错", e);
        }
    }


    /**
     * 获取加密密码
     *
     * @param sourcePwd
     * @return
     */
    public static String getPassWord(String sourcePwd) {
        String upass = null;
        try {
            byte[] b = null;
            b = sourcePwd.getBytes("utf-8");
            if (b != null) {
                StringBuffer stringBuffer = new StringBuffer(new BASE64Encoder().encode(b).replace("=", ""));//base64加密
                upass = stringBuffer.reverse().toString();//加密结果倒叙
            }

        } catch (Exception e) {
            log.error("获取加密密码失败,密码：{}", sourcePwd, e);
        }
        return upass;
    }

    private boolean isErrorCode(String content) {
        boolean flag = false;
        JSONObject json = JSONObject.parseObject(content);
        String errno = json.getString("errno");
        if ("0".equals(errno)) {
            return true;
        }
        return flag;
    }

    private void setCookieStores(String userName, String pwd) {
        CookieStoreUtils.storeCookie(cookieStore, MD5.md5(userName + "_" + pwd) + ".cookies");
    }


    private boolean getCookiestores(String userName, String pwd, String start, String end) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(MD5.md5(userName + "_" + pwd) + ".cookies");
            if (localCookie == null) {
                return false;
            } else {
                cookieStore = localCookie;
                client = HttpClientUtil.getHttpClient(cookieStore);
                String shouye = "https://wmcrm.baidu.com/crm?qt=neworderlist";
                HttpGet get = HttpClientUtil.get(shouye);
                String content = HttpClientUtil.executeGetWithResult(client, get);
                if (content.contains("百度商户")) {
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                loadBills(start, end);
                                setCookieStores(userName, pwd);
                            }
                        }).start();
                    } catch (Exception e) {
                        log.error("异步调用失败{}", userName, e);
                    }
                    return true;
                }
            }
        } catch (Exception e) {

        }


        return false;
    }

    /**
     * 获取图片验证码
     *
     * @return
     */

    private String getCaptcha() {
        String url = "https://wmpass.baidu.com/wmpass/openservice/imgcaptcha?token=" + token + "&t=" + System.currentTimeMillis() + "&color=3c78d8";
        HttpGet httpget = new HttpGet(url);
        String path = HttpUtil.getCaptchaCodeImage(client, httpget);
        return CodeImage.Imgencode(path);
    }

    //
//    public static void main(String[] args) {
//
//
//        WaimaiCrawler l = new WaimaiCrawler();
////        l.getExporthistory();
//        l.getToken();
//        CloseableHttpClient httpRequest = HttpClients.createDefault();
//        String url = "https://wmpass.baidu.com/wmpass/openservice/imgcaptcha?token=" + token + "&t=" + System.currentTimeMillis() + "&color=3c78d8";
//        HttpGet httpget = new HttpGet(url);
////        String path = HttpUtil.getCaptchaCodeImage(client, httpget);
////        String imgCode = CodeImage.Imgencode(path);
//        String pwd = getPassWord("wang170106");
//        log.info("密码{}", pwd);
//        ;
////        Scanner scanner = new Scanner(System.in);
////        System.out.print("请输入验证码：");
////        String img = scanner.nextLine();
//        log.info("图片验证码{}",imgCode);
//        l.logins("twfhscywjd", pwd, imgCode);
//    }

}
