package com.ksrs.clue.mapper;

import com.ksrs.clue.dto.ClueUserDto;
import com.ksrs.clue.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    /**
     * 根据用户id修改用户信息
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByUsername(@Param("username") String username);

    /**
     * 显示用户列表接口(enable为1)
     * @param map 用于分页查询的参数
     * @return 返回一个list集合存放user
     */
    List<ClueUserDto> findUsers(Map<String,Integer> map);

    /**
     * 查询有多少个用户，用于分页
     * @return
     */
    Integer findUsersTotal();



}