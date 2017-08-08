package com.business.intelligence.crawler.mt;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.crawler.baidu.CodeImage;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.mt.MTOrder;
import com.business.intelligence.util.CookieStoreUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
import com.business.intelligence.util.MD5;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.httpclient.URI;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.util.Lists;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.business.intelligence.util.HttpClientUtil.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
public class MTCrawler extends BaseCrawler {
    private String LOGIN_URL = "https://epassport.meituan.com/account/loginv2?service=waimai&continue=http://e.waimai.meituan.com/v2/epassport/entry&part_type=0&bg_source=3";
    private CloseableHttpClient client;
    private CookieStore cookieStore = new BasicCookieStore();
    private LoginBean loginBean;
    private AccountInfo accountInfo;
    private CookieStore cookieStore2;


    public MTCrawler(LoginBean loginBean) {
        client = HttpClientUtil.getHttpClient(cookieStore);
        this.loginBean = loginBean;
    }

    @Override
    public void doRun() {

    }


    public void login() {
        List<BasicNameValuePair> params = loginBean.form().entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(toList());
        try {
            HttpPost loginS1 = new HttpPost(LOGIN_URL);
            loginS1.setHeader("accept", "application/json");
            loginS1.setHeader("Accept-Encoding", "gzip, deflate, br");
            loginS1.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            loginS1.setHeader("Connection", "keep-alive");
            loginS1.setHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
            loginS1.setHeader("Host", "epassport.meituan.com");
            loginS1.setHeader("Origin", "https://epassport.meituan.com");
            loginS1.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            loginS1.setHeader("Cookie", "wpush_server_url=wss://wpush.meituan.com; shopCategory=food");
            loginS1.setEntity(new UrlEncodedFormEntity(params, Charset.forName(UTF_8)));

            CloseableHttpResponse execute = client.execute(loginS1);
            if (execute.getStatusLine().getStatusCode() == 302) {
                Header location = execute.getFirstHeader("Location");
                String BSID = location.getValue().split("=")[1];
                log.info("find BSID is {}", BSID);
                //重试2个 第一次获取设备ID 第二次登录成功
                for (int i = 0; i < 2; i++) {
                    Optional<String> uuid = cookieStore.getCookies().stream().filter(cookie -> cookie.getName().equals("device_uuid")).map(cookie -> cookie.getValue()).findFirst();
                    String uuidDev = null;
                    //获取设备ID
                    if (uuid.isPresent()) {
                        uuidDev = uuid.get();
                        log.info("uuid is {}", uuidDev);
                    }
                    //  HttpPost loginS2 = new HttpPost("https://waimaie.meituan.com/v2/epassport/logon");
                    HttpPost loginS2 = new HttpPost("http://e.waimai.meituan.com/v2/epassport/logon");
                    loginS2.setEntity(new UrlEncodedFormEntity(Lists.newArrayList(new BasicNameValuePair("BSID", BSID), new BasicNameValuePair("device_uuid", uuidDev))));
                    CloseableHttpResponse execute1 = client.execute(loginS2);
                    String loginMessage = EntityUtils.toString(execute1.getEntity());
                    log.info("{}", loginMessage);
                    ReadContext parse = JsonPath.parse(loginMessage);
                    String success = parse.read("$.msg");
                    if ("Success".equals(success)) {
                        log.info("login success");
                        accountInfo = new AccountInfo();
                        accountInfo.setWmPoiId(parse.read("$.data.wmPoiId"));
                        accountInfo.setAccessToken(parse.read("$.data.accessToken"));
                        accountInfo.setAcctId(parse.read("$.data.acctId"));
                        log.info("create account info : {}", accountInfo);


                        //跨区单点登录地址 更新JSESSIONID
                        String waimaieappLogin = "https://waimaieapp.meituan.com/bizdata/?_source=PC&token=" + accountInfo.getAccessToken() + "&acctId=" + accountInfo.getAcctId() + "&wmPoiId=" + accountInfo.getWmPoiId();
                        HttpClientUtil.executeGetWithResult(client, waimaieappLogin);
                        //保存COOKIE 到指定文件

                        HttpClientUtil.executeGetWithResult(client, "http://e.waimai.meituan.com");
                        CookieStoreUtils.storeCookie(cookieStore, loginBean.cookieStoreName());
                        break;
                    }
                }

            } else {
                //输入验证码重新登录
                String captchaJson = EntityUtils.toString(execute.getEntity());
                log.info("json is {}", captchaJson);
                String token = JsonPath.read(captchaJson, "$.error.captcha_v_token");
                String captchaImage = "https://epassport.meituan.com/bizverify/captcha?verify_event=1&captcha_v_token=" + token + "&" + System.currentTimeMillis();
                String path = HttpUtil.getCaptchaCodeImage(client, new HttpGet(captchaImage));
                String captchaCode = CodeImage.Imgencode(path);
                log.info("get captchaCode is {} ", captchaCode);
                loginBean.setCaptchaVtoken(token);
                loginBean.setCaptchaCode(captchaCode);
                login();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 经营分析报表下载

    /**
     * @param fromDate
     * @param endDate
     * @param isLogin
     */
    public void bizDataReport(String fromDate, String endDate, Boolean isLogin) throws InterruptedException {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            String reportJson, url;
            int taskId = 0;
            int status;
            while (true) {
                reportJson = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/report/charts/download?wmPoiIdSel=%s&fromDate=%s&toDate=%s&taskId=%s", accountInfo.wmPoiId, fromDate, endDate, taskId == 0 ? "" : taskId));
                log.info("read json is {}", reportJson);
                ReadContext parse = JsonPath.parse(reportJson);
                taskId = parse.read("$.data.taskId");
                status = parse.read("$.data.status");
                if (status != 0) {
                    url = parse.read("$.data.url");
                    break;
                }
                TimeUnit.SECONDS.sleep(10);
            }

            url = new URI(url).toString();
            log.info("csv url is {}", url);
            List<MTOrder> orders = Lists.newArrayList();
            try (CloseableHttpResponse execute = client.execute(new HttpGet(url))) {
                if (execute.getStatusLine().getStatusCode() == 200) {
                    Reader reader = new InputStreamReader(new BOMInputStream(execute.getEntity().getContent()), "GBK");
                    CSVParser csvRecords = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
                    for (CSVRecord record : csvRecords) {
                        log.info("length is {}", record.size());
                        int i = 0;
                        MTOrder order = new MTOrder();
                        order.setAppNo(record.get(i++));
                        order.setOrderTime(record.get(i++));
                        order.setHourLong(record.get(i++));
                        order.setName(record.get(i++));
                        order.setId(record.get(i++));
                        order.setCity(record.get(i++));
                        order.setType(record.get(i++));
                        order.setStatus(record.get(i++));
                        order.setDisStatus(record.get(i++));
                        order.setIsSchedule(record.get(i++));
                        order.setPostDiscount(record.get(i++));
                        order.setTotalPrice(record.get(i++));
                        order.setMtPrice(record.get(i++));
                        order.setMerchantPrice(record.get(i++));
                        order.setDishInfo(record.get(i++));
                        order.setDeliveryfee(record.get(i++));
                        order.setIsDiscount(record.get(i++));
                        order.setPreferential(record.get(i++));
                        order.setIsPress(record.get(i++));
                        order.setReplyStatus(record.get(i++));
                        order.setMerchantReplay(record.get(i++));
                        order.setComplaintTime(record.get(i++));
                        order.setComplaintInfo(record.get(i++));
                        order.setAppraiseTime(record.get(i++));
                        order.setDeliveryTime(record.get(i++));
                        order.setStar(record.get(i++));
                        order.setAppraiseInfo(record.get(i++));
                        order.setFoodBoxPrice(record.get(i++));
                        order.setFoodBoxQuantity(record.get(i++));
                        order.setOrderDoneTime(record.get(i++));
                        order.setOrderCancelInfo(record.get(i++));
                        log.info("order info is {}", order);
                        orders.add(order);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            bizDataReport(fromDate, endDate, true);
        }


    }

    //营业数据 营业统计
    public void businessStatistics(String fromDate, String endDate, boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            String reportJson, url;
            int taskId = 0;
            int status;
            while (true) {
                reportJson = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/businessStatisticsV2/report/allAnalysis?wmPoiId=%s&beginTime=%s&endTime=%s&taskId=", "2843062", fromDate, endDate, taskId == 0 ? "" : taskId));
                log.info("read json is {}", reportJson);
                ReadContext parse = JsonPath.parse(reportJson);
                taskId = parse.read("$.data.taskId");
                status = parse.read("$.data.status");
                if (status != 0) {
                    url = parse.read("$.data.url");
                    break;
                }
                TimeUnit.SECONDS.sleep(10);
            }
            url = new URI(url).toString();
            log.info("csv url is {}", url);
        } catch (Exception e) {
            e.printStackTrace();
            businessStatistics(fromDate, endDate, true);
        }
    }
//https://waimaieapp.meituan.com/bizdata/flowanalysis/flowgeneral/r/generalInfo?recentDays=30&wmPoiId=2843062&sortType=&sortValue=

    //营业统计流量分析
    public void flowanalysis(String days, boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            String json = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/flowanalysis/flowgeneral/r/generalInfo?recentDays=%s&wmPoiId=%s&sortType=&sortValue=", days, "2843062"));
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);
            int exposureNum = parse.read("$.data.flowGeneralInfoVo.exposureNum");
            int visitNum = parse.read("$.data.flowGeneralInfoVo.visitNum");
            int orderNum = parse.read("$.data.flowGeneralInfoVo.orderNum");
            log.info("exposureNum is {} ,visitNum is {} orderNum is {} ", exposureNum, visitNum, orderNum);
        } catch (Exception e) {
            e.printStackTrace();
            flowanalysis(days, true);
        }
    }

    //https://waimaieapp.meituan.com/bizdata/hotSales/data/download?startDate=2017-07-31&endDate=2017-08-06&type=count
    //营业分析热门商品
    public void hotSales(String fromDate, String endDate, boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            try (CloseableHttpResponse execute = client.execute(new HttpGet(String.format("https://waimaieapp.meituan.com/bizdata/hotSales/data/download?startDate=%s&endDate=%s&type=count", fromDate, endDate)))) {
                XSSFWorkbook hssfWorkbook = new XSSFWorkbook(execute.getEntity().getContent());
                XSSFSheet sheet = hssfWorkbook.getSheetAt(0);
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    XSSFRow hssfRow = sheet.getRow(rowNum);
                    if (hssfRow != null) {
                        log.info("cell is {}", hssfRow.getCell(0));
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            hotSales(fromDate, endDate, true);
        }
    }


    //评价管理
    //http://e.waimai.meituan.com/v2/customer/getCommentList?wmPoiId=2843062&acctId=27578629&token=0a2TJcERKArTCsg5YO2qsWVv3JoRgTnEUynbbnU4_olc*&rate=-1&reply=-1&context=-1&startDate=2017-07-07&endDate=2017-08-06&pageNum=1
    public void comment(String fromDate, String endDate, boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            String url = String.format("http://e.waimai.meituan.com/v2/customer/getCommentList?wmPoiId=%s&acctId=%s&token=%s&rate=-1&reply=-1&context=-1&startDate=%s&endDate=%s&pageNum=1", accountInfo.wmPoiId, accountInfo.acctId, accountInfo.accessToken, fromDate, endDate);
            String json = HttpClientUtil.executeGetWithResult(client, url);
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);

        } catch (Exception e) {
            e.printStackTrace();
            comment(fromDate, endDate, true);
        }
    }

    //https://waimaieapp.meituan.com/finance/pc/api/poiSettleBill/historySettleBillList?settleBillStartDate=2017-02-07&settleBillEndDate=2017-08-06&pageSize=10&pageNo=1
    //财务管理
    public void historySettleBillList(String fromDate, String endDate, boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            String url = String.format("https://waimaieapp.meituan.com/finance/pc/api/settleBillExport/billExportTask?beginDate=%s&endDate=%s", fromDate, endDate);
            String json = HttpClientUtil.executeGetWithResult(client, url);
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);
            int taskNo = parse.read("$.data.taskNo");
            //{"data":{"taskNo":1001079},"code":0,"msg":"success"}


            String format = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            url = String.format("https://waimaieapp.meituan.com/finance/v2/finance/orderChecking/export/download//meituan_waimai_file_bill_export-2017-08-06-1001079.xls", format, taskNo);
            log.info("excel url is {}", url);
            while (true) {
                try (CloseableHttpResponse execute = client.execute(new HttpGet(url))) {
                    XSSFWorkbook hssfWorkbook = new XSSFWorkbook(execute.getEntity().getContent());
                    XSSFSheet sheet = hssfWorkbook.getSheetAt(0);
                    if (sheet == null) {
                        TimeUnit.SECONDS.sleep(10);
                        log.info("sleep...");
                        continue;
                    }
                    for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        XSSFRow hssfRow = sheet.getRow(rowNum);
                        if (hssfRow != null) {
                            log.info("cell is {}", hssfRow.getCell(0));
                        }

                    }

                }
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            comment(fromDate, endDate, true);
        }
    }

    //https://waimaieapp.meituan.com/reuse/activity/setting/r/listActs

    //评价管理
    public void acts(boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }


            Map<String, Object> params = Maps.newHashMap();
            params.put("wmPoiId", accountInfo.wmPoiId);
            String json = HttpClientUtil.executePostWithResult(client, "https://waimaieapp.meituan.com/reuse/activity/setting/r/listActs", params);
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);


            for (int i = 0; i < 2; i++) {
                Map<String, String> params1 = Maps.newHashMap();
                params1.put("isEntered", String.valueOf(i));
                params1.put("poiId", String.valueOf(accountInfo.wmPoiId));
                params1.put("pageStart", "0");
                params1.put("numPerPage", "10");
                List<BasicNameValuePair> nameValuePairs = params1.entrySet().stream().map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(toList());

                String url = "https://waimaieapp.meituan.com/api/invite/query";
                HttpPost httpPost = new HttpPost(url);

                httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
                httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
                httpPost.setHeader("Connection", "keep-alive");
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                CloseableHttpResponse execute = client.execute(httpPost);
                log.info("code is {}", EntityUtils.toString(execute.getEntity()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Data
    static class LoginBean {
        private Authenticate authenticate;
        private String parkey;
        private String captchaCode;
        private String captchaVtoken;
        private String smsVerify;
        private String smsCode;

        public String cookieStoreName() {
            return MD5.md5(authenticate.getUserName() + "_" + authenticate.getPassword()) + ".cookies";
        }


        public Map<String, String> form() {
            Map<String, String> values = Maps.newHashMap();
            values.put("login", authenticate.getUserName());
            values.put("password", authenticate.getPassword());
            values.put("park_key", parkey);
            values.put("captcha_code", captchaCode);
            values.put("captcha_v_token", captchaVtoken);
            values.put("sms_verify", smsVerify);
            values.put("sms_code", smsCode);
            return values;
        }

    }

    @Data
    static class AccountInfo {
        private String cityId;
        private Integer wmPoiId;
        private Integer acctId;
        private String accessToken;
        private String brandId;
    }

    public static void main(String[] args) throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        LoginBean loginBean = new LoginBean();
        loginBean.setAuthenticate(authenticate);

        MTCrawler mtCrawler = new MTCrawler(loginBean);

        //  mtCrawler.login(loginBean);
        // mtCrawler.bizDataReport("2017-07-02", "2017-08-02", false);
        // mtCrawler.businessStatistics("20170707","20170805",false);
        // mtCrawler.flowanalysis("30",false);
        /// mtCrawler.hotSales("2017-07-31","2017-08-06",true);
        //mtCrawler.comment("2017-08-01", "2017-08-06", true);
//waimaieapp.meituan.com/finance/pc/api/settleBillExport/billExportTask?beginDate=2017-07-01&endDate=2017-07-30
        //  mtCrawler.historySettleBillList("2017-07-03", "2017-07-29", true);
        mtCrawler.acts(true);
    }
}


