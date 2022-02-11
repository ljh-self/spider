package com.example.spider.task;

import com.example.spider.config.ThreadPoolConfiguration;
import com.example.spider.task.SpiderResultHandler;
import com.example.spider.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 新浪结果处理器
 * @author ljh
 */
@Slf4j
@Service
public class SpiderResultHandlerManager {

    @Qualifier(ThreadPoolConfiguration.HANDLER)
    @Autowired
    private ThreadPoolExecutor executor;

    private final List<SpiderResultHandler> resultHandlers;

    public SpiderResultHandlerManager(List<SpiderResultHandler> resultHandlers) {
        this.resultHandlers = resultHandlers;
    }

    public void handle(Task task, String result) {
        if(resultHandlers == null){
            return;
        }
        log.info("handle spiderTask " + task.getUrl());
        executor.execute(() -> {
            // 责任链
            resultHandlers.forEach(handler -> {
                if(handler.canHandle(task, result)){
                    handler.handle(task, result);
                }
            });
        });
        log.info("finish handle spiderTask " + task.getUrl());
    }


}
