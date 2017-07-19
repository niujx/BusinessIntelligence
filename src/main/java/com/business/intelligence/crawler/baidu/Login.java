package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.util.DateUtils;
import com.business.intelligence.util.Extracter;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.HttpUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Configuration
@EnableAsync
public class Login {

    private static final Logger log = LoggerFactory.getLogger(Login.class);

    private static String token;

    private static CloseableHttpClient client;

    private static CookieStore cookieStore = new BasicCookieStore();

    public Login() {
        if (client == null) {
            client = HttpClientUtil.getHttpClient(cookieStore);
        }

    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param passWord 密码
     * @param captcha  图片验证码（解析后的）
     */
    public static void logins(String userName, String passWord, String captcha) {
        String loginUrl = "https://wmpass.baidu.com/api/login";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "1");
        map.put("channel", "pc");
        map.put("account", userName);
        map.put("upass", passWord);
        map.put("captcha", captcha);
        map.put("token", token);
        map.put("redirect_url", "https://wmcrm.baidu.com/");
        map.put("return_url", "https://wmcrm.baidu.com/crm/setwmstoken");
        HttpPost post = HttpClientUtil.post(loginUrl, map);
        String content = null;
        try {
            content = HttpClientUtil.executePostWithResult(client, post);
            log.info("登录时返回内容：" + content);
            JSONObject json = JSONObject.parseObject(content);
            JSONObject data = json.getJSONObject("data");
            String return_url = data.getString("return_url");
            HttpGet rget = HttpClientUtil.get(return_url);
            content = HttpClientUtil.executeGetWithResult(client, rget);
            log.info("登录后返回内容1：" + content);
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/");

            String shouye = "https://wmcrm.baidu.com/crm?qt=neworderlist";
            rget = HttpClientUtil.get(shouye);
            content = HttpClientUtil.executeGetWithResult(client, rget);
            if (content.contains("百度商户")) {
                loadBills("","");
            }
            log.info("登录后返回内容2：" + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一下载
     */
    private static void loadBills(String startTime,String endTime) {
        if(StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            startTime = DateUtils.formatDate(new Date(),"yyyyMMdd");
            endTime =  DateUtils.formatDate(DateUtils.someDayAgo(1),"yyyyMMdd");
        }
        dowShopdata();
        dowShophotsaledish();
    }

    /**
     * 下载曝光数据
     *
     * @return
     */
    @Async
    private static void dowShopdata() {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm?qt=shopdata");
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm/shopdata?display=json&type=1&startTime=20170707&endTime=20170713&act=export");
            dwd.addHeader("referer", "https://wmcrm.baidu.com/crm?qt=shopdata");
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            dwd.addHeader("accept-language", "zh-CN,zh;q=0.8");
            CloseableHttpResponse response = client.execute(dwd);
            InputStream in = response.getEntity().getContent();
            File file = new File("/Users/wangfukun/other/img/");
            File target = new File(file.getParentFile(), file.getName() + System.currentTimeMillis() + ".csv");
            FileUtils.copyInputStreamToFile(in, target);
        } catch (IOException e) {
            log.error("下载曝光数据失败：" + e);
        }
    }

    /**
     * 下载热销菜品
     *
     * @return
     */
    @Async
    private static void dowShophotsaledish() {
        try {
            HttpClientUtil.executeGet(client, "https://wmcrm.baidu.com/crm?qt=shophotsaledish");
            HttpGet dwd = HttpClientUtil.get("https://wmcrm.baidu.com/crm?qt=exportshophotsaledishtask&from=pc&start_time=2017-06-18&end_time=2017-07-17&orderby=xl&display=json&");
            dwd.addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            dwd.addHeader("accept-encoding", "gzip, deflate, br");
            CloseableHttpResponse response = client.execute(dwd);
            InputStream in = response.getEntity().getContent();
            File file = new File("/Users/wangfukun/other/img/");
            File target = new File(file.getParentFile(), file.getName() + System.currentTimeMillis() + ".csv");
            FileUtils.copyInputStreamToFile(in, target);
        } catch (IOException e) {

        }
    }

    /**
     * 获取图片验证token
     *
     * @return
     */
    public static void getToken() {
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
            log.error("获取图片验证token出错：" + e);
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
            log.error("获取加密密码失败：" + e);
        }
        return upass;
    }

    public static void main(String[] args) {
        getToken();
        CloseableHttpClient httpRequest = HttpClients.createDefault();
        String url = "https://wmpass.baidu.com/wmpass/openservice/imgcaptcha?token=" + token + "&t=" + System.currentTimeMillis() + "&color=3c78d8";
        HttpGet httpget = new HttpGet(url);
        String path = HttpUtil.getCaptchaCodeImage(client, httpget);
        String imgCode = CodeImage.Imgencode(path);
        String pwd = getPassWord("wang170106");
        log.info("密码：" + pwd);
        ;
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入验证码：");
        String img = scanner.nextLine();
        log.info("图片验证码" + img);
        logins("twfhscywjd", pwd, img);
    }

}
