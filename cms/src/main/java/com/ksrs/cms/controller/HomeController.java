package com.ksrs.cms.controller;

import com.alibaba.fastjson.JSON;
import com.ksrs.cms.dto.LoginDto;
import com.ksrs.cms.dto.PageDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.model.User;
import com.ksrs.cms.service.UserService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Api("登录模块")
@RestController
@RequestMapping("/cms")
@Validated
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    RedisSessionDAO redisSessionDAO;

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public ResultVo login(@RequestParam("username")String username, @RequestParam("password") String password){
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return ResultUtil.fail(ResultEnum.USERNAME_NULL);
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        logger.info("【用户登录】:{}",username);
        try {
            subject.login(token);
        }catch (AuthenticationException e){
            token.clear();
            throw new SysException(ResultEnum.USER_ERROR);
        }

        User user = (User) subject.getPrincipal();
        logger.info("【登录成功】");
        user.setPassword(null);
        Object tokens = subject.getSession().getId();
        LoginDto loginDto = new LoginDto();
        loginDto.setToken(tokens);
        return ResultUtil.success(loginDto);
    }
    @ApiIgnore
    @GetMapping("/unauthorized")
    public ResultVo unauthorized(){
        return ResultUtil.fail(ResultEnum.NO_PERMISSIONS);
    }

    @ApiOperation(value = "登出接口")
    @DeleteMapping("/logout")
    public ResultVo logout(){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
        obj = spc.getPrimaryPrincipal();
        User user = (User) obj;
        subject.logout();
        logger.info("{}已退出登录", JSON.toJSONString(user));
        return ResultUtil.success("退出登录");
    }

    @ApiIgnore
    @GetMapping("/unlogin")
    public ResultVo unlogin(){
        return ResultUtil.fail(ResultEnum.USER_SESSION_TIMEOUT);
    }

    @ApiIgnore
    @GetMapping("/online")
    public ResultVo test(){
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        Map<String,String> username = new HashMap<>();
        int count = 0;
        for(Session session : sessions){
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            String test = DefaultSubjectContext.PRINCIPALS_SESSION_KEY;
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            obj = spc.getPrimaryPrincipal();
            User user = (User) obj;
            if(null!=user){
                count++;
            }
           username.put(user.getUsername(),user.getRealName());
        }
        PageDto pageDto = new PageDto();
        pageDto.setTotal(count);
        pageDto.setUsers(username);
        return ResultUtil.success(pageDto);
    }
}
