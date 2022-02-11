package com.example.spider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author ljh
 */
@Configuration
public class ThreadPoolConfiguration {

    public static final String SPIDER = "spiderThreadPool";

    public static final String HANDLER = "handlerThreadPool";

    @Bean(SPIDER)
    public ThreadPoolExecutor spiderThreadPool(){
        return new ThreadPoolExecutor(30, 30, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(2000), new MyThreadFactory("SPIDER", true));
    }

    @Bean(HANDLER)
    public ThreadPoolExecutor handlerThreadPool(){
        return new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(2000), new MyThreadFactory("HANDLER", true));
    }


    public static class MyThreadFactory implements ThreadFactory {

        private final ThreadGroup threadGroup;
        private final AtomicLong threadSeq = new AtomicLong(1);
        private final String threadNamePrefix;
        private boolean daemon = true;

        public MyThreadFactory(String groupName, boolean isDaemon){
            this.daemon = isDaemon;
            this.threadNamePrefix = groupName;
            this.threadGroup = new ThreadGroup(groupName);
        }


        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(threadGroup, r, threadNamePrefix + threadSeq.getAndIncrement(), 0);
            t.setDaemon(daemon);
            //recovery thread priority
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }

    }


}
