package com.xiaomi.ibook.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaomi
 * on 20/04/29 09:46:00
 */
@Slf4j
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        log.info("已经另一台机器登录，您被迫下线。");
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", 0);
        map.put("msg", "已经另一台机器登录，您被迫下线。" + event.getSessionInformation().getLastRequest());
        String json = objectMapper.writeValueAsString(map);
        event.getResponse().setContentType("application/json;charset=UTF-8");
        event.getResponse().getWriter().write(json);

        // 如果是跳转html页面，url代表跳转的地址
        // redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "url");
    }
}
