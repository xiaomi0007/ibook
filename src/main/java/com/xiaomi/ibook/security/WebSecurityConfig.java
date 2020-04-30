package com.xiaomi.ibook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by xiaomi
 * on 20/04/28 14:04:00
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CustomUserDetailsService userDetailsService;

    @Resource
    private DataSource dataSource;

    @Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Resource
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/static/**","**/favicon.ico").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll()
                .and().logout().permitAll()
                .and().rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailsService)
                .and().sessionManagement()
                .invalidSessionUrl("/login/invalid")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy())
                .sessionRegistry(sessionRegistry());
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 设置拦截忽略文件夹，可以对静态资源放行
        web.ignoring().antMatchers("/css/**", "/js/**");
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public RoleHierarchy roleHierarchy() {
        String separator = System.lineSeparator();
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_USER " + separator + " ROLE_USER > ROLE_TOURISTS";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    /**
     * 登录时验证规则,如果不加密登录,则注释掉这块
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                //配置 UserDetailsService 实现类，实现自定义登录校验
                .userDetailsService(userDetailsService)
                //配置密码加密规则
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 密码加密，必须为 @Bean ，否则报错
     * 作用：实例化密码加密规则，该规则首先会校验数据库中存储的密码是否符合其规则（经过 BCryptPasswordEncoder 加密的密码
     * 的字符串符合一定的规则）：
     * 1.若不符合，直接报错；
     * 2.若符合，则会将前端传递的密码经过 BCryptPasswordEncoder 加密，再和数据库中的密码进行比对，一样则通过
     * 所以，这里要求，我们存入进数据库的密码不能是明文，而必须是经过 BCryptPasswordEncoder 加密后，才能存入数据库
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        //不加密登录方式使用以下
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return charSequence.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return s.equals(charSequence.toString());
//            }
//        };
    }

}
