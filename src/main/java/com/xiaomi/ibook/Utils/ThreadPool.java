package com.xiaomi.ibook.Utils;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiaomi
 * on 2020.04.16 09:16:00
 */
public enum  ThreadPool {

    INSTANCE;

    private ExecutorService es;

    private ThreadPool(){
        es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public ExecutorService getInstance(){
        if (es.isShutdown()) {
            es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        }
        return es;
    }
}
