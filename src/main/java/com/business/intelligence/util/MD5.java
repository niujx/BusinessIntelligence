package com.business.intelligence.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

public class MD5 {

    private static final Logger logger = LoggerFactory.getLogger(MD5.class);

    /**
     *
     * 校验密码，密码采用MD5算法加密。
     *
     * @Param PasswordInput, 待校验密码
     * @Return 校验通过返回true，否则返回false
     *
     *
     */
    public static String md5(String parm) {

        MessageDigest md;
        String pwd = "";
        // 生成一个MD5加密计算摘要
        try {
            md = MessageDigest.getInstance("MD5");

            // 计算md5函数
            // md.update(parm.getBytes());
            
            // md.digest(parm.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            // String pwd = new BigInteger(1, md.digest()).toString(16);
            pwd = getHexString(md.digest(parm.getBytes()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return pwd;

    }

    public static String getHexString(byte[] b) throws Exception {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
