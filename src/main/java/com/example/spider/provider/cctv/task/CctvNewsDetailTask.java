package com.example.spider.provider.cctv.task;

import com.example.spider.provider.cctv.dto.CctvNewsItem;
import com.example.spider.task.AbstractSpiderTask;
import com.example.spider.task.Task;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 获取 cctv 新闻详情任务
 * @author ljh
 */
@Data
@Builder
public class CctvNewsDetailTask implements Task {

    private String url;

    private Pattern pattern;

    private Map<String, String> param;

    private CctvNewsItem item;

}
