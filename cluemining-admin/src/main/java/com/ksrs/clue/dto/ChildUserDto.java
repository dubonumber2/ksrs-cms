package com.ksrs.clue.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 添加子账号时对应的dto模型
 */
public class ChildUserDto implements Serializable {
    @NotEmpty(message = "用户名必须填写")
    @Pattern(regexp = "^(?![0-9]+$)[0-9A-Za-z_@]{4,18}$",message = "用户名不合法")
    @Length(min = 4,message = "用户名长度不能小于4")
    @Length(max = 18,message = "用户名长度不能大于18")
    private String username;
    @NotEmpty(message = "密码必须填写")
    @Length(min = 6,message = "密码长度不能小于6")
    @Length(max = 18,message = "密码长度不能大于18")
    private String password;
    @NotEmpty(message = "使用者不能为空")
    private String realName;

    private String userNumber;
    @NotNull(message = "下载量不能为空")
    @Range(min = 0,message = "下载量不可为负数")
    private Integer dowloadAble;

    @NotNull(message = "所选地域不能为空")
    @Size(min = 1,message = "所选地域不能为空")
    private Integer[] provinces;

    public Integer[] getProvinces() {
        return provinces;
    }

    public void setProvinces(Integer[] provinces) {
        this.provinces = provinces;
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

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Integer getDowloadAble() {
        return dowloadAble;
    }

    public void setDowloadAble(Integer dowloadAble) {
        this.dowloadAble = dowloadAble;
    }
}
