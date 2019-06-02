package com.ksrs.clue.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class HttpAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    // 形成一个横切贯入点
    @Pointcut("execution(public * com.ksrs.clue.controller..*.*(..))")
    public void log(){

    }
    // 再发起请求之前执行该方法
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        // url
        // 客户端ip
        // 请求的方法
        // 请求参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logger.info("sessionId={}",request.getHeader("Authorization"));
        logger.info("url={}",request.getRequestURL());
        logger.info("method={}",request.getMethod());
        logger.info("ip={}",request.getRemoteAddr());
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        StringBuilder builder = new StringBuilder(new String("args="));
        for(int i=0;i<args.length;i++){
            builder.append("{},");
        }
        logger.info(builder.toString(),joinPoint.getArgs());

    }
    // 在日志中打印出请求响应的结果
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturning(Object object){

        logger.info("response={}", JSON.toJSONString(object));
    }
}
