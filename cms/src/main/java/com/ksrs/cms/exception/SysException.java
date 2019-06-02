package com.ksrs.cms.exception;

import com.ksrs.cms.enums.ResultEnum;

/**
 * 自定义异常 全局异常处理
 */
public class SysException extends RuntimeException {

    private Integer code;

    public SysException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
