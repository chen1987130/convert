package com.kao.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kao.convert.bean.Api;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author chensheng
 * @Description 自动转换启动类
 * @date 2019/04/17
 * @since V1.13
 */
public class AutoBuilder {


    public static void main(String[] args) throws Exception {
        File file = FileUtils.getFile(AutoProperties.FILE_PATH_INPUT);
        if (file.exists()) {
            Api api = HtmlResolve.parse(FileUtils.readFileToString(file, "utf-8"));
            String json = JSON.toJSONString(api.getList(), SerializerFeature.PrettyFormat);
            System.out.println(json);
            FileUtils.write(new File(AutoProperties.FILE_PATH_OUTPUT), json, "utf-8");
        }
    }

}
