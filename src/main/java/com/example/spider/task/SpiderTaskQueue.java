package com.example.spider.task;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 任务队列
 * @author ljh
 */
public class SpiderTaskQueue {

    private static BlockingDeque<Task> blockingDeque = new LinkedBlockingDeque<>(10_000);

    public static boolean putTask(@NonNull Task task){
        return blockingDeque.offer(task);
    }

    @Nullable
    public static Task getTask(){
        return blockingDeque.poll();
    }

    public static int getSize(){
        return blockingDeque.size();
    }


}
