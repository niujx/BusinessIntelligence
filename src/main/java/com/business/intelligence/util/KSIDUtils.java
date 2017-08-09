package com.business.intelligence.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;

/**
 * Created by Tcqq on 2017/8/9.
 * 用来持久化ksid
 */

@Slf4j
public class KSIDUtils {

    private static String storePath = "ElemeKsidStore";

    /**
     * 保存ksid
     */
    public static void storeKSID(String ksid ,String fileName){
        File file = new File(storePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        try (
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File(file, fileName)))) {
            objectOutputStream.writeObject(ksid);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * 提取ksid
     */
    public static String readKsid(String fileName){
        try (
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(storePath, fileName)))) {
            String ksid = (String) inputStream.readObject();
            return ksid;
        } catch (Exception e) {
            log.error("本地无ksid，或者在读取中出现错误");
        }
        return null;
    }



}
