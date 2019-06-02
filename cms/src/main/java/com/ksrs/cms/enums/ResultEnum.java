package com.ksrs.cms.enums;

public enum ResultEnum {
    SUCCESS(200,"success"),
    INSERT_SUCCESS(200,"插入成功"),
    UPDATE_SUCCESS(200,"修改信息成功"),
    DELETE_SUCCESS(200,"删除成功"),
    SYS_ERROR(400,"未知错误"),
    USERNAME_NULL(401,"用户名或密码不能为空"),
    USER_NOT_EXIST(402,"该用户不存在"),
    UNKNOW_ERROR(403,"系统异常"),
    USER_EXIST(404,"该用户名已存在"),
    WRONGFUL_PARAMS(405,"您有多个参数不合法"),
    USER_ERROR(406,"用户名与密码错误"),
    USER_LOCKED(407,"该用户已被锁定，无法登录"),
    NO_PERMISSIONS(408,"对不起，你暂无权限访问"),
    PAGEPARAMS_NOTNULL(409,"分页参数不能为空"),
    ROLE_NOT_EXIST(410,"该角色不存在"),
    NOT_LOGIN(411,"请登录再试试"),
    USER_SESSION_TIMEOUT(416,"您的登录已失效，请重新登录");



    ;

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
