package com.ksrs.clue.enums;

public enum ResultEnum {
    SUCCESS(200,"success"),
    INSERT_SUCCESS(200,"插入成功"),
    UPDATE_SUCCESS(200,"修改信息成功"),
    DELETE_SUCCESS(200,"删除成功"),
    SYS_ERROR(400,"未知错误"),
    USERNAME_NULL(401,"用户名或密码不能为空"),
    USER_NOT_EXIST(402,"该用户名不存在"),
    UNKNOW_ERROR(403,"系统异常"),
    USER_EXIST(404,"该用户名已存在"),
    WRONGFUL_PARAMS(405,"您有多个参数不合法"),
    USER_ERROR(406,"用户名与密码不匹配"),
    USER_LOCKED(407,"该用户已被锁定，无法登录"),
    NO_PERMISSIONS(408,"对不起，你暂无权限访问"),
    PASSWORD_ERROR(409,"您输入的旧密码不正确"),
    PASSWORD_NOT_SAME(410,"新密码不能与旧密码相同"),
    PASSWORD_OLD_NEW(411,"两次输入密码不一致"),
    NOT_LOGIN(412,"请您先登录再试试"),
    USERDAD_UNENABLE(413,"您所属父账号已被关闭"),
    DOWLOAD_NOT_ENOUGH(414,"您的下载量不够啦"),
    USER_NOT_DAD(415,"您不是父账号不能执行该操作"),
    USER_SESSION_TIMEOUT(416,"您的登录已失效，请重新登录"),
    LOGIN_MANYS(417,"请跳转改密码界面");




    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
