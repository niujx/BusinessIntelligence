package com.business.intelligence.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myYml")
public class YmlConfig {

    /**
     * 存放百度图片验证码路径
     */
    private String imgPath;

    /**
     * 存放百度下载csv路径
     */
    private String csvPath;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getCsvPath() {
        return csvPath;
    }

    public void setCsvPath(String csvPath) {
        this.csvPath = csvPath;
    }
}
