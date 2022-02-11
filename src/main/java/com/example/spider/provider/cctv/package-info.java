package com.example.spider.provider.cctv;

/**
 * 爬取 cctv 网站的爬虫
 *
 * 1. 请求 http://news.cctv.com/2019/07/gaiban/cmsdatainterface/page/news_1.jsonp?cb=t&cb=news
 * 2. 获取 总数，当前数
 * 3. 创建页面详情爬虫任务，如果还有剩余数，则创建下一页的爬虫任务
 *
 */