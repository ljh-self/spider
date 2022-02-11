package com.example.spider.factory;

import com.example.spider.task.Task;

/**
 * 爬虫任务工厂
 */
public interface TaskFactory {

    Task createTask();

}
