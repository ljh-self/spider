package com.example.spider.provider.cctv;

import com.example.spider.starter.SpiderStarter;
import com.example.spider.task.SpiderTaskQueue;
import org.springframework.stereotype.Component;

/**
 * @author ljh
 */
@Component
public class CctvNewsSpiderStarter implements SpiderStarter {

    @Override
    public void startSpiders(){
        SpiderTaskQueue.putTask(CctvNewsListTaskFactory.createTask(1));
    }

}
