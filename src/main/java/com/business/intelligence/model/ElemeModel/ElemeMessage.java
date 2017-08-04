package com.business.intelligence.model.ElemeModel;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Tcqq on 2017/8/3.
 */
@Data
@Component
public class ElemeMessage {
    //获取的标签列表
    private List<LinkedHashMap<String, Object>> list;
    //获取的原始json串
    private String json;

    @Override
    public String toString() {
        return "ElemeMessage{" +
                "list=" + list +
                ", json='" + json + '\'' +
                '}';
    }
}
