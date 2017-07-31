package com.business.intelligence.crawler.mt;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.crawler.baidu.CodeImage;
import com.business.intelligence.model.Authenticate;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.JsonPath;
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

import static com.business.intelligence.util.HttpClientUtil.UTF_8;
import static java.util.stream.Collectors.toList;

@Slf4j
public class MTCrawler extends BaseCrawler {
    private String LOGIN_URL = "https://epassport.meituan.com/account/loginv2?service=waimai&continue=http://e.waimai.meituan.com/v2/epassport/entry&part_type=0&bg_source=3";
    private static CloseableHttpClient client;
    private CookieStore cookieStore = new BasicCookieStore();
    private ObjectMapper mapper = new ObjectMapper();


    public MTCrawler(Authenticate authenticate) {
        client = HttpClientUtil.getHttpClient(cookieStore);
        setAuthenticate(authenticate);
    }

    @Override
    public void doRun() {

    }


    public void login(LoginBean loginBean) {

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
            loginS1.setEntity(new UrlEncodedFormEntity(params, Charset.forName(UTF_8)));

            CloseableHttpResponse execute = client.execute(loginS1);
            if (execute.getStatusLine().getStatusCode() == 302) {
                Header location = execute.getFirstHeader("Location");
                String BSID = location.getValue().split("=")[1];
                log.info("find BSID is {}", BSID);
                for (int i = 0; i < 2; i++) {
                    HttpPost loginS2 = new HttpPost("http://e.waimai.meituan.com/v2/epassport/logon");
                    loginS2.setEntity(new UrlEncodedFormEntity(Lists.newArrayList(new BasicNameValuePair("BSID", BSID), new BasicNameValuePair("device_uuid", ""))));
                    CloseableHttpResponse execute1 = client.execute(loginS2);
                    log.info("{}", EntityUtils.toString(execute1.getEntity()));
                }
                log.info("login success");
            } else {
                //输入验证码重新登录
                String captchaJson = EntityUtils.toString(execute.getEntity());
                log.info("json is {}",captchaJson);
                String token = JsonPath.read(captchaJson, "$.error.captcha_v_token");
                String captchaImage = "https://epassport.meituan.com/bizverify/captcha?verify_event=1&captcha_v_token="+token+"&"+System.currentTimeMillis();
                String path = HttpUtil.getCaptchaCodeImage(client, new HttpGet(captchaImage));
                String captchaCode = CodeImage.Imgencode(path);
                log.info("get captchaCode is {} ",captchaCode);
                loginBean.setCaptchaVtoken(token);
                loginBean.setCaptchaCode(captchaCode);
                login(loginBean);
            }
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

    public static void main(String[] args) {
        Authenticate authenticate = new Authenticate();
        authenticate.setUserName("wmONEd46480");
        authenticate.setPassword("RHpXW72879");


        MTCrawler mtCrawler = new MTCrawler(authenticate);
        LoginBean loginBean = new LoginBean();
        loginBean.setAuthenticate(authenticate);
        mtCrawler.login(loginBean);

    }
}


