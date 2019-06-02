package com.ksrs.clue.controller;

import com.ksrs.clue.dto.ChildUserDto;
import com.ksrs.clue.dto.UpdateClueUserDto;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(value = "子父账号管理模块")
@RestController
@RequestMapping("/clue")
@Validated
public class ChildUserAdminController {

    @Autowired
    ClueminingUserService clueminingUserService;

    @ApiOperation(value = "显示用户列表",notes = "分页展示用户列表")
    @GetMapping("/userChilds")
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "当前显示第几页",required = true,paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "limit",value = "每页显示多少条数据",required = true,paramType = "query",dataType = "Integer")})
    public ResultVo findUsers(Integer page, Integer limit){

        return clueminingUserService.findClueUsers(page, limit);
    }


    @ApiOperation(value = "添加用户",notes = "根据User对象添加用户")
    @PostMapping("/child/create")
    public ResultVo saveUser(@Valid @RequestBody ChildUserDto user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        Integer[] provinces = user.getProvinces();
        return clueminingUserService.addUser(user, CollectionUtils.arrayToList(provinces));
    }

    @DeleteMapping("/child/delete/{id}")
    @ApiOperation(value = "根据用户id删除用户")
    @ApiImplicitParam(name = "id",value = "要删除的用户id",required = true,paramType = "path",dataType = "String")
    public ResultVo deleteUserById(@PathVariable("id")@NotNull String id){

        return clueminingUserService.deleteUserById(id);
    }

    @ApiOperation(value = "根据用户id修改用户接口")
    @PutMapping("/child/update/{id}")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "要修改的用户id",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "provinces",value = "开通城市的id",required = false,dataType = "Array")
    })
    public ResultVo updateByPrimaryKeySelective(@PathVariable("id")String id, @Valid@RequestBody UpdateClueUserDto updateClueUserDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResultUtil.fail(420,bindingResult.getFieldError().getDefaultMessage());
        }
        updateClueUserDto.setId(id);
        Integer[] provinces = updateClueUserDto.getProvinces();
        return clueminingUserService.updateDowload(updateClueUserDto,CollectionUtils.arrayToList(provinces));
    }

    @ApiOperation(value = "停用用户接口")
    @PutMapping("/child/{id}/close")
    @ApiImplicitParam(name = "id",paramType = "path",dataType = "String",required = true)
    public ResultVo closeUser(@PathVariable("id")@NotNull(message="id不能为空") String id){
        return clueminingUserService.closeEnable(id);
    }
    @ApiOperation(value = "启用用户接口")
    @PutMapping("/child/{id}/open")
    @ApiImplicitParam(name = "id",paramType = "path",dataType = "String",required = true)
    public ResultVo openUser(@PathVariable("id")@NotNull(message = "id不能为空")String id){
        return clueminingUserService.openEnable(id);
    }
    @ApiOperation("根据条件搜索子账号")
    @GetMapping("/child/selective")
    public ResultVo findChildUsersBySelectvite(String username, String realName, String createTime, Integer page, Integer limit){
        return clueminingUserService.findChildUsersBySelectvite(username, realName, createTime, page, limit);
    }
    @ApiOperation("获取最新的下载量信息")
    @GetMapping("/dowload")
    public ResultVo getDowloadAble(){
        return clueminingUserService.findDowloadAble();
    }

}