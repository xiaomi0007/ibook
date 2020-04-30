package com.xiaomi.ibook.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by xiaomi
 * on 20/04/26 10:08:00
 */
@Component
@Slf4j
public class StartRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("服务器已启动,开始初始化参数");
        log.info("参数保存位置:" + System.getProperty("user.dir"));

    }


}

