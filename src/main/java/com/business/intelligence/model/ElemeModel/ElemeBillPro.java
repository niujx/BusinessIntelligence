package com.business.intelligence.model.ElemeModel;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tcqq on 2017/12/19.
 */
@Data
public class ElemeBillPro {
    private List<LinkedHashMap<String, Object>> resultList;
    private Map<String ,Integer> map;
}
