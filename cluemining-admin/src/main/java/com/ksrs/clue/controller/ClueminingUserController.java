package com.ksrs.clue.controller;



import com.ksrs.clue.dto.ClueUserDto;
import com.ksrs.clue.dto.UpdateClueUserDto;
import com.ksrs.clue.dto.UpdatePasswordDto;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.resourcesUtil.OutHandle;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.service.UserService;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.service.ResourcesService;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Api(value = "个人信息模块")
@RestController
@RequestMapping("/clue")
@Validated
public class ClueminingUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private ClueminingUserService clueminingUserService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    @Autowired
    private RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(ClueminingUserController.class);

   /* *//**
     * 用户端添加用户的接口
     * @param
     * @param
     * @return
     *//*
    //@RequiresPermissions("/users/add1")
    @ApiOperation(value = "添加用户",notes = "根据User对象添加用户")
    @RequiresPermissions("/clue/user:create")
    @PostMapping ("/user")
    public ResultVo saveUser(@Valid ClueminingUser user, BindingResult bindingResult, Integer[] roleIds,Integer[] webTypes,Integer[] provinces){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        return clueminingUserService.addUser(user,CollectionUtils.arrayToList(roleIds),CollectionUtils.arrayToList(webTypes),CollectionUtils.arrayToList(provinces));
    }

    @GetMapping("/test")
    public ResultVo test(){
       *//* ClueUserInfo userInfo = clueUserService.test();
        WebType a = new WebType();
        a.setWebTypeName("赶集");
        Province b = new Province();
        b.setProvinceName("北京市");
        List<String> city = new ArrayList<>();
        city.add("朝阳区");
        city.add("海淀区");
        city.add("丰台区");
        b.setCitys(city);
        userInfo.getSelectTag().getWebType().add(a);
        userInfo.getSelectTag().getProvince().add(b);
       return ResultUtil.success(userInfo);*//*
        Collection<Session> sessions = redisSessionDAO.getActiveSessions();
        for(Session session : sessions){
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            String test = DefaultSubjectContext.PRINCIPALS_SESSION_KEY;
            logger.info(test);
            SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
            obj = spc.getPrimaryPrincipal();
            ClueminingUser user = (ClueminingUser) obj;
            System.out.println(user.getId()+"\n"
            +  user.getUsername());
        }
        return ResultUtil.success(null);
    }

    @ApiOperation(value = "查看角色资源",notes = "根据角色id查看角色所绑定资源")
    @ApiImplicitParam(name = "roleId",value = "角色id",dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/roleDetail/{roleId}",method = RequestMethod.GET)
    public ResultVo testRole(@PathVariable("roleId")Integer roleId){

        return ResultUtil.success(resourcesService.selectByRoleId(roleId));
    }

    @ApiOperation(value = "显示用户列表",notes = "分页展示用户列表")
    @GetMapping("/users")
    @RequiresPermissions("/clue/users:read")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "当前显示第几页",required = true,paramType = "query",dataType = "Integer"),
    @ApiImplicitParam(name = "limit",value = "每页显示多少条数据",required = true,paramType = "query",dataType = "Integer")})
    public ResultVo findUsers(Integer page, Integer limit){

        return clueminingUserService.findClueUsers(page, limit);
     }
     @ApiOperation(value = "根据用户id修改用户接口")
     @RequiresPermissions("/clue/user:update")
     @PutMapping("/user/{id}")
     @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "要修改的用户id",required = true,dataType = "Integer",paramType = "path"),
     @ApiImplicitParam(name = "roleIds",value = "角色的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "roleIds",value = "角色的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "webTypes",value = "网络推广标签的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "provinces",value = "开通城市的id",required = false,dataType = "Array")
     })
     public ResultVo updateByPrimaryKeySelective(@PathVariable("id")Integer id, UpdateClueUserDto updateClueUserDto, Integer[] roleIds, Integer[] webTypes, Integer[] provinces){
         updateClueUserDto.setId(id);

        return clueminingUserService.updateUserById(updateClueUserDto,CollectionUtils.arrayToList(roleIds),CollectionUtils.arrayToList(webTypes),CollectionUtils.arrayToList(provinces));
     }
     @DeleteMapping("/user/{id}")
     @RequiresPermissions("/clue/user:delete")
     @ApiOperation(value = "根据用户id删除用户")
     @ApiImplicitParam(name = "id",value = "要删除的用户id",required = true,paramType = "path",dataType = "Integer")
     public ResultVo deleteUserById(@PathVariable("id")@NotNull Integer id){

        return clueminingUserService.deleteUserById(id);
     }
     @ApiOperation(value = "根据id查看用户详细信息")
     @RequiresPermissions("/clue/user:read")
     @GetMapping("/user/{id}")
     public ResultVo getDetailClueUserMsg(@PathVariable("id")Integer id){
        return clueminingUserService.getDetailClueUserMsg(id);
     }*/
     @ApiOperation("修改密码接口")
     @PutMapping("/user/password")
    public ResultVo updatePasswordById(@Valid@RequestBody UpdatePasswordDto dto,BindingResult result){
         if(result.hasErrors()){
             return ResultUtil.fail(420,result.getFieldError().getDefaultMessage());
         }
         String oldPassword = dto.getOldPassword();
         String newPassword = dto.getNewPassword();
         String confirmPass = dto.getConfirmNewPassword();
         if(!newPassword.equals(confirmPass)){
             return ResultUtil.fail(ResultEnum.PASSWORD_OLD_NEW);
         }
        return clueminingUserService.updatePasswordById(oldPassword, newPassword);
    }
    @ApiOperation("验证旧密码接口")
    @GetMapping("/user/identify/password")
    public ResultVo identifyPassword(@Validated @NotEmpty(message = "旧密码不能为空")@RequestParam(name = "oldPassword",required = true) String oldPassword){

         boolean res = clueminingUserService.identifyPassword(oldPassword);
         if(res){
             return ResultUtil.success("PASS_IDENTIFY");
         }
         return ResultUtil.fail(ResultEnum.PASSWORD_ERROR);
    }
    @ApiOperation("获取当前登录用户的信息")
    @GetMapping("/user/message")
    public ResultVo getLoginUserMessage(){
         return clueminingUserService.getLoginUserMessage();
    }

    @ApiOperation("获取当前用户的资源信息")
    @GetMapping("/user/permission")
    public ResultVo getPermissionsMessage(){
        // 拿到当前实体
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        // 根据当前登录用户的id查询出该用户拥有的角色id
        List<Integer> list = roleService.findRoleIdByUserId(user.getId());
        if(list.size()==0||null==list){
            OutHandle outHandle = new OutHandle();
            outHandle.setPermissions(null);
            return ResultUtil.success(outHandle);
        }
         return resourcesService.findResourcesByRoleIds(list);
    }

    @ApiOperation("获取父账号地域信息")
    @GetMapping("/provinces")
    public ResultVo getProvince(){
        return clueminingUserService.getProvinceById();
    }

    @ApiOperation("点击不再提示接口")
    @PutMapping("/expand")
    public ResultVo closePoint(){
         Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        return clueminingUserService.closeIsPoint(user.getId());
    }
    @ApiOperation("判断是否提示接口")
    @GetMapping("/expand")
    public ResultVo isPoint(){
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        return clueminingUserService.findIsPoint(user.getId());
    }

    @ApiOperation("判断老用户是否改过密码")
    @GetMapping("/turnout")
    public ResultVo isChagePassword(){
         return clueminingUserService.isChagePassword();
    }



}
