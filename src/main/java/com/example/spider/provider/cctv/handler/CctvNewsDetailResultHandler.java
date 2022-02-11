package com.example.spider.provider.cctv.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import com.example.spider.provider.cctv.task.CctvNewsDetailTask;
import com.example.spider.task.SpiderResultHandler;
import com.example.spider.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 处理 cctv 新闻详情
 * @author ljh
 */
@Slf4j
@Component
public class CctvNewsDetailResultHandler implements SpiderResultHandler {

    @Override
    public boolean canHandle(Task task, String result) {
        return task instanceof CctvNewsDetailTask;
    }

    private Pattern pattern = Pattern.compile("<!--repaste.body.begin-->(.*?)<!--repaste.body.end-->", Pattern.DOTALL);

    private Pattern paragraphPattern = Pattern.compile("<p>(.*?)</p>");


    /**
     * 保存到数据库
     */
    @Override
    public void handle(Task task, String result) {
        log.info("prepare handing CctvNewsDetail" + task.getUrl());
        // 获取正文
        List<String> contextMatchList = ReUtil.findAll(pattern, result, 1);
        if(CollectionUtils.isEmpty(contextMatchList)){
            log.error("cctvNewsDetail invalid. Task:{}", task);
            return;
        }
        String context = contextMatchList.get(0);

        // 获取正文中所有文字段落
        List<String> paragraphMatchList = ReUtil.findAll(paragraphPattern, context, 1);
        if(CollectionUtils.isEmpty(paragraphMatchList)){
            log.error("cctvNewsDetail invalid(paragraph match fail).");
            return;
        }

        // 将未处理的字符处理 todo 优化：搞成 map 形式 	&divide; ÷ lt < rt >
        for (int i = 0; i < paragraphMatchList.size(); i++) {
            paragraphMatchList.set(i, paragraphMatchList.get(i)
                    // 单引号转义
                    .replaceAll("&lsquo;", "‘")
                    .replaceAll("&rsquo;", "’")
                    // 双引号转义
                    .replaceAll("&ldquo;", "“")
                    .replaceAll("&rdquo;", "”")

                    // 破折号转义
                    .replaceAll("&mdash;", "——")
                    // 空格转义
                    .replaceAll("&nbsp;", " ")
                    // 省略号
                    .replaceAll("&hellip;", "...")
                    .replaceAll("&middot;", "·")
                    .replaceAll("&times;", "×")
                    .replaceAll("&permil;", "‰")

                    // 去掉加粗
                    .replaceAll("<strong>", "")
                    .replaceAll("</strong>", "")
            );
            if(paragraphMatchList.get(i).contains("&")){
                System.out.println(paragraphMatchList.get(i));
            }
        }

        // 保存到文件
        CctvNewsDetailTask detailTask = (CctvNewsDetailTask)task;
        String title = detailTask.getItem().getTitle();
        title = StringUtils.isEmpty(title) ? UUID.randomUUID().toString().replace("-", "") : title;
        if(title.contains("/")){
            title = title.replaceAll("/", "or")
                    .replaceAll("\\\\", "or");
            log.warn("title contains '\\\\' or '/', changed to 'or'");
        }
        String fileName = "F:/spider/cctv/" + title + ".txt";
        log.debug("prepare save to file: {}" + fileName);
        File file = FileUtil.touch(fileName);
        FileUtil.writeLines(paragraphMatchList, file, StandardCharsets.UTF_8);

        log.info("finished handle CctvNewsDetail" + task.getUrl());
    }
}
