package com.kao.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kao.convert.bean.*;
import com.kao.convert.util.FieldUtils;
import com.kao.convert.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chensheng
 * @Description Apizz-Html解析类
 * @date 2019/04/17
 * @since V1.13
 */
public class HtmlResolve {

    public static Api parse(String html) {
        Document doc = Jsoup.parse(html);
        Api api = new Api();
        //解析分类
        parseTag(doc, api);
        //解析接口
        parseApi(doc, api);
        return api;
    }

    /**
     * 解析分类信息
     */
    public static void parseTag(Document doc, Api api) {
        List<ApiTag> tags = new ArrayList<>();
        Elements tagEls = doc.select(".container .nav li span");
        tagEls.eachText().forEach(data -> {
            ApiTag tag = new ApiTag();
            tag.setName(data);
            tags.add(tag);
        });
        api.setList(tags);
    }

    /**
     * 解析接口
     */
    public static void parseApi(Document doc, Api api) {
        Elements mainEls = doc.select(".container .docs_main .block-doc-one");
        for (Element mainEl : mainEls) {
            String tag = "";
            int i = 1;
            for (Element children : mainEl.children()) {
                //分类
                if ("h1".equals(children.nodeName())) {
                    tag = children.text().trim();
                }
                //接口
                else if ("div".equals(children.nodeName()) && children.hasClass("api-one")) {
                    if (!"附录".equals(tag)) {
                        ApiDetail apiDetail = parseApiDetails(children, i);
                        if (apiDetail != null) {
                            for (ApiTag apiTag : api.getList()) {
                                if (StringUtils.equals(apiTag.getName(), tag)) {
                                    apiTag.getList().add(apiDetail);
                                }
                            }
                            i++;
                        }
                    }
                }
            }
        }
    }

    public static ApiDetail parseApiDetails(Element apiEl, Integer index) {
        ApiDetail detail = new ApiDetail();
        //接口名称
        String apiName = apiEl.select("h3").text().trim();
        //请求地址
        String apiUrl = apiEl.select("p").get(2).text().trim();
        //详细说明
        String desc = apiEl.select(".detail-info").html().trim();

        String method = apiEl.child(2).text();
        if ("请求方式：GET".equals(method)) {
            method = "GET";
        } else {
            method = "POST";
        }

        if (apiUrl.indexOf("//") > 0) {
            apiUrl = apiUrl.substring(apiUrl.indexOf("//") + 2);
        }
        if (apiUrl.indexOf("}}") > 0) {
            apiUrl = apiUrl.substring(apiUrl.indexOf("}}") + 2);
        }
        if (StringUtils.isBlank(apiUrl)) {
            return null;
        }

        detail.setIndex(index);
        detail.setMethod(method);
        detail.setTitle(apiName);
        detail.setPath(apiUrl);
        detail.setDesc(desc);
        parseApiRequest(apiEl, detail);
        return detail;
    }

    /**
     * 解析请求参数
     *
     * @param apiEl     接口Html
     * @param apiDetail 接口明细
     */
    public static void parseApiRequest(Element apiEl, ApiDetail apiDetail) {
        //返回报文
        String response = apiEl.select(".highlight .json").text().trim();
        List<ApiProperties> headerList = new ArrayList<>();
        List<ApiProperties> queryList = new ArrayList<>();
        apiDetail.setReq_headers(headerList);
        apiDetail.setReq_query(queryList);

        Map<String, String> reqParam = new HashMap<>();
        Map<String, String> respParam = new HashMap<>();

        Elements tables = apiEl.select("table");
        for (Element table : tables) {
            String title = table.select(".label-success").text();
            if ("Header参数名".equals(title)) {
                Elements headers = table.select("tbody tr");
                for (Element header : headers) {
                    String name = header.child(0).text().trim();
                    String required = header.child(2).text().trim();
                    String desc = header.child(3).text().trim();

                    ApiProperties properties = new ApiProperties();
                    properties.setName(name);
                    properties.setDesc(desc);
                    if ("是".equals(required)) {
                        properties.setRequired("1");
                    } else {
                        properties.setRequired("0");
                    }
                    headerList.add(properties);
                }
            }

            if ("Query参数名".equals(title)) {
                Elements headers = table.select("tbody tr");
                for (Element header : headers) {
                    String name = header.child(0).text().trim();
                    String required = header.child(2).text().trim();
                    String desc = header.child(3).text().trim();

                    ApiProperties properties = new ApiProperties();
                    properties.setName(name);
                    properties.setDesc(desc);
                    if ("是".equals(required)) {
                        properties.setRequired("1");
                    } else {
                        properties.setRequired("0");
                    }
                    queryList.add(properties);
                }
            }

            //request请求参数说明
            if ("Body参数名".equals(title)) {
                Elements headers = table.select("tbody tr");
                for (Element header : headers) {
                    String name = header.child(0).text().trim();
                    String desc = header.child(3).text().trim();
                    reqParam.put(name, desc);
                }
            }

            //返回值请求参数说明
            if ("参数名".equals(title)) {
                Elements headers = table.select("tbody tr");
                for (Element header : headers) {
                    String name = header.child(0).text().trim();
                    String desc = header.child(1).text().trim();
                    respParam.put(name, desc);
                }
            }
        }

        String requestRaw = apiEl.select(".raw-area textarea").text().trim();
        if (StringUtils.isNotBlank(requestRaw) && JSONUtils.isJson(requestRaw)) {
            ApiField field = parseApiField(requestRaw, reqParam);
            apiDetail.setReq_body_type("json");
            apiDetail.setReq_body_other(JSON.toJSONString(field));
        } else {
            apiDetail.setReq_body_type("raw");
            apiDetail.setReq_body_other(StringUtils.trimToEmpty(requestRaw));
        }


        if (StringUtils.isNotBlank(response) && JSONUtils.isJson(response)) {
            ApiField field = parseApiField(response, respParam);
            apiDetail.setRes_body_type("json");
            apiDetail.setRes_body(JSON.toJSONString(field));
        } else {
            apiDetail.setRes_body_type("raw");
            apiDetail.setRes_body(StringUtils.trimToEmpty(response));
        }
    }

