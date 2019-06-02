package com.ksrs.cms.controller;

import com.ksrs.cms.dto.MenuDto;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/cms")
@Api(value = "管理端资源管理模块")
@Validated
public class ResourcesController {

    @Autowired
    private ResourcesService resourcesService;

    @GetMapping("/resources/user")
    @ApiOperation(value = "根据登录用户获取该用户的资源(树形结构)")
    public ResultVo getMenuList(){
        return resourcesService.findResourcesByRoleIds();
    }

    @GetMapping("/resources")
    @ApiOperation(value = "查询出所有的资源(树形结构)")
    @RequiresPermissions("resources:read")
    public ResultVo getAllMenus(){
        return resourcesService.queryAll();
    }

    @GetMapping("/resources/ids")
    @ApiOperation(value = "根据角色id获取该角色的资源id(用于修改角色时回显资源信息)")
    public ResultVo getResourcesIdByRoleId(@RequestParam("roleId") Integer roleId){
        return resourcesService.findResourcesIdByRoleId(roleId);
    }

    @PostMapping("/resources")
    @ApiOperation(value = "新增资源接口")
    @RequiresPermissions("resources:create")
    public ResultVo insertResources(@Valid @RequestBody Menu menu, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }

        return resourcesService.insertResources(menu);
    }

    @DeleteMapping("/resources")
    @ApiOperation("删除资源接口")
    @RequiresPermissions("resources:delete")
    @ApiImplicitParam(name = "resourcesIds",value = "要删除的资源id(数组)",required = true,dataType = "Integer",paramType = "query")
    public ResultVo deleteResourcesByIds(@RequestParam("resourcesIds")@NotNull Integer[] resourcesIds){

        return resourcesService.deleteResourcesByIds(CollectionUtils.arrayToList(resourcesIds));
    }
    @PutMapping("/resources/{id}")
    @ApiOperation(value = "根据资源id修改资源接口")
    @RequiresPermissions("resources:update")
    public ResultVo updateByPrimaryKeySelective(@RequestBody Menu menu){

        return resourcesService.updateByPrimaryKeySelective(menu);
    }

    @ApiOperation("根据id查询资源信息")
    @GetMapping("/resources/{id}")
    @RequiresPermissions("resource:read")
    public ResultVo selectByPrimaryKey(@PathVariable("id") Integer id){
        return resourcesService.selectByPrimaryKey(id);
    }

    @ApiOperation("根据当前登录用户获得资源信息")
    @GetMapping("/resources/subject")
    public ResultVo selectBySubject(){
        return resourcesService.findResourcesByRoleIds();
    }
}
