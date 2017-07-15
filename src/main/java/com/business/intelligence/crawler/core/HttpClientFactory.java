package com.business.intelligence.crawler.core;

import com.google.common.base.Charsets;
import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Created by yanshi on 2017/7/15.
 */
public class HttpClientFactory {

    public static HttpClient create() {

        ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Charsets.UTF_8)
                .build();

        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setDefaultMaxPerRoute(1);
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultSocketConfig(socketConfig);

        return HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler())
                .setConnectionManager(connectionManager).build();
    }


}
