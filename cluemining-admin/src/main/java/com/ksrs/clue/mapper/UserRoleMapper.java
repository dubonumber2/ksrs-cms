package com.ksrs.clue.mapper;

import com.ksrs.clue.model.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}