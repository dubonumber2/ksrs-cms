package com.ksrs.cms.service;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.vo.ResultVo;

import java.util.List;
import java.util.Set;

public interface ClueRoleService {
    @TargetDataSource(name="cluemining")
    List<String> findRoleByUserId(String userId);
    @TargetDataSource(name="cluemining")
    List<Integer> findRoleIdByUserId(String userId);
    @TargetDataSource(name="cluemining")
    int inertUserRoles(String userId, Integer roleId);
    @TargetDataSource(name="cluemining")
    int deleteUserRoleByUserId(String userId);

    /**
     * 查询角色列表
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo findAllRole();

    /**
     * 查询用户列表(分页)
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo findRoles(Integer page, Integer limit);

    /**
     * 查询出有多少个角色，用于分页
     * @return
     */
    @TargetDataSource(name="cluemining")
    Integer findRolesTotal();
    @TargetDataSource(name="cluemining")
    Set<Role> findRolesByUserId(String id);

    /**
     *  添加角色的接口
     * @param record
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo insert(Role record,List<Integer> resourcesIds);

    /**
     *  删除角色接口
     * @param id
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo deleteByPrimaryKey(Integer id);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    @TargetDataSource(name="cluemining")
    Role selectByPrimaryKey(Integer id);

    /**
     * 更新角色
     * @param record
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo updateByPrimaryKeySelective(Role record);

    /**
     * 插入角色资源表相关信息
     * @param roleId
     * @param resourcesId
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo insertRoleResources(Integer roleId, List<Integer> resourcesId);

    /**
     * 修改角色接口
     * @param role
     * @param resourcesId
     * @return
     */
    @TargetDataSource(name="cluemining")
    ResultVo realUpdateRole(Role role, List<Integer> resourcesId);
}
