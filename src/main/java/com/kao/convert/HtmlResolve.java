package com.kao.convert;

import com.kao.convert.bean.Api;
import com.kao.convert.bean.ApiDetail;
import com.kao.convert.bean.ApiProperties;
import com.kao.convert.bean.ApiTag;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

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
        //返回报文
        String response = apiEl.select(".highlight .json").text().trim();
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
        detail.setRes_body(response);
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
        List<ApiProperties> headerList = new ArrayList<>();
        List<ApiProperties> queryList = new ArrayList<>();
        apiDetail.setReq_headers(headerList);
        apiDetail.setReq_query(queryList);

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
        }

        String requestRaw = apiEl.select(".raw-area textarea").text().trim();
        if (StringUtils.isNotBlank(requestRaw)) {
            apiDetail.setReq_body_other(requestRaw);
        }
    }


}
