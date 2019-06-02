package com.ksrs.cms.controller.clue;

import com.ksrs.cms.mapper.clue.ClueResourcesMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.service.ClueResourcesService;
import com.ksrs.cms.service.ClueRoleService;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(value = "线索挖掘角色管理")
@RequestMapping("/clue")
@RestController
@Validated
public class ClueRoleController {

    @Autowired
    private ClueRoleService clueRoleService;

    @Autowired
    private ClueResourcesService clueResourcesService;

    private static final Logger logger = LoggerFactory.getLogger(ClueRoleController.class);

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/allroles")
    public ResultVo findAllRole(){
        return clueRoleService.findAllRole();
    }

    @ApiOperation(value = "分页查询角色列表")
    @GetMapping("/roles")
    @RequiresPermissions("/clue/roles:read")
    public ResultVo findRoles(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit){
        return clueRoleService.findRoles(page, limit);
    }

    @ApiOperation(value = "查看角色资源",notes = "根据角色id查看角色所绑定资源")
    @ApiImplicitParam(name = "id",value = "角色id",dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    @RequiresPermissions("/clue/role:read")
    public ResultVo testRole(@NotNull(message = "角色id不能为空") @PathVariable("id")Integer id){

        return ResultUtil.success(clueResourcesService.selectByRoleId(id));
    }
    @PostMapping("/role")
    @ApiOperation(value = "添加角色接口")
    @RequiresPermissions("/clue/role:create")
    public ResultVo insertRole(@Valid Role role, BindingResult bindingResult,Integer[] resourcesIds){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        List<Integer> ids = null;
        if(resourcesIds != null){
            ids = CollectionUtils.arrayToList(resourcesIds);
        }
        return clueRoleService.insert(role,ids);
    }


    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "根据角色id删除角色接口")
    @RequiresPermissions("/clue/role:delete")
    public ResultVo deleteByPrimaryKey(@NotNull(message = "id不能为空") @PathVariable("id")Integer id){
        return clueRoleService.deleteByPrimaryKey(id);
    }

    @PutMapping("/role/{id}")
    @ApiOperation(value = "根据角色id修改角色信息及相应的资源信息")
    @RequiresPermissions("/clue/role:update")
    public ResultVo updateRole(@Valid Role role,Integer[] resourcesIds){
        List<Integer> ids = null;
        if(resourcesIds != null){
            ids = CollectionUtils.arrayToList(resourcesIds);
        }
        return clueRoleService.realUpdateRole(role,ids);
    }

}
