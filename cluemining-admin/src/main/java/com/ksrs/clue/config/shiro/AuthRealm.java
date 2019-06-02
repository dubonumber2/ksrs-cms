package com.ksrs.clue.config.shiro;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.exception.SysException;
import com.ksrs.clue.mapper.ResourcesMapper;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.model.Resources;
import com.ksrs.clue.model.User;
import com.ksrs.clue.resourcesUtil.ClueUserInfo;
import com.ksrs.clue.resourcesUtil.Menu;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.service.ResourcesService;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.service.UserService;
import com.ksrs.clue.service.impl.RedisService;
import com.ksrs.clue.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;


public class AuthRealm extends AuthorizingRealm {

    @Autowired
    ClueminingUserService clueminingUserService;

    @Autowired
    UserService userService;

    @Autowired
    ResourcesService resourcesService;

    @Autowired
    RedisSessionDAO redisSessionDAO;

    @Autowired
    RedisService redisService;

    @Autowired
    RoleService roleService;

    @Resource
    ResourcesMapper resourcesMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthRealm.class);


    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ClueminingUser user = (ClueminingUser) principalCollection.fromRealm(this.getClass().getName()).iterator().next();

        String userId = user.getId();
        logger.info("【开始授权】用户:{}",user.getUsername());
        //查出角色名称
        List<String> roleNames = roleService.findRoleByUserId(userId);

        // 查出用户所拥有的角色id
        List<Integer> roleIds = roleService.findRoleIdByUserId(userId);

        // 用set结构来存放这些角色所绑定的资源(无重复)
        Set<Menu> resourcesSet = new HashSet<>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(!CollectionUtils.isEmpty(roleIds)){
            for(Integer roleId:roleIds){
                List<Menu> menu = resourcesMapper.selectByRoleId(roleId);
                if(!CollectionUtils.isEmpty(menu)){
                    for(Menu tmp:menu){
                        resourcesSet.add(tmp);
                    }
                }
            }
        }

        List<String> promissions = new ArrayList<>();
        if(!CollectionUtils.isEmpty(resourcesSet)){
            for(Menu tmp : resourcesSet){
                promissions.add(tmp.getInit());
            }
        }
        if(CollectionUtils.isEmpty(roleNames)){
            info.addRoles(roleNames);
        }
        info.addStringPermissions(promissions);
        logger.info("【授权完成】用户:{}",user.getUsername());
        return info;
    }

    // 认证登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String)authenticationToken.getPrincipal();
        ClueminingUser user = clueminingUserService.findByUsername(username);
        // 当用户重新登录时，踢掉前一个用户
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for(Session session:sessions){
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            if(null!=spc){
                obj = spc.getPrimaryPrincipal();
                // 在线的人
                ClueminingUser loginUser = (ClueminingUser) obj;
                if(username.equals(loginUser.getUsername())){
                    // 如果发现有用户名一样的人，则删除
                    redisSessionDAO.delete(session);
                    redisService.delete("session_u{"+session.getId()+"}u_session");
                }
            }

        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                user.getPassword(), //密码
                this.getClass().getName()  //realm name
        );

       return authenticationInfo;
    }


}
