package com.example.spider.spider;

import com.example.spider.task.Task;

/**
 * 爬虫
 * @author ljh
 */
public interface Spider {

    /**
     * 进行爬虫
     */
    String doSpider(Task task);

}
