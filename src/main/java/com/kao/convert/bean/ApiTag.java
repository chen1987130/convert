package com.kao.convert.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chensheng
 * @Description 项目接口分类
 * @date 2019/04/17
 * @since V1.13
 */
public class ApiTag {

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 接口列表
     */
    private List<ApiDetail> list = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ApiDetail> getList() {
        return list;
    }

    public void setList(List<ApiDetail> list) {
        this.list = list;
    }
}
