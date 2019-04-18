package com.kao.convert.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kao.convert.AutoProperties;

import java.math.BigDecimal;

/**
 * @author chensheng
 * @Description
 * @date 2019/04/18
 * @since V1.13
 */
public class FieldUtils {

    public static String getFieldType(Object obj) {
        if (obj instanceof JSONObject) {
            return AutoProperties.FieldType.OBJECT;
        }
        if (obj instanceof JSONArray) {
            return AutoProperties.FieldType.ARRAY;
        }
        if (obj instanceof String) {
            return AutoProperties.FieldType.STRING;
        }
        if (obj instanceof Integer) {
            return AutoProperties.FieldType.NUMBER;
        }
        if (obj instanceof Long) {
            return AutoProperties.FieldType.NUMBER;
        }
        if (obj instanceof Float) {
            return AutoProperties.FieldType.NUMBER;
        }
        if (obj instanceof BigDecimal) {
            return AutoProperties.FieldType.NUMBER;
        }
        if (obj instanceof Boolean) {
            return AutoProperties.FieldType.BOOLEAN;
        }
        System.out.println("未匹配到值类型:" + obj.getClass());
        throw new RuntimeException("未匹配到值类型");
    }
}
