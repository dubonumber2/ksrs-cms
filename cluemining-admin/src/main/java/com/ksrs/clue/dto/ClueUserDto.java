package com.ksrs.clue.dto;

import com.ksrs.clue.resourcesUtil.Province;

import java.util.List;
import java.util.Set;

/**
 * 用于用户列表展示
 */

public class ClueUserDto {

    private String id;

    private String username;

    private String realName;

    private Integer dowloadAble;

    private String createTime;

    private Integer enable;

    private List<Province> provinces;

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }



    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getDowloadAble() {
        return dowloadAble;
    }

    public void setDowloadAble(Integer dowloadAble) {
        this.dowloadAble = dowloadAble;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
