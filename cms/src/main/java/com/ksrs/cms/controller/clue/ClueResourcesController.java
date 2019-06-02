package com.ksrs.cms.controller.clue;

import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.service.ClueResourcesService;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/clue")
@Api(value = "线索挖掘资源管理模块")
@Validated
public class ClueResourcesController {

    @Autowired
    private ClueResourcesService clueResourcesService;

    @GetMapping("/resources/user")
    @ApiOperation(value = "根据登录用户获取该用户的资源(树形结构)")
    public ResultVo getMenuList(){
        return clueResourcesService.findResourcesByRoleIds();
    }

    @GetMapping("/resources")
    @ApiOperation(value = "查询出所有的资源(树形结构)")
    @RequiresPermissions("/clue/resources:read")
    public ResultVo getAllMenus(){
        return clueResourcesService.queryAll();
    }

    @GetMapping("/resources/ids")
    @ApiOperation(value = "根据角色id获取该角色的资源id(用于修改角色时回显资源信息)")
    public ResultVo getResourcesIdByRoleId(@RequestParam("roleId") Integer roleId){
        return clueResourcesService.findResourcesIdByRoleId(roleId);
    }

    @PostMapping("/resources")
    @ApiOperation(value = "新增资源接口")
    @RequiresPermissions("/clue/resources:create")
    public ResultVo insertResources(@Valid @RequestBody Menu menu, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        return clueResourcesService.insertResources(menu);
    }

    @DeleteMapping("/resources")
    @ApiOperation("删除资源接口")
    @RequiresPermissions("/clue/resources:delete")
    @ApiImplicitParam(name = "resourcesIds",value = "要删除的资源id(数组)",required = true,dataType = "Integer",paramType = "query")
    public ResultVo deleteResourcesByIds(@RequestParam("resourcesIds")@NotNull Integer[] resourcesIds){

        return clueResourcesService.deleteResourcesByIds(CollectionUtils.arrayToList(resourcesIds));
    }
    @PutMapping("/resources/{id}")
    @ApiOperation(value = "根据资源id修改资源接口")
    @RequiresPermissions("/clue/resources:update")
    public ResultVo updateByPrimaryKeySelective(@RequestBody Menu menu){

        return clueResourcesService.updateByPrimaryKeySelective(menu);
    }
    @ApiOperation("根据id查询资源信息")
    @GetMapping("/resources/{id}")
    @RequiresPermissions("/clue/resource:read")
    public ResultVo selectByPrimaryKey(@PathVariable("id") Integer id){
        return clueResourcesService.selectByPrimaryKey(id);
    }
}
