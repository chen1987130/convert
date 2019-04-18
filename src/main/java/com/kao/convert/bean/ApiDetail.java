package com.kao.convert.bean;

import java.util.List;

/**
 * @author chensheng
 * @Description 接口详情
 * @date 2019/04/17
 * @since V1.13
 */
public class ApiDetail {

    /**
     * 完成情况
     */
    private String status = "done";

    private String type = "static";

    private Boolean req_body_is_json_schema = true;

    private Boolean res_body_is_json_schema = true;

    private Boolean api_opened = false;

    /**
     * 排序
     */
    private Integer index;

    /**
     * 标签
     */
    private String[] tag;

    /**
     * 方法（POST|GET）
     */
    private String method;

    /**
     * 接口名称
     */
    private String title;

    /**
     * 接口路径
     */
    private String path;

    /**
     * 接口说明
     */
    private String desc;

    /**
     * 请求数据格式
     */
    private String req_body_type = "raw";

    /**
     * 返回数据格式
     */
    private String res_body_type = "raw";

    /**
     * headers请求参数
     */
    private List<ApiProperties> req_headers;

    /**
     * query请求参数
     */
    private List<ApiProperties> req_query;

    /**
     * body请求参数
     */
    private String req_body_other;

    /**
     * 返回报文
     */
    private String res_body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getReq_body_is_json_schema() {
        return req_body_is_json_schema;
    }

    public void setReq_body_is_json_schema(Boolean req_body_is_json_schema) {
        this.req_body_is_json_schema = req_body_is_json_schema;
    }

    public Boolean getRes_body_is_json_schema() {
        return res_body_is_json_schema;
    }

    public void setRes_body_is_json_schema(Boolean res_body_is_json_schema) {
        this.res_body_is_json_schema = res_body_is_json_schema;
    }

    public Boolean getApi_opened() {
        return api_opened;
    }

    public void setApi_opened(Boolean api_opened) {
        this.api_opened = api_opened;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReq_body_type() {
        return req_body_type;
    }

    public void setReq_body_type(String req_body_type) {
        this.req_body_type = req_body_type;
    }

    public String getRes_body_type() {
        return res_body_type;
    }

    public void setRes_body_type(String res_body_type) {
        this.res_body_type = res_body_type;
    }

    public List<ApiProperties> getReq_headers() {
        return req_headers;
    }

    public void setReq_headers(List<ApiProperties> req_headers) {
        this.req_headers = req_headers;
    }

    public List<ApiProperties> getReq_query() {
        return req_query;
    }

    public void setReq_query(List<ApiProperties> req_query) {
        this.req_query = req_query;
    }

    public String getReq_body_other() {
        return req_body_other;
    }

    public void setReq_body_other(String req_body_other) {
        this.req_body_other = req_body_other;
    }

    public String getRes_body() {
        return res_body;
    }

    public void setRes_body(String res_body) {
        this.res_body = res_body;
    }
}
