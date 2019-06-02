package com.ksrs.clue.service.impl;

import com.ksrs.clue.dto.PageDto;
import com.ksrs.clue.dto.ClueUserDto;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.mapper.UserMapper;
import com.ksrs.clue.model.User;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.service.UserService;
import com.ksrs.clue.util.PasswordHelper;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import com.ksrs.clue.exception.SysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResultVo insert(User record, List<Integer> roleIds) {
        try{
            PasswordHelper helper = new PasswordHelper();
            String mdPassword = helper.encryptPassword(record.getUsername(),record.getPassword());
            record.setPassword(mdPassword);
            userMapper.insert(record);
        }catch (DuplicateKeyException e){
            logger.error("【添加用户异常】:{}", ResultEnum.USER_EXIST.getMsg());
           throw  new SysException(ResultEnum.USER_EXIST) ;
        }catch (Exception e){
            logger.error("【系统异常】:{}",e.getMessage());
            throw new SysException(ResultEnum.UNKNOW_ERROR);
        }
        // 如果添加用户时角色信息不为空，则在user_role表中新增对应关系
        if(!CollectionUtils.isEmpty(roleIds)){
            for(int i=0;i<roleIds.size();i++){
               // roleService.inertUserRoles(record.getId(),roleIds.get(i));
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
        List<ClueUserDto> list = userMapper.findUsers(map);

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
        userMapper.updateByPrimaryKeySelective(record);
        Integer userId = record.getId();
        if(!CollectionUtils.isEmpty(roleIds)){
            //更新 user_role 表
            // 先删除该用户拥有的角色信息
           /// roleService.deleteUserRoleByUserId(userId);
            // 再插入新的角色信息
            for(int i=0;i<roleIds.size();i++){
               // roleService.inertUserRoles(userId,roleIds.get(i));
            }
        }

        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);

    }
}
