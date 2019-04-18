package com.kao.convert.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

/**
 * @author chensheng
 * @Description 字段属性
 * @date 2019/04/18
 * @since V1.13
 */
public class ApiField {

    @JSONField(name = "$schema")
    private String schema;

    /**
     * 字段类型object、array、string、boolean、number
     */
    private String type;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 对象子元素
     */
    private Map<String, ApiField> properties;

    /**
     * 数组子元素字段类型
     */
    private ApiField items;

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ApiField> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, ApiField> properties) {
        this.properties = properties;
    }

    public ApiField getItems() {
        return items;
    }

    public void setItems(ApiField items) {
        this.items = items;
    }
}
