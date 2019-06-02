package com.ksrs.clue.controller;

import com.alibaba.fastjson.JSON;
import com.ksrs.clue.dto.LoginDto;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.resourcesUtil.ClueUserInfo;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.service.ResourcesService;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.service.UserService;
import com.ksrs.clue.service.impl.RedisService;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import com.ksrs.clue.exception.SysException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


@Api("登录模块")
@RestController
@RequestMapping("/clue")
public class HomeController {


    @Autowired
    ClueminingUserService clueminingUserService;

    @Autowired
    RoleService roleService;

    @Autowired
    ResourcesService resourcesService;

    @Autowired
    RedisService redisService;



    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public ResultVo login(@RequestBody Map<String,String> logMap){
       String username = logMap.get("username");
       String password = logMap.get("password");
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return ResultUtil.fail(ResultEnum.USERNAME_NULL);
        }
        ClueminingUser user= clueminingUserService.findByUsername(username);
        if(user==null){
            throw new SysException(ResultEnum.USER_NOT_EXIST);
        }
        String parentId = user.getParentId();
        // 判断是子账号的父账号状态是否正常
        if(!parentId .equals("0")  && !parentId.equals("-1")){
            ClueminingUser userDad = clueminingUserService.selectByPrimaryKey(parentId);
            // 如果父账号状态不正常，则抛出异常
            if(userDad.getEnable()!=1){
                throw  new SysException(ResultEnum.USERDAD_UNENABLE);
            }
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        logger.info("【用户登录】:{}",username);
        try {
            subject.login(token);
        }catch (LockedAccountException e){
            token.clear();
            throw new SysException(ResultEnum.USER_LOCKED);
        }catch (UnknownAccountException e){
            token.clear();
            throw new SysException(ResultEnum.USER_NOT_EXIST);
        }catch (AuthenticationException e){
            token.clear();
            throw new SysException(ResultEnum.USER_ERROR);
        }

        ClueminingUser loginUser = (ClueminingUser) subject.getPrincipal();
        // 当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("userSessionId", loginUser.getId());
        List<Integer> list = roleService.findRoleIdByUserId(loginUser.getId());
        ResultVo voP = null;
        if(!CollectionUtils.isEmpty(list)){
            voP = resourcesService.findResourcesByRoleIds(list);
        }

        logger.info("sessionId:{}",session.getId().toString());
        ResultVo vo = clueminingUserService.getDetailClueUserMsg(user.getUserNumber());
        ClueUserInfo info = (ClueUserInfo) vo.getData();
        if(null!=voP){
            info.setPermissions(voP.getData());
        }
        redisService.set("session_u{"+session.getId()+"}u_session",JSON.toJSON(info).toString(),30);

        logger.info("【登录成功】");
        loginUser.setPassword(null);
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
        ClueminingUser user = (ClueminingUser) obj;
        subject.logout();
        // 删除自定义的缓存
        redisService.delete("session_u{"+session.getId()+"}u_session");
        logger.info("{}已退出登录", JSON.toJSONString(user));
        return ResultUtil.success("退出登录");
    }

    @ApiIgnore
    @GetMapping("/unlogin")
    public ResultVo unlogin(){
        return ResultUtil.fail(ResultEnum.USER_SESSION_TIMEOUT);
    }


}
