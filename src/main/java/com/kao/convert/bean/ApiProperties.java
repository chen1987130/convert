package com.kao.convert.bean;

/**
 * @author chensheng
 * @Description 接口属性
 * @date 2019/04/17
 * @since V1.13
 */
public class ApiProperties {

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性描述
     */
    private String desc;

    /**
     * 是否必填(0.非必填 1.必填)
     */
    private String required = "0";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }
}
