package com.ksrs.clue.mapper;

import com.ksrs.clue.model.RoleSourcesKey;

public interface RoleSourcesMapper {
    int deleteByPrimaryKey(RoleSourcesKey key);

    int insert(RoleSourcesKey record);

    int insertSelective(RoleSourcesKey record);
}