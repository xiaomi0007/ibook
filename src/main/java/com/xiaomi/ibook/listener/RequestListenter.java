package com.xiaomi.ibook.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by xiaomi
 * on 20/04/27 12:17:00
 */

@WebListener
@Slf4j
public class RequestListenter implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    /**
     * 使用idea,这里有一个坑,点击红方块停止的服务,这个不生效,可以使用jar包的方式CTRL+C的停止测试
     *
     * @param sce 监听器
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
