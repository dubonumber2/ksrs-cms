package com.ksrs.cms.model;




import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable{
    private Integer id;
    @NotBlank(message = "用户名必须填写")
    @Length(min=4,message = "用户名太短啦")
    private String username;
    @NotEmpty(message = "密码必须填写")
    @Length(min = 4,message = "您设置的密码太短")
    private String password;
    private int enable = 1;
    @NotEmpty(message = "使用者不能为空")
    private String realName;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}