package com.business.intelligence.crawler.eleme;

import com.business.intelligence.crawler.BaseCrawler;
import com.business.intelligence.model.ElemeModel.ElemeBean;
import com.business.intelligence.util.CookieStoreUtils;
import com.business.intelligence.util.HttpClientUtil;
import com.business.intelligence.util.KSIDUtils;
import com.business.intelligence.util.WebUtils;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.xml.ws.WebEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tcqq on 2017/7/18.
 */
@Slf4j
public abstract class ElemeCrawler extends BaseCrawler{
    protected  CookieStore cookieStore = new BasicCookieStore();
    protected CloseableHttpClient client;

   public ElemeCrawler(){
        if(client==null){
            client = HttpClientUtil.getHttpClient(cookieStore);
        }
    }

    //登录
    protected String username;
    protected String password;
    protected String shopId;
    protected String merchantId;
    private static final String LOGINURL = "https://app-api.shop.ele.me/arena/invoke/?method=LoginService.loginByUsername";

    //爬取数据需要的
    protected String ksId;
//    //登出
//    private static final String LOGOUTURL = "https://melody.shop.ele.me/app/shop/150148671/stats/business";
    //测试Cookie的url
    private static final String URL="https://app-api.shop.ele.me/shop/invoke/?method=shopLiveVideo.getShopVideoDevices";
    private static final String INDEX = "https://melody.shop.ele.me/login";
    //测试ksid的url
    private static final String KSIDURL = "https://app-api.shop.ele.me/marketing/invoke/?method=applyActivityManage.getApplyActivity";


