package com.ksrs.cms.dto;

import com.ksrs.cms.model.User;

import java.io.Serializable;

/**
 * 用于用户列表展示
 */
public class UserDto implements Serializable {

    private Integer id;

    private String username;

    private String realName;

    private String createTime;
    // 角色名称
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
