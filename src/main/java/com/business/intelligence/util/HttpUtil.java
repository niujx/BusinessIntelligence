package com.business.intelligence.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    static String proxyHost = "127.0.0.1";
    static int proxyPort = 8087;

    @Autowired
    private YmlConfig config;

    public  static String doGets(String url){
        CloseableHttpClient httpRequest = HttpClients.createDefault();
        String content=null;
        HttpGet httppost = new HttpGet(url);
        try {
            httppost.setHeader(HttpMethodParams.SO_TIMEOUT,"600000");
            httppost.setHeader("Content-type", "application/json;charset=utf-8");
            logger.info("request: " + httppost.getURI());
            CloseableHttpResponse httpResponse = httpRequest.execute(httppost);
            try {
                HttpEntity entity = httpResponse.getEntity();
                content = EntityUtils.toString(entity, "UTF-8");
                if (entity != null) {
                    logger.info("response: " + content);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            if (httpRequest != null) {
                try {
                    httpRequest.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return content;
    }

    /**
     * <p>
     * 发送GET请求
     *
     * @param url GET请求地址
     * @return 与当前请求对应的响应内容字节数组
     */
    public static byte[] doGet(String url) {
        return HttpUtil.doGet(url, null, null, 0);
    }

    /**
     * <p>
     * 发送GET请求
     *
     * @param url       GET请求地址
     * @param headerMap GET请求头参数容器
     * @return 与当前请求对应的响应内容字节数组
     */
    public static byte[] doGet(String url, Map headerMap) {
        return HttpUtil.doGet(url, headerMap, null, 0);
    }

    /**
     * <p>
     * 发送GET请求
     *
     * @param url       GET请求地址
     * @param proxyUrl  代理服务器地址
     * @param proxyPort 代理服务器端口号
     * @return 与当前请求对应的响应内容字节数组
     */
    public static byte[] doGet(String url, String proxyUrl, int proxyPort) {
        return HttpUtil.doGet(url, null, proxyUrl, proxyPort);
    }

    /**
     * <p>
     * 发送GET请求
     *
     * @param url       GET请求地址
     * @param headerMap GET请求头参数容器
     * @param proxyUrl  代理服务器地址
     * @param proxyPort 代理服务器端口号
     * @return 与当前请求对应的响应内容字节数组
     */
    public static byte[] doGet(String url, Map headerMap, String proxyUrl, int proxyPort) {
        byte[] content = null;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        if (headerMap != null) {
            // 头部请求信息
            if (headerMap != null) {
                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    getMethod.addRequestHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        }
        if (StringUtils.isNotBlank(proxyUrl)) {
            httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
        }
        // 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
        // postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER ,
        // new DefaultHttpMethodRetryHandler());
        InputStream inputStream = null;
        try {
            if (httpClient.executeMethod(getMethod) == HttpStatus.SC_OK) {
                // 读取内容
                inputStream = getMethod.getResponseBodyAsStream();
                content = IOUtils.toByteArray(inputStream);
            } else {
                logger.info("Method failed: " + getMethod.getStatusLine());
            }
        } catch (IOException ex) {
            logger.info(ex.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
            getMethod.releaseConnection();
        }
        return content;
    }

    /**
     * <p>
     * 发送POST请求
     *
     * @param url          POST请求地址
     * @param parameterMap POST请求参数容器
     * @return 与当前请求对应的响应内容字节数组
     */
    public static String doPost(String url, Map parameterMap) {
        return HttpUtil.doPost(url, null, parameterMap, "UTF-8", null, 0);
    }

    /**
     * <p>
     * 发送POST请求
     *
     * @param url          POST请求地址
     * @param parameterMap POST请求参数容器
     * @param paramCharset 参数字符集名称
     * @return 与当前请求对应的响应内容字节数组
     */
    public static String doPost(String url, Map parameterMap, String paramCharset) {
        return HttpUtil.doPost(url, null, parameterMap, paramCharset, null, 0);
    }

    /**
     * <p>
     * 发送POST请求
     *
     * @param url          POST请求地址
     * @param headerMap    POST请求头参数容器
     * @param parameterMap POST请求参数容器
     * @param paramCharset 参数字符集名称
     * @return 与当前请求对应的响应内容字节数组
     */
    public static String doPost(String url, Map headerMap, Map parameterMap, String paramCharset) {
        return HttpUtil.doPost(url, headerMap, parameterMap, paramCharset, null, 0);
    }

    /**
     * <p>
     * 发送POST请求
     *
     * @param url          POST请求地址
     * @param parameterMap POST请求参数容器
     * @param paramCharset 参数字符集名称
     * @param proxyUrl     代理服务器地址
     * @param proxyPort    代理服务器端口号
     * @return 与当前请求对应的响应内容字节数组
     */
    public static String doPost(String url, Map parameterMap, String paramCharset, String proxyUrl, int proxyPort) {
        return HttpUtil.doPost(url, null, parameterMap, paramCharset, proxyUrl, proxyPort);
    }

    /**
     * <p>
     * 发送POST请求
     *
     * @param url          POST请求地址
     * @param headerMap    POST请求头参数容器
     * @param parameterMap POST请求参数容器
     * @param paramCharset 参数字符集名称
     * @param proxyUrl     代理服务器地址
     * @param proxyPort    代理服务器端口号
     * @return 与当前请求对应的响应内容字节数组
     */
    public static String doPost(String url, Map headerMap, Map parameterMap, String paramCharset, String proxyUrl,
                                int proxyPort) {
        String result = "";
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        if (StringUtils.isNotBlank(paramCharset)) {
            postMethod.getParams().setContentCharset(paramCharset);
            postMethod.getParams().setHttpElementCharset(paramCharset);
        }
        if (headerMap != null) {
            // 头部请求信息
            if (headerMap != null) {
                Iterator iterator = headerMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();
                    postMethod.addRequestHeader(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        }
        Iterator iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            postMethod.addParameter(key, JSONObject.toJSONString(parameterMap.get(key)));
        }
        if (StringUtils.isNotBlank(proxyUrl)) {
            httpClient.getHostConfiguration().setProxy(proxyUrl, proxyPort);
        }
        // 设置成了默认的恢复策略，在发生异常时候将自动重试3次，在这里你也可以设置成自定义的恢复策略
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 600000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(600000);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(600000);
        // postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER ,
        // new DefaultHttpMethodRetryHandler());
        InputStream inputStream = null;
        try {
            if (httpClient.executeMethod(postMethod) == HttpStatus.SC_OK) {
                // 读取内容
                inputStream = postMethod.getResponseBodyAsStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                logger.info("Method failed: " + postMethod.getStatusLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            postMethod.releaseConnection();
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url     发送请求的 URL
     * @param param   请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy 是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, boolean isproxy) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            if (isproxy) {// 使用代理模式
                @SuppressWarnings("static-access")
                Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                conn = (HttpURLConnection) realUrl.openConnection(proxy);
            } else {
                conn = (HttpURLConnection) realUrl.openConnection();
            }
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // POST方法

            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.info("发送 POST 请求出现异常！" + e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取忽略证书验证的client
     *
     * @return
     * @throws Exception
     */

    public CloseableHttpClient getIgnoeSSLClient() throws Exception {
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();

        //创建httpClient
        CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext).
                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        return client;
    }

    public static String sendHttpPost(String url, List<NameValuePair> formparams) {
        CloseableHttpClient httpRequest = HttpClients.createDefault();
        String content=null;
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            httppost.setHeader(HttpMethodParams.SO_TIMEOUT,"600000");
            logger.info("request: " + httppost.getURI());
            CloseableHttpResponse httpResponse = httpRequest.execute(httppost);
            try {
                HttpEntity entity = httpResponse.getEntity();
                content = EntityUtils.toString(entity, "UTF-8");
                if (entity != null) {
                    logger.info("response: " + content);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            if (httpRequest != null) {
                try {
                    httpRequest.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return content;

    }

    /**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public static String sendHttpPost(String url, String json) {
        CloseableHttpClient httpRequest = HttpClients.createDefault();
        String content=null;
        HttpPost httppost = new HttpPost(url);
        StringEntity jsonEntity = null;
        try {
            jsonEntity = new StringEntity(json, "UTF-8");
            httppost.setEntity(jsonEntity);
            httppost.setHeader(HttpMethodParams.SO_TIMEOUT,"600000");
            httppost.setHeader("Content-type", "application/json;charset=utf-8");
            logger.info("request: " + httppost.getURI());
            CloseableHttpResponse httpResponse = httpRequest.execute(httppost);
            try {
                HttpEntity entity = httpResponse.getEntity();
                content = EntityUtils.toString(entity, "UTF-8");
                if (entity != null) {
                    logger.info("response: " + content);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                httpResponse.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            if (httpRequest != null) {
                try {
                    httpRequest.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return content;

    }

    /**
     * 存储图片，并获取图片地址
     * @param client
     * @param get
     * @return
     */
    public static String getCaptchaCodeImage(CloseableHttpClient client, HttpGet get) {
        logger.info("验证码图片url:" + get.getURI().toString());
        try {
            CloseableHttpResponse response = client.execute(get);
//            File codeFile = new File(config.getImgPath(), System.currentTimeMillis() + ".jpg");
            File codeFile = new File("/Users/wangfukun/other/img", System.currentTimeMillis() + ".jpg");
            String path = codeFile.getPath();
            logger.info("图片验证码存储地址："+path);
            FileUtils.copyInputStreamToFile(response.getEntity().getContent(), codeFile);
            response.close();
            return path;
        } catch (Exception e) {
            logger.error("获取验证码图片失败", e);
        }
        return null;
    }

}
