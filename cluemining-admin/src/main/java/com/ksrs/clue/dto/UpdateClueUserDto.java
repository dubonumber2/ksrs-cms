package com.ksrs.clue.dto;

import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel
public class UpdateClueUserDto {
    private String id;
    @NotEmpty(message = "用户名必须填写")
    @Pattern(regexp = "^(?![0-9]+$)[0-9A-Za-z_@]{4,18}$",message = "用户名不合法")
    @Length(min = 4,message = "用户名长度不能小于4")
    @Length(max = 18,message = "用户名长度不能大于18")
    private String username;

    private String realName;
    @Range(min = 0,message = "下载量不可为负数")
    private Integer dowloadAble;

    @Size(min = 1,message = "所选地域不能为空")
    private Integer[] provinces;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer[] getProvinces() {
        return provinces;
    }

    public void setProvinces(Integer[] provinces) {
        this.provinces = provinces;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDowloadAble() {
        return dowloadAble;
    }

    public void setDowloadAble(Integer dowloadAble) {
        this.dowloadAble = dowloadAble;
    }
}