    public static ApiField parseApiField(String json, Map<String, String> paramMap) {
        Object obj = JSON.parse(json);
        ApiField apiField = new ApiField();
        apiField.setSchema(AutoProperties.SCHEMA);
        if (obj instanceof JSONObject) {
            apiField.setType(AutoProperties.FieldType.OBJECT);
            parseApiObject(apiField, (JSONObject) obj, paramMap, "");
        } else if (obj instanceof JSONArray) {
            apiField.setType(AutoProperties.FieldType.ARRAY);
            parseApiArray(apiField, (JSONArray) obj, paramMap, "");
        }
        return apiField;
    }


    public static void parseApiObject(ApiField apiField, JSONObject jsonObject, Map<String, String> paramMap, String prefix) {
        Map<String, ApiField> properties = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getValue() != null) {
                ApiField child = new ApiField();
                String nPrefix = prefix + entry.getKey();
                if (paramMap.get(nPrefix) != null) {
                    child.setDescription(paramMap.get(nPrefix));
                }

                String fieldType = FieldUtils.getFieldType(entry.getValue());
                if (AutoProperties.FieldType.STRING.equals(fieldType)) {
                    child.setType(AutoProperties.FieldType.STRING);
                } else if (AutoProperties.FieldType.BOOLEAN.equals(fieldType)) {
                    child.setType(AutoProperties.FieldType.BOOLEAN);
                } else if (AutoProperties.FieldType.NUMBER.equals(fieldType)) {
                    child.setType(AutoProperties.FieldType.NUMBER);
                } else if (AutoProperties.FieldType.OBJECT.equals(fieldType)) {
                    child.setType(AutoProperties.FieldType.OBJECT);
                    parseApiObject(child, (JSONObject) entry.getValue(), paramMap, nPrefix + ".");
                } else if (AutoProperties.FieldType.ARRAY.equals(fieldType)) {
                    child.setType(AutoProperties.FieldType.ARRAY);
                    parseApiArray(child, (JSONArray) entry.getValue(), paramMap, nPrefix + ".");
                }
                properties.put(entry.getKey(), child);
            }
        }
        apiField.setProperties(properties);
    }

    public static void parseApiArray(ApiField apiField, JSONArray jsonArray, Map<String, String> paramMap, String prefix) {
        ApiField items = new ApiField();
        if (jsonArray != null && jsonArray.size() > 0) {
            Object object = jsonArray.get(0);
            if (object != null) {
                String fieldType = FieldUtils.getFieldType(object);
                if (AutoProperties.FieldType.STRING.equals(fieldType)) {
                    items.setType(AutoProperties.FieldType.STRING);
                } else if (AutoProperties.FieldType.BOOLEAN.equals(fieldType)) {
                    items.setType(AutoProperties.FieldType.BOOLEAN);
                } else if (AutoProperties.FieldType.NUMBER.equals(fieldType)) {
                    items.setType(AutoProperties.FieldType.NUMBER);
                } else if (AutoProperties.FieldType.OBJECT.equals(fieldType)) {
                    items.setType(AutoProperties.FieldType.OBJECT);
                    parseApiObject(items, (JSONObject) object, paramMap, prefix);
                } else if (AutoProperties.FieldType.ARRAY.equals(fieldType)) {
                    items.setType(AutoProperties.FieldType.ARRAY);
                    parseApiArray(items, (JSONArray) object, paramMap, prefix);
                }
            }
        }
        apiField.setItems(items);
    }


}
