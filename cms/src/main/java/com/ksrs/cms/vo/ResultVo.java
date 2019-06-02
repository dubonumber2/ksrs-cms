package com.ksrs.cms.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 返回结果格式统一
 */
public class ResultVo<T> {

    //错误码
    private Integer code;

    //提示信息
    private String msg;

    //具体的内容
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
