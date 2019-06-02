package com.ksrs.cms.mapper.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.model.Resources;
import com.ksrs.cms.resourcesUtil.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ClueResourcesMapper {
    @TargetDataSource(name="cluemining")
    int deleteByPrimaryKey(Integer id);
    @TargetDataSource(name="cluemining")
    int insert(Resources record);
    @TargetDataSource(name="cluemining")
    int insertSelective(Resources record);
    @TargetDataSource(name="cluemining")
    Menu selectByPrimaryKey(@Param("id") Integer id);

    /**
     * 根据id更新资源信息
     * @param menu
     * @return
     */
    @TargetDataSource(name="cluemining")
    int updateByPrimaryKeySelective(Menu menu);
    @TargetDataSource(name="cluemining")
    int updateByPrimaryKey(Resources record);
    @TargetDataSource(name="cluemining")
    List<Menu> selectByRoleId(@Param("roleId") Integer roleId);
    /**
     * 查询出所有权限，将权限加入shiro管理
     */
    @TargetDataSource(name="cluemining")
    List<Menu> queryAll();
    /**
     * 根据多个角色id查询资源信息
     * @param list
     * @return
     */
    @TargetDataSource(name="cluemining")
    Set<Menu> findResourcesByRoleIds(@Param("roleIds") List<Integer> list);

    /**
     * 根据角色id查询出资源的id(用于修改角色时回显该角色拥有的资源信息)
     * @param roleId
     * @return
     */
    @TargetDataSource(name="cluemining")
    List<Integer> findResourcesIdByRoleId(@Param("roleId") Integer roleId);

    /**
     * 添加资源接口
     * @param menu
     * @return
     */
    @TargetDataSource(name="cluemining")
    int insertResources(Menu menu);

    /**
     * 批量删除资源
     * @param resorucesId
     * @return
     */
    @TargetDataSource(name="cluemining")
    int deleteResourcesByIds(@Param("list") List<Integer> resorucesId);

    /**
     * 批量删除role_resources表中的角色绑定的资源信息
     * @param resourcesIds
     * @return
     */
    @TargetDataSource(name="cluemining")
    int deleteRoleResourcesByIds(@Param("list") List<Integer> resourcesIds);
}