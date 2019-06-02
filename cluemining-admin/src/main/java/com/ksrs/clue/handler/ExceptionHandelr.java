package com.ksrs.clue.handler;

import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import com.ksrs.clue.exception.SysException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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
        }else if(e instanceof ConstraintViolationException){
            StringBuilder str = new StringBuilder();
            for(ConstraintViolation violation: ((ConstraintViolationException) e).getConstraintViolations()){
                str.append(violation.getMessage());
            }
            return ResultUtil.fail(420,str.toString());
        }else if(e instanceof DuplicateKeyException){
            return ResultUtil.fail(ResultEnum.USER_EXIST);
        }else if(e instanceof HttpRequestMethodNotSupportedException){
            return ResultUtil.fail(ResultEnum.USER_SESSION_TIMEOUT);
        }else{
            logger.error("【系统异常】={}",e);
            return ResultUtil.fail();
        }
    }
}
