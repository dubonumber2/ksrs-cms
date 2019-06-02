package com.ksrs.clue.mapper;

import com.ksrs.clue.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 根据用户id查询该用户所拥有的权限
     * @param userId
     * @return
     */
    List<String> findRoleByUserId(@Param("userId")String userId);

    /**
     * 用户添加角色时支持添加多个
     * @param
     * @return
     */
    int inertUserRoles( Map<String,Object> roleIds);

    /**
     * 根据用户id查询出角色id，解决用户插入相同角色问题
     * @param userId
     * @return
     */
    List<Integer> findRoleIdByUserId(@Param("userId") String userId);

    /**
     * 当用户的角色信息发生变更时，先根据用户id删除此用户拥有的所有角色
     * @param userId
     * @return
     */
    int deleteUserRoleByUserId(@Param("userId") String userId);
}