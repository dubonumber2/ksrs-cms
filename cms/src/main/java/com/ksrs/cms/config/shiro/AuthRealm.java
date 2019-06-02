package com.ksrs.cms.config.shiro;


import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.ResourcesMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.model.User;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    @Autowired
    ResourcesService resourcesService;

    @Autowired
    RoleService roleService;

    @Resource
    ResourcesMapper resourcesMapper;

    @Autowired
    RedisSessionDAO redisSessionDAO;

    private static final Logger logger = LoggerFactory.getLogger(AuthRealm.class);


    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.fromRealm(this.getClass().getName()).iterator().next();
        logger.info("【授权操作】用户名为：{}，开始授权",user.getUsername());
        Integer userId = user.getId();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if(!user.getUsername().equals("admin")){
            List<String> roleName = roleService.findRoleByUserId(userId);
            List<Integer> roleIds = roleService.findRoleIdByUserId(userId);
            Set<Menu> menus = resourcesMapper.findResourcesByRoleIds(roleIds);
            List<String> promissions = new ArrayList<>();
            List<String> resourcesName = new ArrayList<>();
            if(!CollectionUtils.isEmpty(roleName)){
                logger.info("【授权操作】当前用户拥有的角色是：{}",roleName);
                info.addRoles(roleName);
            }

            for(Menu permissisons : menus){
                if(!"".equals(permissisons.getInit()) && null != permissisons.getInit()){
                    promissions.add(permissisons.getInit());
                    resourcesName.add(permissisons.getName());
                }

            }
            if(!CollectionUtils.isEmpty(promissions)){
                logger.info("【授权操作】当前用户拥有的资源是：{}",resourcesName);
                info.addStringPermissions(promissions);
            }

            logger.info("【授权操作】用户名为：{}，授权完成",user.getUsername());
        }else{
            logger.info("【系统管理员授权操作】admin");
            List<Menu> menus = resourcesMapper.queryAll();
            for (Menu menu:menus){
                if(!StringUtils.isEmpty(menu.getInit())){
                    logger.info("【授权操作admin】当前用户拥有的资源是：{}",menu.getInit());
                    info.addStringPermission(menu.getInit());
                }

            }
        }


        return info;
    }

    // 认证登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户的输入的账号.
        String username = (String)authenticationToken.getPrincipal();
        User user = userService.findByUsername(username);
        if(user==null){
            throw new SysException(ResultEnum.USER_NOT_EXIST);
        }
        // 当用户重新登录时，踢掉前一个用户
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for(Session session:sessions){
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            if(null!=spc){
                obj = spc.getPrimaryPrincipal();
                // 在线的人
                User loginUser = (User) obj;
                if(username.equals(loginUser.getUsername())){
                    // 如果发现有用户名一样的人，则删除
                    redisSessionDAO.delete(session);
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

    public void clearAuthz(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}
