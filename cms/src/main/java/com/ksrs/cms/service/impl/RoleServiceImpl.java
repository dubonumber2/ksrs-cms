package com.ksrs.cms.service.impl;

import com.ksrs.cms.dto.RolePageDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.RoleMapper;
import com.ksrs.cms.mapper.RoleSourcesMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.util.ShiroUtil;
import com.ksrs.cms.vo.ResultVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    RoleMapper roleMapper;

    @Resource
    RoleSourcesMapper roleSourcesMapper;


    @Override
    public List<String> findRoleByUserId(Integer userId) {
        return roleMapper.findRoleByUserId(userId);
    }

    @Override
    public List<Integer> findRoleIdByUserId(Integer userId) {

        return roleMapper.findRoleIdByUserId(userId);
    }

    @Override
    public int inertUserRoles(Integer userId, Integer roleId) {
        Map<String,Integer> map = new HashMap<>();
        map.put("userId",userId);
        map.put("roleId",roleId);
        roleMapper.inertUserRoles(map);
        ShiroUtil.clearAuth();
        return 0;
    }

    @Override
    public int deleteUserRoleByUserId(Integer userId) {
        roleMapper.deleteUserRoleByUserId(userId);
        ShiroUtil.clearAuth();
        return 0;
    }

    @Override
    public ResultVo findAllRole() {
        List<Role> roles = roleMapper.findAllRole();
        RolePageDto pageDto = new RolePageDto();
        pageDto.setRoles(roles);
        return ResultUtil.success(pageDto);
    }

    @Override
    public ResultVo findRoles(Integer page, Integer limit) {
        if(null == page || null == limit){
            throw new SysException(ResultEnum.PAGEPARAMS_NOTNULL);
        }
        Integer pageSize = (page-1)*limit;
        Map<String,Integer> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("limit",limit);
        List<Role> roles = roleMapper.findRoles(map);
        RolePageDto pageDto = new RolePageDto();
        pageDto.setRoles(roles);
        pageDto.setTotal(this.findRolesTotal());
        return ResultUtil.success(pageDto);
    }

    @Override
    public Integer findRolesTotal() {
        return roleMapper.findRolesTotal();
    }

    @Override
    public Set<Role> findRolesByUserId(Integer id) {

        return roleMapper.findRolesByUserId(id);
    }

    @Override
    @Transactional
    public ResultVo insert(Role record,List<Integer> resourcesIds) {
        roleMapper.insert(record);
        if(!CollectionUtils.isEmpty(resourcesIds)){
            roleSourcesMapper.insertRoleResources(record.getId(),resourcesIds);
        }

        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    @Transactional
    public ResultVo deleteByPrimaryKey(Integer id) {
        roleMapper.deleteByPrimaryKey(id);

        //同时删除该角色所绑定的资源信息
        roleSourcesMapper.deleteRoleResourcesByRoleId(id);
        ShiroUtil.clearAuth();

        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    public Role selectByPrimaryKey(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResultVo updateByPrimaryKeySelective(Role record) {
        roleMapper.updateByPrimaryKeySelective(record);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }
    @Transactional
    @Override
    public ResultVo insertRoleResources(Integer roleId,List<Integer> resourcesId){

        // 先删除该角色已有的权限信息
        roleSourcesMapper.deleteRoleResourcesByRoleId(roleId);
        // 再插入
        if(!CollectionUtils.isEmpty(resourcesId)){
            roleSourcesMapper.insertRoleResources(roleId,resourcesId);
        }
        ShiroUtil.clearAuth();

        return ResultUtil.success("修改成功");
    }

    @Override
    @Transactional
    public ResultVo realUpdateRole(Role role, List<Integer> resourcesId) {
        Integer roleId = role.getId();
        this.updateByPrimaryKeySelective(role);
        this.insertRoleResources(roleId,resourcesId);
        ShiroUtil.clearAuth();
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }
}
