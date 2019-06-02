package com.ksrs.cms.service;

import com.ksrs.cms.dto.PageDto;
import com.ksrs.cms.dto.UserDto;
import com.ksrs.cms.model.User;
import com.ksrs.cms.vo.ResultVo;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    ResultVo insert(User record,List<Integer> roleIds);

    ResultVo findUsers(int page, int limit);

    Integer findUsersTotal();

    /**
     * 根据用户id修改用户信息和角色信息
     * @param record
     * @return
     */
    ResultVo updateByPrimaryKeySelective(User record,List<Integer> roleIds);

    /**
     * 根据用户id删除用户
     * @param id
     * @return
     */
    ResultVo deleteByPrimaryKey(Integer id);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    ResultVo selectByPrimaryKey(Integer id);
    /**
     * 修改密码接口
     * @param password
     * @return
     */
    ResultVo updatePassword(String password,Integer id);

    /**
     * 获取当前登录用户的信息
     * @return
     */
    ResultVo getLoginMessage();
}
