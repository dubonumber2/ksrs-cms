package com.ksrs.cms.mapper;

import com.ksrs.cms.dto.MenuDto;
import com.ksrs.cms.model.Resources;
import com.ksrs.cms.resourcesUtil.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ResourcesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Resources record);

    int insertSelective(Resources record);

    Menu selectByPrimaryKey(@Param("id") Integer id);

    /**
     * 根据id更新资源信息
     * @param menu
     * @return
     */
    int updateByPrimaryKeySelective(Menu menu);

    int updateByPrimaryKey(Resources record);

    List<Menu> selectByRoleId(@Param("roleId") Integer roleId);
    /**
     * 查询出所有权限，将权限加入shiro管理
     */
    List<Menu> queryAll();
    /**
     * 根据多个角色id查询资源信息
     * @param list
     * @return
     */
    Set<Menu> findResourcesByRoleIds(@Param("roleIds")List<Integer> list);

    /**
     * 根据角色id查询出资源的id(用于修改角色时回显该角色拥有的资源信息)
     * @param roleId
     * @return
     */
    List<Integer> findResourcesIdByRoleId(@Param("roleId") Integer roleId);

    /**
     * 添加资源接口
     * @param menu
     * @return
     */
    int insertResources(Menu menu);

    /**
     * 批量删除资源
     * @param resorucesId
     * @return
     */
    int deleteResourcesByIds(@Param("list") List<Integer> resorucesId);

    /**
     * 批量删除role_resources表中的角色绑定的资源信息
     * @param resourcesIds
     * @return
     */
    int deleteRoleResourcesByIds(@Param("list") List<Integer> resourcesIds);
}