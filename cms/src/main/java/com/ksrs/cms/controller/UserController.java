package com.ksrs.cms.controller;



import com.ksrs.cms.dto.UpdateUserDto;
import com.ksrs.cms.model.User;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.service.UserService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.*;

@Api(value = "管理端用户管理模块")
@RestController
@RequestMapping("/cms")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 管理端添加用户的接口
     * @param user
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加用户",notes = "根据User对象添加用户")
    @PostMapping ("/user")
    @RequiresPermissions("user:create")
    public ResultVo saveUser(@Valid User user, BindingResult bindingResult, Integer[] roleIds){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        return userService.insert(user, CollectionUtils.arrayToList(roleIds));
    }

    @ApiOperation(value = "显示用户列表",notes = "分页展示用户列表")
    @GetMapping("/users")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "当前显示第几页",required = true,paramType = "query",dataType = "Integer"),
    @ApiImplicitParam(name = "limit",value = "每页显示多少条数据",required = true,paramType = "query",dataType = "Integer")})
    @RequiresPermissions("users:read")
    public ResultVo findUsers(Integer page, Integer limit){

        return userService.findUsers(page,limit);
     }
     @ApiOperation(value = "根据用户id修改用户接口")
     @PutMapping("/user/{id}")
     @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "要修改的用户id",required = true,dataType = "Integer",paramType = "path"),
     @ApiImplicitParam()})
     @RequiresPermissions("user:update")
     public ResultVo updateByPrimaryKeySelective(@PathVariable("id")Integer id, UpdateUserDto updateUserDto, Integer[] roleIds){
         updateUserDto.setId(id);
         User user = new User();
         BeanUtils.copyProperties(updateUserDto,user);
        return userService.updateByPrimaryKeySelective(user,CollectionUtils.arrayToList(roleIds));
     }
     @ApiOperation(value = "根据用户id删除用户")
     @DeleteMapping("/user/{id}")
     @RequiresPermissions("user:delete")
     public ResultVo deleteUserByPrimaryKey(@PathVariable("id") Integer id){
        return userService.deleteByPrimaryKey(id);
     }

     @ApiOperation(value = "根据用户id查看用户信息")
     @GetMapping("/user/{id}")
     @RequiresPermissions("user:read")
     public ResultVo selectUserByPrimaryKey(@PathVariable("id") Integer id){
        return userService.selectByPrimaryKey(id);
     }

     @PutMapping("/user/password/{id}")
     @ApiOperation(value = "修改密码接口")
     @RequiresPermissions("user:update")
     public ResultVo updatePassword(@PathVariable("id") Integer id,@Length(min = 4,message = "您的密码太短啦") String password){

        return userService.updatePassword(password, id);
     }
    @ApiOperation("根据当前登录用户获得用户信息")
     @GetMapping("/user/message")
    public ResultVo getLoginMessage() {
        return userService.getLoginMessage();
    }

}
