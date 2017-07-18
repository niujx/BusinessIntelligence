package com.business.intelligence.crawler.baidu;

import com.alibaba.fastjson.JSONObject;
import com.business.intelligence.util.CyCodeImageUtil;
import com.business.intelligence.util.HttpUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodeImage {
    private static final Logger logger = LoggerFactory.getLogger(CodeImage.class);
    private static final String URL = "http://upload.chaojiying.net/Upload/Processing.php";
    private static final String USERNAME = "xzg1993";
    private static final String PASSWORD = "147520heizi";
    private static final String SOFTID = "893655";
    private static final String CODETYPE = "1004";


    /**
     * 获取打码平台数据
     * @param imgPath 图片地址
     * @return
     */
    public static String Imgencode(String imgPath) {
        String imgCode = null;
        try {
            String content = CyCodeImageUtil.PostPic(USERNAME, PASSWORD, SOFTID, CODETYPE, "0", imgPath);
            if(content.contains("pic_str")){
                JSONObject json = JSONObject.parseObject(content);
                imgCode = json.getString("pic_str");
            }
        } catch (Exception e) {
           logger.error("获取打码平台数据出错",e);
        }
        return imgCode;
    }

    public static void main(String[] args) {
        String imgPath = "https://wmpass.baidu.com/wmpass/openservice/imgcaptcha?token=wBACUuTSFLc0RiLUJhUiVtbT94BngiCAYfZUFBQUFBQUEsGU2QfjoCgAH5AQAEUfwoEJDhRRQRFCkFBQUFBQWVxS0dEbzQyLyw4KE5ALWwlWQZlbEFBQUFBQUFBBW9vKShBbSsGdhFdX&t=1500020519597&color=3c78d8";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(imgPath);
        imgPath = HttpUtil.getCaptchaCodeImage(client, get);
        Imgencode(imgPath);
    }
}
