package com.ksrs.clue.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

public class UpdatePasswordDto {

    @NotEmpty(message = "旧密码不能为空")
    private String oldPassword;
    @NotEmpty(message = "新密码不能为空")
    @Length(min = 6,message = "您设置的密码不能小于6个字符")
    @Length(max = 18,message = "您设置的密码不能超过18个字符")
    private String newPassword;
    @NotEmpty(message = "新密码不能为空")
    @Length(min = 6,message = "您设置的密码不能小于6个字符")
    @Length(max = 18,message = "您设置的密码不能超过18个字符")
    private String confirmNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
