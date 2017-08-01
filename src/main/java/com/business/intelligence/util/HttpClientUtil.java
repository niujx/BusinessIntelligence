package com.business.intelligence.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:32.0) Gecko/20100101 Firefox/33.0";

    public static HttpPost post(String url) {
        return post(url, null);
    }

    public static HttpPost post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    public static HttpPost post(String url, Map<String, Object> params, String userAgent) {
        HttpPost result = new HttpPost(url);
        result.addHeader("User-Agent", userAgent == null ? DEFAULT_USER_AGENT : userAgent);
        if (params != null && !params.isEmpty()) {
            result.setEntity(buildParams(params));
        }
        return result;
    }

    public static HttpGet get(String url) {
        return get(url, null);
    }

    public static HttpGet get(String url, Map<String, Object> params, String userAgent) {
        url += buildParamString(params);
        HttpGet result = new HttpGet(url);
        result.addHeader("User-Agent", userAgent == null ? DEFAULT_USER_AGENT : userAgent);
        return result;
    }

    public static HttpGet get(String url, Map<String, Object> params) {
        url += buildParamString(params);
        HttpGet result = new HttpGet(url);
        result.addHeader("User-Agent", DEFAULT_USER_AGENT);
        return result;
    }

    public static UrlEncodedFormEntity buildParams(Map<String, ? extends Object> params) {
        return buildParams(params, UTF_8);
    }

    @SuppressWarnings("rawtypes")
    public static UrlEncodedFormEntity buildParams(Map<String, ? extends Object> params, String encoding) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof List) {
                    for (Object o : (List) value) {
                        if (o != null) {
                            parameters.add(new BasicNameValuePair(entry.getKey(), o.toString()));
                        }
                    }
                } else {
                    parameters.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                }
            } else {
                parameters.add(new BasicNameValuePair(entry.getKey(), null));
            }
        }
        return new UrlEncodedFormEntity(parameters, Charset.forName(encoding));
    }

    public static String buildParamString(Map<String, ? extends Object> params) {
        return buildParamString(params, UTF_8);
    }

    public static String buildParamString(Map<String, ? extends Object> params, String encoding) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        try {
            for (Map.Entry<String, ? extends Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                value = value == null ? "" : value.toString();
                if(index == 0){
                    sb.append("?").append(URLEncoder.encode(entry.getKey(), encoding)).append("=")
                            .append(URLEncoder.encode((String) value, encoding));
                }else{
                    sb.append("&").append(URLEncoder.encode(entry.getKey(), encoding)).append("=")
                            .append(URLEncoder.encode((String) value, encoding));
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void executeGet(CloseableHttpClient client, String url) throws ClientProtocolException, IOException {
        HttpGet get = HttpClientUtil.get(url);
        client.execute(get).close();
    }

    public static String executeGetWithResult(CloseableHttpClient client, String url) throws ClientProtocolException,
            IOException {
        HttpGet get = get(url);
        CloseableHttpResponse resp = client.execute(get);
        String result = EntityUtils.toString(resp.getEntity());
        resp.close();
        return result;
    }

    public static String executeGetWithResult(CloseableHttpClient client, HttpGet get) throws ClientProtocolException,
            IOException {
        CloseableHttpResponse resp = client.execute(get);
        String result = EntityUtils.toString(resp.getEntity());
        resp.close();
        return result;
    }

    public static String executeGetWithResult(CloseableHttpClient client, HttpGet get, String encoding)
            throws ClientProtocolException, IOException {
        CloseableHttpResponse resp = client.execute(get);
        String result = EntityUtils.toString(resp.getEntity(), encoding);
        resp.close();
        return result;
    }

    public static void executePost(CloseableHttpClient client, String url) throws ClientProtocolException, IOException {
        executePost(client, url, null);
    }

    public static String executePostWithResult(CloseableHttpClient client, HttpPost post)
            throws ClientProtocolException, IOException {
        CloseableHttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity());
        resp.close();
        return result;
    }

    public static Map<String, Object> executePostWithHead(CloseableHttpClient client, HttpPost post)
            throws ClientProtocolException, IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        CloseableHttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity());
        map.put("result", result);
        map.put("url", getLocationFromHeader(resp));
        resp.close();
        return map;
    }

    public static String executePostWithResult(CloseableHttpClient client, HttpPost post, String encoding)
            throws ClientProtocolException, IOException {
        CloseableHttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity(), encoding);
        resp.close();
        return result;
    }

    public static String executePostWithResult(CloseableHttpClient client, String url, Map<String, Object> params)
            throws ClientProtocolException, IOException {
        return executePostWithResult(client, url, params, HttpClientUtil.UTF_8);
    }

    public static String executePostWithResult(CloseableHttpClient client, String url, Map<String, Object> params,
                                               String charset) throws ClientProtocolException, IOException {
        HttpPost post = params == null ? post(url) : post(url, params);
        CloseableHttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity(), charset);
        resp.close();
        return result;
    }

    public static String excuteResultPost(CloseableHttpClient client, HttpPost post, Object json)
            throws ClientProtocolException, IOException {
        StringEntity entity = new StringEntity(json.toString(), "utf-8");
        post.setEntity(entity);
        CloseableHttpResponse resp = client.execute(post);
        String result = EntityUtils.toString(resp.getEntity(), "UTF-8");
        resp.close();
        return result;
    }

    public static void executePost(CloseableHttpClient client, String url, Map<String, Object> params)
            throws ClientProtocolException, IOException {
        HttpPost post = params == null ? post(url) : post(url, params);
        client.execute(post).close();
    }

    public static String getLocationFromHeader(CloseableHttpResponse response) {
        return getLocationFromHeader(response, false);
    }

    public static String getLocationFromHeader(CloseableHttpResponse response, boolean closeResponse) {
        String result = getHeader(response, "Location");
        if (closeResponse) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String getLocationFromHeader(CloseableHttpClient client, String url) {
        return getLocationFromHeader(client, url, null, false);
    }

    public static String getLocationFromHeader(CloseableHttpClient client, String url, Map<String, Object> params) {
        return getLocationFromHeader(client, url, params, false);
    }

    public static String getLocationFromHeader(CloseableHttpClient client, String url, Map<String, Object> params,
                                               boolean isPost) {
        CloseableHttpResponse response;
        try {
            HttpPost request = null;
            if (isPost) {
                request = post(url, params);
                response = client.execute(request);

            } else {
                HttpGet get = get(url);
                response = client.execute(get);
            }
            return getLocationFromHeader(response, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHeader(CloseableHttpResponse response, String name) {
        Header[] headers = response.getHeaders(name);
        if (headers.length > 0) {
            return headers[0].getValue();
        }
        return null;
    }

    public static BasicClientCookie getCookie(String name, String value, String domain, String path) {
        BasicClientCookie clientCookie = new BasicClientCookie(name, value);
        clientCookie.setDomain(domain);
        clientCookie.setPath(path);
        return clientCookie;
    }

    public static CloseableHttpClient getHttpClient(CookieStore cookieStore) {
        HttpClientBuilder builder = getBuilder();
        if (cookieStore != null) {
            builder.setDefaultCookieStore(cookieStore);
        }
        return builder.build();
    }

    private static HttpClientBuilder getBuilder() {
        return getBuilder(SSLUtils.TRUAT_ALL_SSLSF);
    }

    private static HttpClientBuilder getBuilder(LayeredConnectionSocketFactory sslSocketFactory) {
        return getBuilder(sslSocketFactory, true);
    }

    private static HttpClientBuilder getBuilder(LayeredConnectionSocketFactory sslSocketFactory,
                                                boolean isHttpSameAsHttps) {
        HttpClientBuilder builder = HttpClients.custom();
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(1, false));// 重试一次

        // X509HostnameVerifier hostnameVerifier =
        // SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        // LayeredConnectionSocketFactory sslSocketFactory = new
        // SSLConnectionSocketFactory(SSLContexts.createDefault(),hostnameVerifier);
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslSocketFactory);
        // 联通的http不能使用代理
            registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());

        final PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(
                registryBuilder.build());
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        builder.setDefaultRequestConfig(requestConfigBuilder.build());
        builder.setConnectionManager(poolingmgr);
        // 链接保持策略
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                // keep alive for 120 seconds
                return 120 * 1000;
            }
        };
        builder.setKeepAliveStrategy(myStrategy);
        return builder;
    }
}
