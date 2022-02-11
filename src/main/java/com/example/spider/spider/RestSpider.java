package com.example.spider.spider;

import com.example.spider.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 使用restTemplate 的爬虫
 * @author ljh
 */
@Slf4j
@Component
public class RestSpider implements Spider {

    @Autowired
    private RestTemplate rest;

    @Override
    public String doSpider(Task task) {
        log.info("starting spider To " + task.getUrl());
        String url = task.getUrl();
        String result = rest.getForObject(url, String.class);
        log.info("finished spider To " + task.getUrl());
        return result;
    }




}