    /**
     * 登录
     */
    protected String login() {
        HttpGet httpGetfirst = new HttpGet(INDEX);
//        try {
//            client.execute(httpGetfirst);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        CloseableHttpResponse httpResponse = null;
        String content = null;
        HttpPost httppost = new HttpPost(LOGINURL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"2bbb7b48-c428-4158-b30d-78dc93a8e6f1\",\"method\":\"loginByUsername\",\"service\":\"LoginService\",\"params\":{\"username\":\""+username+"\",\"password\":\""+password+"\",\"captchaCode\":\"\",\"loginedSessionIds\":[]},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        httppost.setEntity(jsonEntity);
        httppost.setHeader("Content-type", "application/json;charset=utf-8");
        httppost.setHeader("Host", "app-api.shop.ele.me");
        httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        httppost.setHeader("Accept", "*/*");
        httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        httppost.setHeader("Accept-Encodinge", "gzip, deflate, br");
        httppost.setHeader("X-Shard", "shopid="+shopId+"");
        httppost.setHeader("X-Eleme-RequestID", "2bbb7b48-c428-4158-b30d-78dc93a8e6f1");
        httppost.setHeader("Referer", "https://melody.shop.ele.me/login");
        httppost.setHeader("origin", "https://melody.shop.ele.me");
        httppost.setHeader("Connection", "keep-alive");
        try{
            httpResponse = client.execute(httppost);
            HttpEntity entity = httpResponse.getEntity();
            content = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                log.info(content);
//                try{
//                    String errorMessage =(String) WebUtils.getOneByJsonPath(content, "$.result.failureData.errorMessage");
//                    if(errorMessage.equals("图片验证码不能为空")){
//                        //补充图片验证码登录
//
//                    }
//                }catch (PathNotFoundException em){
//                    log.info("无需验证码");
//                }
                try{
                    ksId = (String)WebUtils.getOneByJsonPath(content, "$.result.successData.ksid");
                    log.info("登陆成功");
                }catch(PathNotFoundException e){
                    log.info("登录失败，请检查用户名、密码、shopID是否正确");
                    return null;
                }
            }
            if(ksId != null){
//                HttpGet httpGet = new HttpGet(INDEX);
//                client.execute(httpGet);
                //保存ksid
                log.info("开始保存 {} 的ksid",username);
                KSIDUtils.storeKSID(ksId,getKsidName(username,password));
                //保存Cookie
                log.info("开始保存 {} 的cookie",username);
                CookieStoreUtils.storeCookie(cookieStore,getCookieName(username,password));
            }
        }catch (IOException e){
            e.printStackTrace();
            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                if(httpResponse != null){
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ksId;

    }

    /**
     * 拼接保存cookie的名字
     * @param userName
     * @param password
     * @return
     */
    public String getCookieName(String userName,String password){
        StringBuilder sb = new StringBuilder();
        sb.append("eleme")
                .append("_")
                .append(userName)
                .append("_")
                .append(password);
        log.info("cookie name is {}",sb.toString());
        return sb.toString();
    }

    /**
     * 拼接保存ksid的名字
     * @param userName
     * @param password
     * @return
     */
    public String getKsidName(String userName,String password){
        StringBuilder sb = new StringBuilder();
        sb.append("elemeKsid")
                .append("_")
                .append(userName)
                .append("_")
                .append(password);
        log.info("ksid name is {}",sb.toString());
        return sb.toString();
    }

    /**
     * 提取Cookie和ksid，没有则登录
     */
    public CloseableHttpClient getClient(ElemeBean elemeBean){
        //将前台带来的信息全局化
        this.username = elemeBean.getUsername();
        this.password = elemeBean.getPassword();
        this.shopId = elemeBean.getShopId();
        this.merchantId = elemeBean.getShopPri() == null? "":elemeBean.getShopPri();
        if(username == null | password == null | shopId == null){
            log.info("用户名、密码、shopID其中有空值，程序跳过");
            return null;
        }
        //导入本地ksid
        String oldKsid = KSIDUtils.readKsid(getKsidName(username,password));
//        CookieStore oldcookieStore = CookieStoreUtils.readStore(getCookieName(username,password));
//        if (oldcookieStore == null) {
//            log.info("no cookie , so login");
//            login();
//        }else {
//            client = HttpClientUtil.getHttpClient(oldcookieStore);
            if(oldKsid == null ){
                log.info("no ksid,so login");
                String login = login();
                if(login == null){
                    return null;
                }
            }else{
                LinkedHashMap testResult = ksidIsOk(client, oldKsid);
                if(testResult == null ){
                    this.ksId = oldKsid;
                    log.info("无需登录");
                }else{
                    log.info("ksid 测试结果错误为 {},所以进行登录",testResult);
                    login();
                }
            }
//        }

        return client;
    }

    /**
     * 测试Cookie是否有效
     * @param client
     */
    public int cookieIsOk(CloseableHttpClient client){
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(URL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"25f9f5c3-ac95-4fa4-80bb-1bdd0fad0f6e\",\"method\":\"getShopVideoDevices\",\"service\":\"shopLiveVideo\",\"params\":{\"shopId\":"+shopId+"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+ksId+"\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "25f9f5c3-ac95-4fa4-80bb-1bdd0fad0f6e");
        try {
            execute = client.execute(post);
            return execute.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (execute != null){
                    execute.close();
                }
                if(client != null){
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 测试ksid是否有效
     * @param client
     * @param oldKsid
     * @return
     */
    public LinkedHashMap ksidIsOk(CloseableHttpClient client,String oldKsid){
        log.info("测试ksid： {} 是否有效",oldKsid);
        CloseableHttpResponse execute = null;
        HttpPost post = new HttpPost(KSIDURL);
        StringEntity jsonEntity = null;
        String json = "{\"id\":\"9368dd8a-a6e9-4e6c-855c-3d2e29ff3498\",\"method\":\"getApplyActivity\",\"service\":\"applyActivityManage\",\"params\":{\"shopId\":"+shopId+"},\"metas\":{\"appName\":\"melody\",\"appVersion\":\"4.4.0\",\"ksid\":\""+oldKsid+"\"},\"ncp\":\"2.0.0\"}";
        jsonEntity = new StringEntity(json, "UTF-8");
        post.setEntity(jsonEntity);
        setElemeHeader(post);
        post.setHeader("X-Eleme-RequestID", "9368dd8a-a6e9-4e6c-855c-3d2e29ff3498");
        try {
            execute = client.execute(post);
            HttpEntity entity = execute.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");
            log.info("测试ksid的结果： {}",result);
            LinkedHashMap ksidTestResult = (LinkedHashMap)WebUtils.getOneByJsonPath(result, "$.error");
            return ksidTestResult;
        } catch (IOException e) {
            log.error("测试ksid时发生未知错误，不登录");
            e.printStackTrace();
        }finally {
            try {
                if (execute != null){
                    execute.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     *登出
     */
//    public void logOut(CloseableHttpClient client){
//        CloseableHttpResponse execute = null;
//        HttpGet get = new HttpGet(LOGOUTURL);
//        get.setHeader("Cookie","perf_ssid=x0ruhdmzvgqd999uv3wkznb7ett7bnbl_2017-07-20; ubt_ssid=umald6k97vny7o9pxha8pye7mxu8ijzt_2017-07-20; _utrace=8105d34303396caf9f636842a6e511b9_2017-07-20; _ga=GA1.2.1285076203.1501035869");
//        try {
//            execute = client.execute(get);
//            HttpEntity entity = execute.getEntity();
//            String result = EntityUtils.toString(entity, "UTF-8");
//            System.out.println(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if (execute != null){
//                    execute.close();
//                }
//                if(client != null){
//                    client.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 设置饿了么网页的header
     */
    protected void setElemeHeader(HttpPost post){
        post.setHeader("Content-type", "application/json;charset=utf-8");
        post.setHeader("Host", "app-api.shop.ele.me");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
        post.setHeader("Accept", "*/*");
        post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        post.setHeader("Accept-Encodinge", "gzip, deflate, br");
        post.setHeader("X-Shard", "shopid="+shopId+"");
        post.setHeader("Referer", "https://melody-stats.faas.ele.me/");
        post.setHeader("origin", "https://melody-stats.faas.ele.me");
        post.setHeader("Connection", "keep-alive");
    }

    /**
     * 对字符串为null进行处理
     */
    public String notNull(String str){
        return str == null ? "":str;
    }

}


