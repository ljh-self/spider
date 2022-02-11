package com.example.spider.task;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 获取 cctv 新闻列表任务
 * @author ljh
 */
public abstract class AbstractSpiderTask implements Task {

    private String url;

    private Pattern pattern;

    private Map<String, String> param;

}
