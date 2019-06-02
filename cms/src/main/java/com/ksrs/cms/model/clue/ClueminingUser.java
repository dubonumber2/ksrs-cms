package com.ksrs.cms.model.clue;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ClueminingUser implements Serializable{

    private String id;
    @JsonProperty(value = "code")
    private String userNumber;
    @NotEmpty(message = "用户名必须填写")
    private String username;
    @NotEmpty(message = "密码必须填写")
    @Length(min=6,message = "密码不能小于6个字符")
    private String password;
    // 使用者
    @NotEmpty(message = "使用者不能为空")
    private String realName;
    // 所属公司
    @NotEmpty(message = "所属公司不能为空")
    private String  userCompany;
    // 父账号id 0为父账号
    private String parentId = "0";
    // 每日可下载量
    @NotNull(message = "下载量不能为空")
    private Integer dowloadAble;
    // 是否开启去重(子账号之间)
    @Range(min = 0,max = 1,message = "isRepeat只能为0或1")
    private Integer isRepeat;
    // 是否启用 0 停用 1 启用
    private Integer enable = 1;
    // 预约开通时间
    private String startTime;
    // 到期时间
    @NotEmpty(message = "到期时间不能为空")
    private String stopTime;
    // 创建时间
    private String createTime;
    // 最后一次修改时间
    private String updateTime;

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
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

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
