package com.example.spider.provider.cctv.handler;

import com.example.spider.provider.cctv.CctvNewsListTaskFactory;
import com.example.spider.provider.cctv.dto.CctvNewsItem;
import com.example.spider.provider.cctv.dto.CctvNewsList;
import com.example.spider.provider.cctv.task.CctvNewsDetailTask;
import com.example.spider.provider.cctv.task.CctvNewsListTask;
import com.example.spider.task.SpiderResultHandler;
import com.example.spider.task.Task;
import com.example.spider.task.SpiderTaskQueue;
import com.example.spider.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 处理新闻列表
 *
 * @author ljh
 */
@Slf4j
@Component
public class CctvNewsListResultHandler implements SpiderResultHandler {

    @Override
    public boolean canHandle(Task task, String result) {
        return task instanceof CctvNewsListTask && !StringUtils.isEmpty(result);
    }

    /**
     * 分析列表中的新闻，创建详情任务
     */
    @Override
    public void handle(Task task, String result) {

        log.info("prepare handing CctvNewsList" + task.getUrl());
        if(!result.startsWith("news(")){
            // 爬完了都
            log.warn("not a valid result. May finished!");
        }
        String prefix = "news({\"data\":";
        String suffix = "})";
        // 去掉前后缀
        String json = result.substring(prefix.length(), result.length() - suffix.length());

        // 转为json
        CctvNewsList newsList = JsonUtils.toObject(json, CctvNewsList.class);

        List<CctvNewsItem> items = newsList.getList();
        if(CollectionUtils.isEmpty(items)){
            log.warn("items is empty");
            return;
        }
        // 创建爬取详情的任务
        items.forEach(this::publishDetailTask);

        // 创建爬取下一页的任务
        CctvNewsListTask cctvNewsListTask = (CctvNewsListTask)task;
        CctvNewsListTask nextPageTask = CctvNewsListTaskFactory.createTask(cctvNewsListTask.getPage() + 1);
        SpiderTaskQueue.putTask(nextPageTask);
        log.info("finished handle CctvNewsList" + task.getUrl());
    }

    /**
     * 发布爬取一篇新闻详情的任务
     */
    private void publishDetailTask(CctvNewsItem item){
        CctvNewsDetailTask task = CctvNewsDetailTask.builder()
                .url(item.getUrl())
                .item(item)
                .build();
        SpiderTaskQueue.putTask(task);
    }

}
