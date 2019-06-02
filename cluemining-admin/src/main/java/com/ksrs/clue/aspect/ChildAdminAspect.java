package com.ksrs.clue.aspect;

import com.alibaba.fastjson.JSON;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.exception.SysException;
import com.ksrs.clue.model.ClueminingUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
public class ChildAdminAspect {

    private static final Logger logger = LoggerFactory.getLogger(ChildAdminAspect.class);

    // 形成一个横切贯入点
    @Pointcut("execution(public * com.ksrs.clue.controller.ChildUserAdminController.*(..))")
    public void log(){

    }
    // 再发起请求之前执行该方法
    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        if(!user.getParentId().equals("0")){
            throw new SysException(ResultEnum.USER_NOT_DAD);
        }

    }

}
