package com.example.spider.task;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 爬虫任务
 * @author ljh
 */
public interface Task {

    /**
     * 爬取地址
     */
    String getUrl();

    /**
     * 获取匹配内容的正则表达式
     */
    Pattern getPattern();

    /**
     * 请求参数
     */
    Map<String, String> getParam();

}
