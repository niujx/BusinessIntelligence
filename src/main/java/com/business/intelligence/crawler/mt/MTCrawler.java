package com.business.intelligence.crawler.mt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.crawler.baidu.CodeImage;
import com.business.intelligence.dao.CrawlerStatusDao;
import com.business.intelligence.dao.MTDao;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.model.CrawlerName;
import com.business.intelligence.model.mt.*;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MTCrawler extends BaseCrawler {

    private String LOGIN_URL = "https://epassport.meituan.com/api/account/login?service=waimai&bg_source=3&loginContinue=http://e.waimai.meituan.com/v2/epassport/entry";
    private CloseableHttpClient client;
    private CookieStore cookieStore = new BasicCookieStore();
    private LoginBean loginBean;
    private AccountInfo accountInfo;
    @Autowired
    private CrawlerStatusDao crawlerStatusDao;
    @Autowired
    private MTDao mtDao;

    private int retry = 0;

    public MTCrawler() {
        client = HttpClientUtil.getHttpClient(cookieStore);
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }


    @Override
    public void doRun() {

    }

    public void login() {
        try {
            HttpPost loginS1 = new HttpPost(LOGIN_URL);
            loginS1.setHeader("Pragma", "no-cache");
            loginS1.setHeader("Accept", "application/json");
            loginS1.setHeader("Accept-Encoding", "gzip, deflate, br");
            loginS1.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            loginS1.setHeader("Connection", "keep-alive");
            loginS1.setHeader("Content-type", "application/json");
            loginS1.setHeader("Host", "epassport.meituan.com");
            loginS1.setHeader("Origin", "https://epassport.meituan.com");
            loginS1.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
            //'Cache-Control: no-cache' -H 'x-requested-with: XMLHttpRequest'  -H 'Connection: keep-alive'
            loginS1.setHeader("x-requested-with", "XMLHttpRequest");
            loginS1.setHeader("Cache-Control", "no-cache");
            loginS1.setHeader("Connection", "keep-alive");
            log.info("login json is {}", loginBean.json());
            loginS1.setEntity(new StringEntity(loginBean.json()));

            CloseableHttpResponse execute = client.execute(loginS1);
            String loginJson = EntityUtils.toString(execute.getEntity());
            ReadContext loginJsonParser = JsonPath.parse(loginJson);
            log.info("{}", loginJson);
            Integer message = loginJsonParser.read("$.status.code");
            if (message == 2002) {
                log.info("需要短信验证码");
                return;
            }
            if (execute.getStatusLine().getStatusCode() == 200 && message == 0) {
                String bsid = loginJsonParser.read("$.bsid");
                Optional<String> uuid = cookieStore.getCookies().stream().filter(cookie -> cookie.getName().equals("device_uuid")).map(cookie -> cookie.getValue()).findFirst();
                String uuidDev = null;
                //获取设备ID
                if (uuid.isPresent()) {
                    uuidDev = uuid.get();
                    log.info("uuid is {}", uuidDev);
                }
                for (int i = 0; i < 2; i++) {
                    HttpPost loginS2 = new HttpPost("http://e.waimai.meituan.com/v2/epassport/logon");
                    loginS2.setEntity(new UrlEncodedFormEntity(Lists.newArrayList(new BasicNameValuePair("BSID", bsid), new BasicNameValuePair("device_uuid", uuidDev)
                            , new BasicNameValuePair("service", ""))));
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

                        String s = HttpClientUtil.executeGetWithResult(client, "http://e.waimai.meituan.com/api/poi/poiList");
                        log.info("shop name is {}", s);
                        parse = JsonPath.parse(s);
                        String name = parse.read("$.data[0].poiName");
                        accountInfo.setName(name);

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
                //{"status":{"code":2001,"message":"请输入图片验证码"},"loginToken":null,"needChangePassword":null,"captchaToken":"04e439ca56de48c59733412a610b151b","maskMobile":null,"continue":"http://e.waimai.meituan.com/v2/epassport/entry"}
                //输入验证码重新登录
                String token = loginJsonParser.read("$.captchaToken");
                //https://verify.meituan.com/v2/captcha?action=merchantlogin&timestamp=1503192324135&request_code=04e439ca56de48c59733412a610b151b
                String captchaImage = "https://verify.meituan.com/v2/captcha?action=merchantlogin&timestamp=" + System.currentTimeMillis() + "&request_code=" + token;
                String path = HttpUtil.getCaptchaCodeImage(client, new HttpGet(captchaImage));
                String captchaCode = CodeImage.Imgencode(path);
                log.info("get captchaCode is {} ", captchaCode);
                loginBean.setCaptchaVtoken(token);
                loginBean.setCaptchaCode(captchaCode);
                retry++;
                if (retry < 2)
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
            AccountInfo localaccountInfo = (AccountInfo) CookieStoreUtils.readStore(loginBean.cookieStoreName() + "$loginbean");
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
                accountInfo = localaccountInfo;
            }

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_REPORT_FORMS);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
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
            try (CloseableHttpResponse execute = client.execute(new HttpGet(url))) {
                if (execute.getStatusLine().getStatusCode() == 200) {
                    Reader reader = new InputStreamReader(new BOMInputStream(execute.getEntity().getContent()), "GBK");
                    CSVParser csvRecords = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
                    log.info("load bizData size is {}", csvRecords.getCurrentLineNumber());
                    for (CSVRecord record : csvRecords) {
                        log.info("length is {}", record.size());
                        int i = 0;
                        MTOrder order = new MTOrder();
                        order.setAppNo(record.get(0));
                        order.setOrderTime(toDate(record.get(1)));
                        order.setHourLong(toDate(record.get(2)));
                        order.setName(record.get(3));
                        order.setId(accountInfo.wmPoiId + "$" + record.get(4) + "$" + order.getAppNo());
                        order.setCity(record.get(5));
                        order.setType(record.get(6));
                        order.setStatus(record.get(7));
                        order.setDisStatus(record.get(8));
                        order.setIsSchedule(record.get(9));
                        // order.setPostDiscount(record.get(i++));
                        order.setTotalPrice(record.get(10));
                        order.setPostDiscount(record.get(11));
                        order.setMtPrice(record.get(12));
                        order.setMerchantPrice(record.get(13));
                        order.setDishInfo(record.get(14));
                        order.setDeliveryfee(record.get(15));
                        order.setIsDiscount(record.get(16));
                        order.setPreferential(record.get(17));
                        order.setIsPress(record.get(18));
                        order.setReplyStatus(record.get(19));
                        order.setMerchantReplay(record.get(20));
                        order.setComplaintTime(toDate(record.get(21)));
                        //   order.setComplaintInfo(record.get(i++));
                        // order.setAppraiseTime(toDate(record.get(i++)));
                        ///  order.setDeliveryTime(toDate(record.get(i++)));
                        //  order.setStar(record.get(i++));
                        //   order.setAppraiseInfo(record.get(i++));
                        order.setFoodBoxPrice(record.get(22));
                        order.setFoodBoxQuantity(record.get(23));
                        order.setOrderDoneTime(toDate(record.get(24)));
                        order.setOrderCancelInfo(record.get(25));
                        order.setMerchantId(loginBean.getMerchantId());
                        mtDao.insertOrder(order);
                        log.info("order info is {} ", order);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                bizDataReport(fromDate, endDate, true);
            }
        }
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_REPORT_FORMS);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
        }

    }

    private Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            return DateUtils.parseDate(dateStr.trim(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        } catch (ParseException e) {
            return null;
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

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_CRAWLER_SALE);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }

            String reportJson, url = null;
            int taskId = 0;
            int status;
            int count = 0;
            while (count < 10) {
                reportJson = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/businessStatisticsV2/report/allAnalysis?wmPoiId=%s&beginTime=%s&endTime=%s&taskId=", accountInfo.wmPoiId, fromDate, endDate, taskId == 0 ? "" : taskId));
                log.info("read json is {}", reportJson);
                ReadContext parse = JsonPath.parse(reportJson);
                taskId = parse.read("$.data.taskId");
                status = parse.read("$.data.status");
                if (status != 0) {
                    url = parse.read("$.data.url");
                    break;
                }
                TimeUnit.SECONDS.sleep(60);
                count++;
            }
            if (url == null) return;
            url = new URI(url).toString();
            log.info("csv url is {}", url);
            count = 0;
            while (count < 10) {
                try (CloseableHttpResponse execute = client.execute(new HttpGet(url))) {
                    if (execute.getStatusLine().getStatusCode() == 200) {
                        Workbook hssfWorkbook = WorkbookFactory.create(execute.getEntity().getContent());
                        Sheet sheet = hssfWorkbook.getSheetAt(0);

                        if (sheet == null) {
                            TimeUnit.SECONDS.sleep(60);
                            log.info("sleep...");
                            continue;
                        }
                        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                            Row hssfRow = sheet.getRow(rowNum);
                            if (hssfRow != null) {
                                int i = 0;
                                MTBusiness mtBusiness = new MTBusiness();
                                mtBusiness.setDate(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setTotal(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setShopSubsidy(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setMeiTuanSubsidy(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setEstimate(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setValidateOrder(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setInvalidateOrder(hssfRow.getCell(i++).getStringCellValue());
                                mtBusiness.setId(accountInfo.wmPoiId + "$" + mtBusiness.getDate());
                                mtBusiness.setShopName(accountInfo.name);
                                mtBusiness.setMerchantId(loginBean.getMerchantId());
                                mtDao.insertBusiness(mtBusiness);
                                log.info("MTBusiness info is {}", mtBusiness);
                            }

                        }
                    }
                }
                count++;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                businessStatistics(fromDate, endDate, true);
            }
        }
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_CRAWLER_SALE);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
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

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_CRAWLER_FLOW);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }

            String json = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/flowanalysis/flowgeneral/r/generalInfo?recentDays=%s&wmPoiId=%s&sortType=&sortValue=", days, accountInfo.wmPoiId));
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);

            List<Map<String, Object>> datas = parse.read("$.data.flowGeneralChartVoList", List.class);
            for (Map<String, Object> data : datas) {
                log.info("date {} exposureNum is {} ,visitNum is {} orderNum is {} ", data.get("date"), data.get("exposureNum"), data.get("visitNum"), data.get("orderNum"));
                MTAnalysis analysis = new MTAnalysis();
                analysis.setId(accountInfo.wmPoiId + "$" + data.get("date"));
                analysis.setDate(toDate((String) data.get("date")));
                analysis.setExposureNum((Integer) data.get("exposureNum"));
                analysis.setVisitNum((Integer) data.get("visitNum"));
                analysis.setShopName(accountInfo.name);
                analysis.setOrderNum((Integer) data.get("orderNum"));
                analysis.setMerchantId(loginBean.getMerchantId());
                mtDao.insertAnalysis(analysis);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                flowanalysis(days, true);
            }
        }
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_CRAWLER_FLOW);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
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

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_GOODS_SALE);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }


            try (CloseableHttpResponse execute = client.execute(new HttpGet(String.format("https://waimaieapp.meituan.com/bizdata/hotSales/data/download?startDate=%s&endDate=%s&type=count", fromDate, endDate)))) {
                Workbook hssfWorkbook = WorkbookFactory.create(execute.getEntity().getContent());
                Sheet sheet = hssfWorkbook.getSheetAt(0);
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        MTHotSales hotSales = new MTHotSales();
                        hotSales.setId(accountInfo.wmPoiId + "$" + row.getCell(0).getStringCellValue() + "$" + row.getCell(2).getStringCellValue());
                        hotSales.setDate(toDate(row.getCell(0).getStringCellValue()));
                        hotSales.setName(row.getCell(1).getStringCellValue());
                        hotSales.setProductName(row.getCell(2).getStringCellValue());
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        hotSales.setSellNum(NumberUtils.toInt(row.getCell(3).getStringCellValue(), 0));
                        hotSales.setSell(row.getCell(4).getStringCellValue());
                        hotSales.setPercentageNum(row.getCell(5).getStringCellValue());
                        hotSales.setPercentagePrice(row.getCell(6).getStringCellValue());
                        hotSales.setMerchantId(loginBean.getMerchantId());
                        mtDao.insertSales(hotSales);
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                hotSales(fromDate, endDate, true);
            }
        }
        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_GOODS_SALE);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
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

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_CRAWLER_EVALUATE);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }

            int i = 1;
            while (true) {
                String url = String.format("http://e.waimai.meituan.com/v2/customer/comment/r/list?wmPoiId=%s&acctId=%s&token=%s&rate=-1&reply=-1&context=-1&startDate=%s&endDate=%s&pageNum=%s", accountInfo.wmPoiId, accountInfo.acctId, accountInfo.accessToken, fromDate, endDate, i);
                String json = HttpClientUtil.executeGetWithResult(client, url);
                log.info("read json is {}", json);
                ReadContext parse = JsonPath.parse(json);
                int pageCount = parse.read("$.data.pageCount");
                log.info("pageCount is {}", pageCount);
                int commentSize = parse.read("$.data.comments.length()");
                for (int index = 0; index < commentSize; index++) {
                    MTComment mtComment = new MTComment();
                    String indexKey = "$.data.comments[" + index + "].";
                    String userName = parse.read(indexKey + "username");
                    mtComment.setId(accountInfo.wmPoiId + "$" + userName);
                    mtComment.setUserName(userName);
                    mtComment.setComment(parse.read(indexKey + "comment"));
                    mtComment.setCreateTime(toDate(parse.read(indexKey + "createTime")));
                    List<String> foods = parse.read(indexKey + "orderStatus..food_name", List.class);
                    mtComment.setFoods(StringUtils.join(foods, ","));
                    mtComment.setOrderCommentScore(parse.read(indexKey + "order_comment_score"));
                    mtComment.setShipScore(parse.read(indexKey + "ship_score"));
                    mtComment.setScoreMeaning(parse.read(indexKey + "scoreMeaning"));
                    mtComment.setPackagingScore(parse.read(indexKey + "packaging_score"));
                    mtComment.setTasteScore(parse.read(indexKey + "taste_score"));
                    mtComment.setName(accountInfo.name);
                    mtComment.setShipTime(parse.read(indexKey + "ship_time"));
                    mtComment.setMerchantId(loginBean.getMerchantId());
                    mtDao.insertComment(mtComment);
                    log.info("comment is {}", mtComment);
                }

                i++;
                if (i > pageCount + 1) break;
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                comment(fromDate, endDate, true);
            }
        }

        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_CRAWLER_EVALUATE);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
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

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_ORDER_CHECKING);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }

            //https://waimaieapp.meituan.com/finance/v2/finance/orderChecking/export/download//meituan_waimai_file_bill_export-2017-08-11-1029680.xls
            String url = String.format("https://waimaieapp.meituan.com/finance/pc/api/settleBillExport/billExportTask?beginDate=%s&endDate=%s", fromDate, endDate);
            String json = HttpClientUtil.executeGetWithResult(client, url);
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);
            int code = parse.read("$.code");
            String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            if (code == 2005) {
                log.info("not found taskNo is {}", json);
                String exportUrls = HttpClientUtil.executeGetWithResult(client, "https://waimaieapp.meituan.com/finance/pc/api/settleBillExport/settleBillExportList?pageNo=1&pageSize=10");
                parse = JsonPath.parse(exportUrls);
                url = "https://waimaieapp.meituan.com/finance/v2/finance/orderChecking/export/download" + parse.read("$.data.taskList[0].taskUrl");
            } else {
                int taskNo = parse.read("$.data.taskNo");
                String format = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
                url = String.format("https://waimaieapp.meituan.com/finance/v2/finance/orderChecking/export/download/meituan_waimai_file_bill_export-%s-%s.xls", format, taskNo);
            }
            log.info("excel url is {}", url);
            while (true) {

                try (CloseableHttpResponse execute = client.execute(new HttpGet(url))) {
                    Workbook workbook = WorkbookFactory.create(execute.getEntity().getContent());
                    Sheet sheet = workbook.getSheetAt(0);
                    if (sheet == null) {
                        TimeUnit.SECONDS.sleep(10);
                        log.info("sleep...");
                        continue;
                    }
                    for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                        Row row = sheet.getRow(rowNum);
                        if (row != null) {
                            MTBill mtBill = new MTBill();
                            mtBill.setName(row.getCell(0).getStringCellValue());
                            mtBill.setTradeType(row.getCell(1).getStringCellValue());
                            mtBill.setDesc(row.getCell(2).getStringCellValue());
                            mtBill.setPayType(row.getCell(3).getStringCellValue());
                            mtBill.setAppSeq(row.getCell(4).getStringCellValue());
                            mtBill.setAppNo(row.getCell(5).getStringCellValue());
                            mtBill.setId(accountInfo.wmPoiId + "$" + mtBill.getAppNo());
                            mtBill.setOrderCreateTime(toDate(row.getCell(6).getStringCellValue()));
                            mtBill.setDoneTime(toDate(row.getCell(7).getStringCellValue()));
                            mtBill.setRefundTime(toDate(row.getCell(8).getStringCellValue()));
                            mtBill.setOrderStatus(row.getCell(9).getStringCellValue());
                            mtBill.setShipType(row.getCell(10).getStringCellValue());
                            mtBill.setShipStatus(row.getCell(11).getStringCellValue());
                            mtBill.setSettleStatus(row.getCell(12).getStringCellValue());
                            mtBill.setPayDay(toDate(row.getCell(13).getStringCellValue()));
                            mtBill.setVestingDay(toDate(row.getCell(14).getStringCellValue()));
                            mtBill.setShopPrice(row.getCell(15).getStringCellValue());
                            mtBill.setTotalPrice(row.getCell(16).getStringCellValue());
                            mtBill.setPromotionPrice(row.getCell(17).getStringCellValue());
                            mtBill.setMeiTuanSubsidy(row.getCell(18).getStringCellValue());
                            mtBill.setServiceCharge(row.getCell(19).getStringCellValue());
                            mtBill.setShipPrice(row.getCell(20).getStringCellValue());
                            mtBill.setOnlinePrice(row.getCell(21).getStringCellValue());
                            mtBill.setOfflnePirce(row.getCell(22).getStringCellValue());
                            mtBill.setRate(row.getCell(23).getStringCellValue());
                            mtBill.setGuarantees(row.getCell(24).getStringCellValue());
                            mtBill.setDiscount(row.getCell(25).getStringCellValue());
                            mtBill.setMerchantId(loginBean.getMerchantId());
                            log.info("bill info is {}", mtBill);
                            mtDao.insertBill(mtBill);
                        }

                    }

                }
                break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                historySettleBillList(fromDate, endDate, true);
            }
        }

        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_ORDER_CHECKING);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
        }
    }

    //https://waimaieapp.meituan.com/reuse/activity/setting/r/listActs

    //活动管理
    public void acts(boolean isLogin) {
        try {
            CookieStore localCookie = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (localCookie == null || isLogin) {
                login();
            } else {
                cookieStore = localCookie;
            }

            //更新爬取状态为进行中
            int ii = crawlerStatusDao.updateStatusING(CrawlerName.MT_SALE_ACTIVITY);
            if (ii == 1) {
                log.info("更新爬取状态成功");
            } else {
                log.info("更新爬取状态失败");
            }

            Map<String, Object> params = Maps.newHashMap();
            params.put("wmPoiId", accountInfo.wmPoiId);
            String json = HttpClientUtil.executePostWithResult(client, "https://waimaieapp.meituan.com/reuse/activity/setting/r/listActs", params);
            log.info("read json is {}", json);
            ReadContext parse = JsonPath.parse(json);
            int endActCount = parse.read("$.data.endActs.length()");
            for (int endActIndex = 0; endActIndex < endActCount; endActIndex++) {
                String key = "$.data.endActs[" + endActIndex + "].";
                MTAct mtAct = new MTAct();
                mtAct.setName(accountInfo.name);
                mtAct.setIsEnd(1);
                mtAct.setActName(parse.read(key + "actName"));
                mtAct.setId(accountInfo.wmPoiId + "$" + mtAct.getActName() + "$" + parse.read(key + "actId"));
                mtAct.setStartTime(toDate(parse.read(key + "startTime")));
                mtAct.setEndTime(toDate(parse.read(key + "endTime")));
                mtAct.setContext(parse.read(key + "poiPolicy"));
                mtAct.setMerchantId(loginBean.getMerchantId());
                mtDao.insertAct(mtAct);
            }


            int goActCount = parse.read("$.data.onGoingActs.length()");
            for (int goActIndex = 0; goActIndex < goActCount; goActIndex++) {
                String key = "$.data.onGoingActs[" + goActIndex + "].";
                MTAct mtAct = new MTAct();
                mtAct.setName(accountInfo.name);
                mtAct.setIsEnd(0);
                mtAct.setActName(parse.read(key + "actName"));
                mtAct.setId(accountInfo.wmPoiId + "$" + mtAct.getActName() + "$" + parse.read(key + "actId"));
                mtAct.setStartTime(toDate(parse.read(key + "startTime")));
                mtAct.setEndTime(toDate(parse.read(key + "endTime")));
                mtAct.setContext(parse.read(key + "poiPolicy"));
                mtAct.setMerchantId(loginBean.getMerchantId());
                mtDao.insertAct(mtAct);
            }

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
                json = EntityUtils.toString(execute.getEntity());
                parse = JsonPath.parse(json);
                int count = parse.read("$.data.length()");
                for (int j = 0; j < count; j++) {
                    String key = "$.data[" + j + "]";
                    MTAct mtAct = new MTAct();
                    mtAct.setName(accountInfo.name);
                    mtAct.setIsEnd(0);
                    mtAct.setActName(parse.read(key + "name"));
                    mtAct.setId(accountInfo.wmPoiId + "$" + mtAct.getActName() + "$" + parse.read(key + "id"));
                    int st = parse.read(key + "activityStartTime");
                    int et = parse.read(key + "activityEndTime");
                    mtAct.setStartTime(new Date(st));
                    mtAct.setEndTime(new Date(et));
                    String activityType = parse.read(key + "activityType");
                    String activityRule = parse.read(key + "activityRule");
                    String activityIntroduction = parse.read(key + "activityIntroduction");
                    mtAct.setContext(html2Text(activityType) + "||" + html2Text(activityRule) + "||" + html2Text(activityIntroduction));
                    mtAct.setMerchantId(loginBean.getMerchantId());
                    mtDao.insertAct(mtAct);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            if (!isLogin) {
                acts(true);
            }
        }

        int f = crawlerStatusDao.updateStatusFinal(CrawlerName.MT_SALE_ACTIVITY);
        if (f == 1) {
            log.info("更新爬取状态成功");
        } else {
            log.info("更新爬取状态失败");
        }
    }

    public String html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        java.util.regex.Pattern p_html1;
        java.util.regex.Matcher m_html1;

        try {
            String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
            String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }


    @Data
    public static class LoginBean {
        private Authenticate authenticate;
        private String parkey = "";
        private String captchaCode = "";
        private String captchaVtoken = "";
        private Integer smsVerify = 0;
        private String smsCode = "";

        public String getMerchantId() {
            return authenticate.getMerchantId();
        }

        public String cookieStoreName() {
            return MD5.md5(authenticate.getUserName() + "_" + authenticate.getPassword()) + ".cookies";
        }


        public Map<String, Object> form() {
            Map<String, Object> values = Maps.newHashMap();
            values.put("login", authenticate.getUserName());
            values.put("password", authenticate.getPassword());
            values.put("park_key", parkey);
            values.put("captcha_code", captchaCode);
            values.put("captcha_v_token", captchaVtoken);
            values.put("sms_verify", smsVerify);
            values.put("sms_code", smsCode);
            return values;
        }

        public String json() {
            return JSON.toJSONString(form(), SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNonStringKeyAsString);
        }

    }

    @Data
    static class AccountInfo {
        private String cityId;
        private Integer wmPoiId;
        private Integer acctId;
        private String accessToken;
        private String brandId;
        private String name;
    }

    public static void main(String[] args) throws InterruptedException {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        LoginBean loginBean = new LoginBean();
        loginBean.setAuthenticate(authenticate);
        MTCrawler mtCrawler = new MTCrawler();
        mtCrawler.setLoginBean(loginBean);
        mtCrawler.login();
        //  mtCrawler.login(loginBean);
        //  mtCrawler.bizDataReport("2017-07-02", "2017-08-02", false);
        // mtCrawler.businessStatistics("20170707", "20170805", false);
        // mtCrawler.flowanalysis("30", false);
        //mtCrawler.hotSales("2017-07-31","2017-08-06",true);
        // mtCrawler.comment("2017-08-01", "2017-08-06", true);
        //  mtCrawler.historySettleBillList("2017-07-05", "2017-08-02", true);
        //mtCrawler.acts(true);
    }
}


