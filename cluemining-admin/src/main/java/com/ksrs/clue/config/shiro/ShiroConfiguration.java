package com.ksrs.clue.config.shiro;

import com.ksrs.clue.model.Resources;
import com.ksrs.clue.service.ResourcesService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class ShiroConfiguration {

    @Resource
    ResourcesService resourcesService;

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

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setUnauthorizedUrl("/clue/unauthorized");
        bean.setLoginUrl("/clue/unlogin");

        LinkedHashMap<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/index","authc");
        filterChainDefinitionMap.put("/clue/login","anon");
        filterChainDefinitionMap.put("/clue/unauthorized","anon");
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger-resources","anon");
        filterChainDefinitionMap.put("/v2/api-docs","anon");
        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
        filterChainDefinitionMap.put("/css/**","anon");
        filterChainDefinitionMap.put("/js/**","anon");
        filterChainDefinitionMap.put("/img/**","anon");
        filterChainDefinitionMap.put("/font-awesome/**","anon");
        List<Resources> resourcesList = resourcesService.queryAll();
       /* if(!CollectionUtils.isEmpty(resourcesList)){
            for(Resources tmp:resourcesList){
                // 将数据库中的权限加入过滤链
                filterChainDefinitionMap.put(tmp.getResurl(),"rest["+tmp.getResurl()+"]");
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
        redisManager.setHost("r-8vb1a343a29637a4.redis.zhangbei.rds.aliyuncs.com");
        redisManager.setPort(6379);
        redisManager.setTimeout(1800);
        /*redisManager.setHost("10.0.0.96");
        redisManager.setPort(6379);
        redisManager.setTimeout(1800);*/
        redisManager.setPassword("Re2017sRsKSFP38dis");
        // test 3 dev 2
        redisManager.setDatabase(2);
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
