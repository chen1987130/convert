package com.kao.convert.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author chensheng
 * @Description 工具类
 * @date 2019/04/18
 * @since V1.13
 */
public class JSONUtils {

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            JSON.parse(value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是json数组
     */
    public static boolean isArray(String value) {
        try {
            Object object = JSON.parse(value);
            if (object instanceof JSONArray) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断是否是json对象
     */
    public static boolean isObject(String value) {
        try {
            Object object = JSON.parse(value);
            if (object instanceof JSONObject) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
