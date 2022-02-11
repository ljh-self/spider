package com.example.spider.task;

/**
 * 爬虫结果处理器
 * @author ljh
 */
public interface SpiderResultHandler {

    /**
     * 返回能否处理
     */
    boolean canHandle(Task task, String result);

    /**
     * 处理结果
     * @param result 页面信息
     */
    void handle(Task task, String result);

}
