package com.example.spider.schedule;

import com.example.spider.config.ThreadPoolConfiguration;
import com.example.spider.starter.SpiderStarter;
import com.example.spider.task.SpiderTaskQueue;
import com.example.spider.spider.Spider;
import com.example.spider.task.Task;
import com.example.spider.task.SpiderResultHandlerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 爬虫启动器
 * @author ljh
 */
@Slf4j
@Component
public class SpiderScheduled implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Spider spider;

    @Autowired
    private SpiderResultHandlerManager resultHandlerManager;

    @Autowired(required = false)
    private List<SpiderStarter> spiderStarters;

    @Autowired
    @Qualifier(ThreadPoolConfiguration.SPIDER)
    private ThreadPoolExecutor executor;

    private static final String PER_5S = "*/5 * * * * ?";

    /**
     * 每分钟执行
     */
    private static final String PER_1MIN = "0 * * * * ?";

    /**
     * 每天凌晨2点执行一次
     */
    private static final String WHEN_2_AM = "0 0 2 * * ?";

    /**
     * 每 5s 从任务队列中拿出所有任务放到队列
     */
    @Scheduled(cron = PER_5S)
    public void starSpider() {
        Task task = SpiderTaskQueue.getTask();
        if(task == null){
            log.debug("try getTask from queue, but nothing.");
        }else {
            log.debug("getTask success, num of surplus tasks:{}", SpiderTaskQueue.getSize());
        }
        while (task != null) {
            doSpider(task);
            task = SpiderTaskQueue.getTask();
        }
    }

    private void doSpider(Task task) {
        log.info("creat new SpiderTask({})", task.getUrl());
        executor.execute(() -> {
            resultHandlerManager.handle(task, spider.doSpider(task));
        });
    }

    /**
     * 每天凌晨2点启动所有爬虫任务
     */
    @Scheduled(cron = WHEN_2_AM)
    public void startAllSpiders(){
        if(!CollectionUtils.isEmpty(spiderStarters)){
            spiderStarters.forEach(SpiderStarter::startSpiders);
        }
    }


    /**
     * 第一次启动项目时，启动所有爬虫
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        startAllSpiders();
    }

}
