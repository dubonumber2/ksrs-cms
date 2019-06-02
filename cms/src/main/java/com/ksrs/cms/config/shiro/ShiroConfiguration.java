package com.ksrs.cms.config.shiro;

import com.ksrs.cms.mapper.ResourcesMapper;
import com.ksrs.cms.model.Resources;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.service.ResourcesService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class ShiroConfiguration {


    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Resource
    ResourcesMapper resourcesMapper;

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfiguration.class);

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setUnauthorizedUrl("/cms/unauthorized");
        bean.setLoginUrl("/cms/unlogin");
        LinkedHashMap<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/index","authc");
        filterChainDefinitionMap.put("/cms/login","anon");
        filterChainDefinitionMap.put("/cms/unauthorized","anon");

        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger-resources","anon");
        filterChainDefinitionMap.put("/v2/api-docs","anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/font-awesome/**","anon");

        /*List<Menu> list = resourcesMapper.queryAll();
        for(Menu tmp : list){
            if(!tmp.getUrl().isEmpty()&&!tmp.getPermission().isEmpty()){
                logger.info("【注入拦截】url:{}",tmp.getUrl());
                filterChainDefinitionMap.put(tmp.getUrl(),tmp.getPermission());
            }

        }*/

        filterChainDefinitionMap.put("/**","authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return bean;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm, @Qualifier("sessionManager") DefaultWebSessionManager sessionManager){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        // 自定义缓存实现,集成redis
        manager.setCacheManager(cacheManager());
        manager.setSessionManager(sessionManager);

        return manager;
    }

    @Bean("authRealm")
    public AuthRealm authRealm(@Qualifier("credentialMatcher")CredentialMatcher matcher){
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(matcher);
        return authRealm;
    }

    @Bean("credentialMatcher")
    public CredentialMatcher credentialMatcher(){
        return new CredentialMatcher();
    }



    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(timeout);
        //redisManager.setPassword(password);
        redisManager.setDatabase(database);
        return redisManager;
    }

    public RedisCacheManager cacheManager(){
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(redisManager());
        return cacheManager;
    }
    @Bean("redisSessionDao")
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean("sessionManager")
    public MySessionManager sessionManager(@Qualifier("redisSessionDao") RedisSessionDAO redisSessionDAO){
        MySessionManager sessionManager = new MySessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return  creator;
    }
}
