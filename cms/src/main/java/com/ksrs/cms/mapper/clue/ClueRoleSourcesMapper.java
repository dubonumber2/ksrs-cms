package com.ksrs.cms.mapper.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.model.RoleSourcesKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueRoleSourcesMapper {
    @TargetDataSource(name="cluemining")
    int deleteByPrimaryKey(RoleSourcesKey key);
    @TargetDataSource(name="cluemining")
    int insert(RoleSourcesKey record);
    @TargetDataSource(name="cluemining")
    int insertSelective(RoleSourcesKey record);
    /**
     * 根据角色id删除该角色绑定的资源信息
     * @param roleId
     * @return
     */
    @TargetDataSource(name="cluemining")
    int deleteRoleResourcesByRoleId(@Param("roleId") Integer roleId);

    /**
     * 插入角色绑定的资源信息
     * @param roleId
     * @param resourcesIds
     * @return
     */
    @TargetDataSource(name="cluemining")
    int insertRoleResources(@Param("roleId") Integer roleId, @Param("list") List<Integer> resourcesIds);


}