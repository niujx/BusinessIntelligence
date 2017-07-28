package com.business.intelligence.crawler.eleme;

import com.business.intelligence.crawler.BaseCrawler;
import eleme.openapi.sdk.config.Config;
import eleme.openapi.sdk.oauth.response.Token;
import lombok.Setter;

/**
 * Created by Tcqq on 2017/7/18.
 */
public abstract class ElemeCrawler extends BaseCrawler{
    @Setter
    protected Token token = getToken();
    @Setter
    protected Config config = getConfig();

    private static final boolean ISSANDBOX= true;
    private static final String APPKEY = "l6kCfCKiDI";
    private static final String APPSECRET = "dc24f210a1ca72364b726b230c5672115d87f6bf";
    private static final String ACCESSTOKEN = "d6962561958e53cd6dbb724d38e8ec12";
    private static final String TOKENTYPE = "Bearer";
    private static final long EXPIRES = 86400;
    private static final String REFRESHTOKEN = "695aea84c6b186a10d1f32dfe9a0d858";

    private Token getToken(){
        Token token = new Token();
        token.setAccessToken(ACCESSTOKEN);
        token.setTokenType(TOKENTYPE);
        token.setExpires(EXPIRES);
        token.setRefreshToken(REFRESHTOKEN);
        return token;
    }

    private Config getConfig(){
        return new Config(ISSANDBOX,APPKEY,APPSECRET);
    }

}
