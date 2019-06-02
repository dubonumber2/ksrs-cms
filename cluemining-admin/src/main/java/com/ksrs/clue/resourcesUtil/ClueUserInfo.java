package com.ksrs.clue.resourcesUtil;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClueUserInfo implements Serializable{

    private String id;

    private String userNumber;

    private String username;

    private String password;

    private String realName;

    private String userCompany;

    private String parentId;

    private Integer dowloadAble;

    private Integer isRepeat;

    private String startTime;

    private String stopTime;

    private Integer isPoint;

    private SelectTag selectTag;

    private Object permissions;

    public Object getPermissions() {
        return permissions;
    }

    public void setPermissions(Object permissions) {
        this.permissions = permissions;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public SelectTag getSelectTag() {
        return selectTag;
    }

    public void setSelectTag(SelectTag selectTag) {
        this.selectTag = selectTag;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(Integer isPoint) {
        this.isPoint = isPoint;
    }
}
