package com.ksrs.cms.service;

import com.ksrs.cms.model.Role;
import com.ksrs.cms.vo.ResultVo;
import java.util.List;
import java.util.Set;

public interface RoleService {

    List<String> findRoleByUserId(Integer userId);

    List<Integer> findRoleIdByUserId(Integer userId);

    int inertUserRoles(Integer userId, Integer roleId);

    int deleteUserRoleByUserId(Integer userId);

    /**
     * 查询角色列表
     * @return
     */
    ResultVo findAllRole();

    /**
     * 查询用户列表(分页)
     * @return
     */
    ResultVo findRoles(Integer page,Integer limit);

    /**
     * 查询出有多少个角色，用于分页
     * @return
     */
    Integer findRolesTotal();

    Set<Role> findRolesByUserId(Integer id);

    /**
     *  添加角色的接口
     * @param record
     * @return
     */
    ResultVo insert(Role record,List<Integer> resourcesIds);

    /**
     *  删除角色接口
     * @param id
     * @return
     */
    ResultVo deleteByPrimaryKey(Integer id);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    Role selectByPrimaryKey(Integer id);

    /**
     * 更新角色
     * @param record
     * @return
     */
    ResultVo updateByPrimaryKeySelective(Role record);

    /**
     * 插入角色资源表相关信息
     * @param roleId
     * @param resourcesId
     * @return
     */
    ResultVo insertRoleResources(Integer roleId,List<Integer> resourcesId);

    /**
     * 修改角色接口
     * @param role
     * @param resourcesId
     * @return
     */
    ResultVo realUpdateRole(Role role,List<Integer> resourcesId);
}
