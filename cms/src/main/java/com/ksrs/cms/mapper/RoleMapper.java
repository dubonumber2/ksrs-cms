package com.ksrs.cms.mapper;

import com.ksrs.cms.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    List<String> findRoleByUserId(@Param("userId") Integer userId);

    /**
     * 用户添加角色时支持添加多个
     * @param
     * @return
     */
    int inertUserRoles(Map<String, Integer> roleIds);

    /**
     * 根据用户id查询出角色id，解决用户插入相同角色问题
     * @param userId
     * @return
     */
    List<Integer> findRoleIdByUserId(@Param("userId") Integer userId);

    /**
     * 当用户的角色信息发生变更时，先根据用户id删除此用户拥有的所有角色
     * @param userId
     * @return
     */
    int deleteUserRoleByUserId(@Param("userId") Integer userId);

    /**
     * 查询角色列表(所有)
     * @return
     */
    List<Role> findAllRole();

    /**
     * 查询角色列表(分页)
     * @param map
     * @return
     */
    List<Role> findRoles(Map<String,Integer> map);

    /**
     * 查询出有多少个角色，用于分页
     * @return
     */
    Integer findRolesTotal();

    Set<Role> findRolesByUserId(@Param("userId") Integer id);
}