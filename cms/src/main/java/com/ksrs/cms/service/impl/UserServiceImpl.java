package com.ksrs.cms.service.impl;

import com.ksrs.cms.dto.PageDto;
import com.ksrs.cms.dto.UserDetailDto;
import com.ksrs.cms.dto.UserDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.UserMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.model.User;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.service.UserService;
import com.ksrs.cms.util.PasswordHelper;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;



    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }


    @Override
    @Transactional
    public ResultVo insert(User record,List<Integer> roleIds) {
        try{
            PasswordHelper helper = new PasswordHelper();
            String mdPassword = helper.encryptPassword(record.getUsername(),record.getPassword());
            record.setPassword(mdPassword);
            userMapper.insert(record);
        }catch (DuplicateKeyException e){
            logger.error("【添加用户异常】:{}",ResultEnum.USER_EXIST.getMsg());
           throw  new SysException(ResultEnum.USER_EXIST) ;
        }catch (Exception e){
            logger.error("【系统异常】:{}",e.getMessage());
            throw new SysException(ResultEnum.UNKNOW_ERROR);
        }
        // 如果添加用户时角色信息不为空，则在user_role表中新增对应关系
        if(!CollectionUtils.isEmpty(roleIds)){
            for(int i=0;i<roleIds.size();i++){

                roleService.inertUserRoles(record.getId(),roleIds.get(i));
            }
        }
        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    public ResultVo findUsers(int page, int limit) {
        int pageSize = (page-1)*limit;
        Map<String,Integer> map = new HashMap<>(2);
        map.put("pageSize",pageSize);
        map.put("limit",limit);
        List<UserDto> list = userMapper.findUsers(map);
        //查询用户所拥有的角色
        for(int j=0;j<list.size();j++){
            String roleName = this.getUserRoleName(list.get(j).getId());
            list.get(j).setRoleName(roleName);
        }
        PageDto pageDto = new PageDto();
        pageDto.setUsers(list);
        pageDto.setTotal(this.findUsersTotal());
        return ResultUtil.success(pageDto);
    }

    @Override
    public Integer findUsersTotal() {
        return userMapper.findUsersTotal();
    }

    @Override
    @Transactional
    public ResultVo updateByPrimaryKeySelective(User record,List<Integer> roleIds) {
        Integer userId = record.getId();
        if(!StringUtils.isEmpty(record.getPassword())){
            User user = userMapper.selectByPrimaryKey(userId);
            PasswordHelper helper = new PasswordHelper();
            String mdPassword = helper.encryptPassword(user.getUsername(),record.getPassword());
            record.setPassword(mdPassword);
        }
        userMapper.updateByPrimaryKeySelective(record);
        if(!CollectionUtils.isEmpty(roleIds)){
            //更新 user_role 表
            // 先删除该用户拥有的角色信息
            roleService.deleteUserRoleByUserId(userId);
            // 再插入新的角色信息
            for(int i=0;i<roleIds.size();i++){
                roleService.inertUserRoles(userId,roleIds.get(i));
            }
        }

        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);

    }

    @Override
    @Transactional
    public ResultVo deleteByPrimaryKey(Integer id) {
        // 删除该用户
        userMapper.deleteByPrimaryKey(id);
        // 同时删除该用户拥有的角色
        roleService.deleteUserRoleByUserId(id);
        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    public ResultVo selectByPrimaryKey(Integer id) {
        if(null == id){
            throw  new SysException(ResultEnum.SYS_ERROR);
        }
        User user = userMapper.selectByPrimaryKey(id);
        if(null == user){
            throw  new SysException(ResultEnum.USER_NOT_EXIST);
        }
        UserDetailDto userDto = new UserDetailDto();
        BeanUtils.copyProperties(user,userDto);
        Set<Role> roles = roleService.findRolesByUserId(id);
        Set<Integer> roleIds = new HashSet<>();
        if(!CollectionUtils.isEmpty(roles)){
            for(Role role:roles){
                roleIds.add(role.getId());
            }
        }
        userDto.setRoleIds(roleIds);
        return ResultUtil.success(userDto);
    }

    private String getUserRoleName(Integer userId){
        List<String> roleNames = roleService.findRoleByUserId(userId);
        StringBuffer stringBuffer = new StringBuffer();
        for(int i=0;i<roleNames.size();i++){
            if(i+1<roleNames.size()){
                stringBuffer.append(roleNames.get(i)+",");
            }else{
                stringBuffer.append(roleNames.get(i));
            }

        }
        return stringBuffer.toString();
    }

    @Override
    public ResultVo updatePassword(String password, Integer id) {
        PasswordHelper helper = new PasswordHelper();
        String username = userMapper.selectByPrimaryKey(id).getUsername();
        String md5 = helper.encryptPassword(username,password);
        userMapper.updatePassword(md5, id);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }
    @Override
    public ResultVo getLoginMessage() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        return ResultUtil.success(user);
    }
}
