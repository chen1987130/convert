package com.kao.convert.bean;

import java.util.List;

/**
 * @author chensheng
 * @Description 接口数据
 * @date 2019/04/18
 * @since V1.13
 */
public class Api {

    /**
     * 接口分类
     */
    private List<ApiTag> list;

    public List<ApiTag> getList() {
        return list;
    }

    public void setList(List<ApiTag> list) {
        this.list = list;
    }
}
