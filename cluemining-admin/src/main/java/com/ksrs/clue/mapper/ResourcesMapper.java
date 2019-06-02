package com.ksrs.clue.mapper;

import com.ksrs.clue.model.Resources;
import com.ksrs.clue.resourcesUtil.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ResourcesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Resources record);

    int insertSelective(Resources record);

    Resources selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Resources record);

    int updateByPrimaryKey(Resources record);

    List<Menu> selectByRoleId(@Param("roleId")Integer roleId);
    //查询出所有权限，将权限加入shiro管理
    List<Resources> queryAll();

    /**
     * 根据多个角色id查询资源信息
     * @param list
     * @return
     */
    Set<Menu> findResourcesByRoleIds(@Param("roleIds")List<Integer> list);

}