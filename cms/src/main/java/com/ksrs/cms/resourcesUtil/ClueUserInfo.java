package com.ksrs.cms.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksrs.cms.model.Role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClueUserInfo implements Serializable{

    private String id;

    private String username;

    private String password;

    private String realName;

    private String userCompany;

    private String parentId;

    private Integer dowloadAble;

    private Integer isRepeat;

    private String startTime;

    private String stopTime;

    @JsonProperty(value = "webTypes")
    private Set<WebType> webType = new HashSet<>();
    @JsonProperty(value = "provinces")
    private Set<Province> province = new HashSet<>();
    @JsonProperty(value = "roleIds")
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getDowloadAble() {
        return dowloadAble;
    }

    public void setDowloadAble(Integer dowloadAble) {
        this.dowloadAble = dowloadAble;
    }

    public Integer getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Integer isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public Set<WebType> getWebType() {
        return webType;
    }

    public void setWebType(Set<WebType> webType) {
        this.webType = webType;
    }

    public Set<Province> getProvince() {
        return province;
    }

    public void setProvince(Set<Province> province) {
        this.province = province;
    }
}
