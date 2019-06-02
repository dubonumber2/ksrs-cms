package com.ksrs.cms.controller.clue;


import com.ksrs.cms.dto.UpdateClueUserDto;
import com.ksrs.cms.model.clue.ClueminingUser;
import com.ksrs.cms.service.ClueminingUserService;
import com.ksrs.cms.service.UserService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Api(value = "线索挖掘用户管理模块")
@RestController
@RequestMapping("/clue")
@Validated
public class ClueminingUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClueminingUserService clueminingUserService;

    @Autowired
    private RedisSessionDAO redisSessionDAO;

    private static final Logger logger = LoggerFactory.getLogger(ClueminingUserController.class);

    /**
     * 用户端添加用户的接口
     * @param user
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加用户",notes = "根据User对象添加用户")
    @RequiresPermissions("/clue/user:create")
    @PostMapping ("/user")
    public ResultVo saveUser(@Valid ClueminingUser user, BindingResult bindingResult, Integer[] roleIds,Integer[] webTypes,Integer[] provinces){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        return clueminingUserService.addUser(user,CollectionUtils.arrayToList(roleIds),CollectionUtils.arrayToList(webTypes),CollectionUtils.arrayToList(provinces));
    }
    @ApiIgnore
    @GetMapping("/test")
    public ResultVo test(){
       /* ClueUserInfo userInfo = clueUserService.test();
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
       return ResultUtil.success(userInfo);*/
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
     @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "要修改的用户id",required = true,dataType = "String",paramType = "path"),
     @ApiImplicitParam(name = "roleIds",value = "角色的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "roleIds",value = "角色的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "webTypes",value = "网络推广标签的id",required = false,dataType = "Array"),
             @ApiImplicitParam(name = "provinces",value = "开通城市的id",required = false,dataType = "Array")
     })
     public ResultVo updateByPrimaryKeySelective(@PathVariable("id")String id, UpdateClueUserDto updateClueUserDto, Integer[] roleIds, Integer[] webTypes, Integer[] provinces){
         updateClueUserDto.setId(id);

        return clueminingUserService.updateUserById(updateClueUserDto,CollectionUtils.arrayToList(roleIds),CollectionUtils.arrayToList(webTypes),CollectionUtils.arrayToList(provinces));
     }
     @DeleteMapping("/user/{id}")
     @RequiresPermissions("/clue/user:delete")
     @ApiOperation(value = "根据用户id删除用户")
     @ApiImplicitParam(name = "id",value = "要删除的用户id",required = true,paramType = "path",dataType = "String")
     public ResultVo deleteUserById(@PathVariable("id")@NotNull String id){

        return clueminingUserService.deleteUserById(id);
     }
     @ApiOperation(value = "根据id查看用户详细信息")
     @RequiresPermissions("/clue/user:read")
     @GetMapping("/user/{id}")
     public ResultVo getDetailClueUserMsg(@PathVariable("id")String id){
        return clueminingUserService.getDetailClueUserMsg(id);
     }
     @ApiOperation(value = "停用用户接口")
     @PutMapping("/user/{id}/close")
     @ApiImplicitParam(name = "id",paramType = "path",dataType = "String",required = true)
     public ResultVo closeUser(@PathVariable("id")@NotNull(message="id不能为空") String id){
        return clueminingUserService.closeEnable(id);
     }
     @ApiOperation(value = "启用用户接口")
     @PutMapping("/user/{id}/open")
     @ApiImplicitParam(name = "id",paramType = "path",dataType = "String",required = true)
     public ResultVo openUser(@PathVariable("id")@NotNull(message = "id不能为空")String id){
        return clueminingUserService.openEnable(id);
     }

     @PutMapping("/user/password/{id}")
     @ApiOperation(value = "修改密码接口")
     @RequiresPermissions("/clue/user:update")
     @ApiImplicitParams({@ApiImplicitParam(name = "id",paramType = "path",dataType = "String",required = true),
             @ApiImplicitParam(name = "passowrd",dataType = "String",required = true)})
     public ResultVo updatePasswordById(@PathVariable("id")String id,@Length(min = 6,message = "密码不能小于6个字符")@NotEmpty(message = "密码不能为空")@RequestParam("password") String password){

        return clueminingUserService.updatePasswordById(id,password);
     }
    @ApiOperation("根据条件搜索子账号")
    @GetMapping("/user/child")
    @RequiresPermissions("/clue/child:read")
    public ResultVo findChildUsersBySelectvite(String code,Integer page, Integer limit){
        return clueminingUserService.findChildUsersBySelectvite(code, page, limit);
    }




}
