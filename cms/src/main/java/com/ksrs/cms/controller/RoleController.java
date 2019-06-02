package com.ksrs.cms.controller;

import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value = "管理端角色管理模块")
@RequestMapping("/cms")
@RestController
@Validated
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourcesService resourcesService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/allroles")
    public ResultVo findAllRole(){
        return roleService.findAllRole();
    }

    @ApiOperation(value = "分页查询角色列表")
    @GetMapping("/roles")
    @RequiresPermissions("roles:read")
    public ResultVo findRoles(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit){
        return roleService.findRoles(page, limit);
    }

    @ApiOperation(value = "查看角色资源",notes = "根据角色id查看角色所绑定资源")
    @ApiImplicitParam(name = "id",value = "角色id",dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    @RequiresPermissions("role:read")
    public ResultVo testRole(@NotNull(message = "角色id不能为空") @PathVariable("id")Integer id){

        return ResultUtil.success(resourcesService.selectByRoleId(id));
    }
    @PostMapping("/role")
    @ApiOperation(value = "添加角色接口")
    @RequiresPermissions("role:create")
    public ResultVo insertRole(@Valid Role role, BindingResult bindingResult,Integer[] resourcesIds){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        List<Integer> ids = null;
        if(resourcesIds != null){
            ids = CollectionUtils.arrayToList(resourcesIds);
        }
        return roleService.insert(role,ids);
    }


    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "根据角色id删除角色接口")
    @RequiresPermissions("role:delete")
    public ResultVo deleteByPrimaryKey(@NotNull(message = "id不能为空") @PathVariable("id")Integer id){
        return roleService.deleteByPrimaryKey(id);
    }

    @PutMapping("/role/{id}")
    @ApiOperation(value = "根据角色id修改角色信息及相应的资源信息")
    @RequiresPermissions("role:update")
    public ResultVo updateRole(@Valid Role role,Integer[] resourcesIds){
        List<Integer> ids = null;
        if(resourcesIds != null){
            ids = CollectionUtils.arrayToList(resourcesIds);
        }
        return roleService.realUpdateRole(role,ids);
    }

}
