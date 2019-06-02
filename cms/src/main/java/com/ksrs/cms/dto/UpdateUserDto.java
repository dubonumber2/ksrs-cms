package com.ksrs.cms.dto;

import io.swagger.annotations.ApiModel;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

@ApiModel
public class UpdateUserDto implements Serializable {
    private Integer id;

    private String password;

    private String realName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
