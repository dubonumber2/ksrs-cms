package com.ksrs.cms.mapper;

import com.ksrs.cms.model.Resources;
import com.ksrs.cms.model.RoleSourcesKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleSourcesMapper {
    int deleteByPrimaryKey(RoleSourcesKey key);

    int insert(RoleSourcesKey record);

    int insertSelective(RoleSourcesKey record);
    /**
     * 根据角色id删除该角色绑定的资源信息
     * @param roleId
     * @return
     */
    int deleteRoleResourcesByRoleId(@Param("roleId")Integer roleId);

    /**
     * 插入角色绑定的资源信息
     * @param roleId
     * @param resourcesIds
     * @return
     */
    int insertRoleResources(@Param("roleId")Integer roleId,@Param("list")List<Integer> resourcesIds);


}