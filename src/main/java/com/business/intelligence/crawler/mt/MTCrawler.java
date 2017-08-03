package com.business.intelligence.crawler.mt;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.crawler.baidu.CodeImage;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.util.CookieStoreUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
import com.business.intelligence.util.MD5;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import org.assertj.core.util.Lists;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.business.intelligence.util.HttpClientUtil.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
public class MTCrawler extends BaseCrawler {
    private String LOGIN_URL = "https://epassport.meituan.com/account/loginv2?service=waimai&continue=http://e.waimai.meituan.com/v2/epassport/entry&part_type=0&bg_source=3";
    private static CloseableHttpClient client;
    private CookieStore cookieStore = new BasicCookieStore();
    private LoginBean loginBean;
    private AccountInfo accountInfo;


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
                    log.info("de");
                    HttpPost loginS2 = new HttpPost("https://waimaie.meituan.com/v2/epassport/logon");
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
    public void bizDataReport(String fromDate, String endDate, Boolean isLogin) {
        try {
            cookieStore = CookieStoreUtils.readStore(loginBean.cookieStoreName());
            if (cookieStore == null || isLogin) {
                login();
            }
            client = HttpClientUtil.getHttpClient(cookieStore);
            String url1 = HttpClientUtil.executeGetWithResult(client, String.format("https://waimaieapp.meituan.com/bizdata/report/charts/download?wmPoiIdSel=2843062&fromDate=%s&toDate=%s&taskId=", fromDate, endDate));
            log.info("read json is {}", url1);

        } catch (IOException e) {
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

    public static void main(String[] args) {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");
        LoginBean loginBean = new LoginBean();
        loginBean.setAuthenticate(authenticate);

        MTCrawler mtCrawler = new MTCrawler(loginBean);

        //  mtCrawler.login(loginBean);
       // mtCrawler.bizDataReport();

    }
}


