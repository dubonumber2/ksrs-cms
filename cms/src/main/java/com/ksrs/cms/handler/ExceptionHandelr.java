package com.ksrs.cms.handler;

import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandelr {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandelr.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVo handler(Exception e){
        if(e instanceof SysException){
            SysException sysException = (SysException) e;
            return ResultUtil.fail(sysException.getCode(),sysException.getMessage());
        }else if(e instanceof UnauthenticatedException){
            return ResultUtil.fail(ResultEnum.NO_PERMISSIONS);
        }else if(e instanceof UnauthorizedException){
            return ResultUtil.fail(ResultEnum.NO_PERMISSIONS);
        }else if(e instanceof ConstraintViolationException){
            StringBuilder str = new StringBuilder();
            for(ConstraintViolation violation: ((ConstraintViolationException) e).getConstraintViolations()){
                str.append(violation.getMessage());
           }
           return ResultUtil.fail(420,str.toString());
        }else {
            logger.error("【系统异常】={}",e);
            return ResultUtil.fail();
        }
    }
}
