package com.ksrs.clue.service;

import java.util.List;

public interface RoleService {

    List<String> findRoleByUserId(String userId);

    List<Integer> findRoleIdByUserId(String userId);

    int inertUserRoles(String userId,Integer roleId);

    int deleteUserRoleByUserId(String userId);
}
