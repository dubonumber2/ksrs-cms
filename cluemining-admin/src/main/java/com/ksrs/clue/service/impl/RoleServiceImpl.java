package com.ksrs.clue.service.impl;

import com.ksrs.clue.mapper.RoleMapper;
import com.ksrs.clue.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    RoleMapper roleMapper;

    @Override
    public List<String> findRoleByUserId(String userId) {
        return roleMapper.findRoleByUserId(userId);
    }

    @Override
    public List<Integer> findRoleIdByUserId(String userId) {

        return roleMapper.findRoleIdByUserId(userId);
    }

    @Override
    public int inertUserRoles(String userId, Integer roleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("roleId",roleId);
        return roleMapper.inertUserRoles(map);
    }

    @Override
    public int deleteUserRoleByUserId(String userId) {
        return roleMapper.deleteUserRoleByUserId(userId);
    }
}
