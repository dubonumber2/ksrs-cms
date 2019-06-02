package com.ksrs.cms.mapper;

import com.ksrs.cms.model.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}