package com.xiaomi.ibook.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * <p>描述: [类型描述] </p>
 * <p>创建时间: 2019-11-29 </p>
 *
 * @author xiaomi
 */
@Configuration
@Slf4j
public class DruidConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.data-username}")
    private String username;

    @Value("${spring.datasource.data-password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.dbcp2.test-while-idle}")
    private String testWhileIdle;

    @Value("${spring.datasource.dbcp2.validation-query}")
    private String validationQuery;

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        datasource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
        datasource.setValidationQuery(validationQuery);
        return datasource;
    }

    @Bean
    public ServletRegistrationBean druidStatViewServlet(){
        ServletRegistrationBean servletRegistration = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        servletRegistration.addInitParameter("allow","127.0.0.1");
        servletRegistration.addInitParameter("loginUsername",username);
        servletRegistration.addInitParameter("loginPassword",password);
        servletRegistration.addInitParameter("resetEnable",Boolean.FALSE.toString());
        return servletRegistration;
    }

    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions","*.gif,*.png,*.jpg,*.js,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
