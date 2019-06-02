package com.ksrs.cms.service;

import com.ksrs.cms.dto.MenuDto;
import com.ksrs.cms.model.Resources;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.resourcesUtil.OutHandle;
import com.ksrs.cms.vo.ResultVo;

import java.util.List;
import java.util.Set;

public interface ResourcesService {

    OutHandle selectByRoleId(int roleId);

    /**
     * 查询出所有资源
     * @return
     */
    ResultVo queryAll();

    /**
     * 根据多个角色id查询资源信息
     * @return
     */
    ResultVo findResourcesByRoleIds();

    /**
     * 根据角色id查询出资源的id(用于修改角色时回显该角色拥有的资源信息)
     * @param roleId
     * @return
     */
    ResultVo findResourcesIdByRoleId(Integer roleId);
    /**
     * 添加资源接口
     * @param menu
     * @return
     */
    ResultVo insertResources(Menu menu);
    /**
     * 批量删除资源
     * @param resorucesId
     * @return
     */
    ResultVo deleteResourcesByIds( List<Integer> resorucesId);

    /**
     * 批量删除role_resources表中的角色绑定的资源信息
     * @param resourcesIds
     * @return
     */
    int deleteRoleResourcesByIds( List<Integer> resourcesIds);
    /**
     * 根据id更新资源信息
     * @param menu
     * @return
     */
    ResultVo updateByPrimaryKeySelective(Menu menu);

    ResultVo selectByPrimaryKey(Integer id);
}
