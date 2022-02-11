package com.example.spider.provider.cctv;

import com.example.spider.provider.cctv.task.CctvNewsListTask;

/**
 * 新浪新闻爬虫任务生成器
 * @author ljh
 */
public class CctvNewsListTaskFactory {

    public static CctvNewsListTask createTask(int pageNo) {
        String urlTemplate = "http://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_%d.jsonp?cb=t&cb=news";
        return CctvNewsListTask.builder()
                .url(String.format(urlTemplate, pageNo))
                .page(pageNo)
                .build();
    }

}
