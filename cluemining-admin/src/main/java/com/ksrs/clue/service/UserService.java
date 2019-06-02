package com.ksrs.clue.service;

import com.ksrs.clue.model.User;
import com.ksrs.clue.vo.ResultVo;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    ResultVo insert(User record, List<Integer> roleIds);

    ResultVo findUsers(int page, int limit);

    Integer findUsersTotal();

    /**
     * 根据用户id修改用户信息和角色信息
     * @param record
     * @return
     */
    ResultVo updateByPrimaryKeySelective(User record,List<Integer> roleIds);
}
