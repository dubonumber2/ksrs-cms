package com.ksrs.clue.service;

import com.ksrs.clue.model.Resources;
import com.ksrs.clue.resourcesUtil.OutHandle;
import com.ksrs.clue.vo.ResultVo;

import java.util.List;

public interface ResourcesService {

    /**
     * 根据多个角色id查询资源信息
     * @return
     */
    ResultVo findResourcesByRoleIds(List<Integer> roleIds);

    List<Resources> queryAll();
}
