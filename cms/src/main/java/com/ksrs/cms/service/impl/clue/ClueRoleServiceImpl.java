package com.ksrs.cms.service.impl.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.dto.RolePageDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.RoleMapper;
import com.ksrs.cms.mapper.RoleSourcesMapper;
import com.ksrs.cms.mapper.clue.ClueRoleMapper;
import com.ksrs.cms.mapper.clue.ClueRoleSourcesMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.service.ClueResourcesService;
import com.ksrs.cms.service.ClueRoleService;
import com.ksrs.cms.service.ClueUtilService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.ResultUtil;
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
public class ClueRoleServiceImpl implements ClueRoleService {
    @Resource
    ClueRoleMapper clueRoleMapper;

    @Resource
    ClueRoleSourcesMapper clueRoleSourcesMapper;


    @Override
    @TargetDataSource(name="cluemining")
    public List<String> findRoleByUserId(String userId) {
        return clueRoleMapper.findRoleByUserId(userId);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public List<Integer> findRoleIdByUserId(String userId) {

        return clueRoleMapper.findRoleIdByUserId(userId);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int inertUserRoles(String userId, Integer roleId) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("roleId",roleId);
        return clueRoleMapper.inertUserRoles(map);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int deleteUserRoleByUserId(String userId) {
        return clueRoleMapper.deleteUserRoleByUserId(userId);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo findAllRole() {
        List<Role> roles = clueRoleMapper.findAllRole();
        RolePageDto pageDto = new RolePageDto();
        pageDto.setRoles(roles);
        return ResultUtil.success(pageDto);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo findRoles(Integer page, Integer limit) {
        if(null == page || null == limit){
            throw new SysException(ResultEnum.PAGEPARAMS_NOTNULL);
        }
        Integer pageSize = (page-1)*limit;
        Map<String,Integer> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("limit",limit);
        List<Role> roles = clueRoleMapper.findRoles(map);
        RolePageDto pageDto = new RolePageDto();
        pageDto.setRoles(roles);
        pageDto.setTotal(this.findRolesTotal());
        return ResultUtil.success(pageDto);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public Integer findRolesTotal() {
        return clueRoleMapper.findRolesTotal();
    }

    @Override
    @TargetDataSource(name="cluemining")
    public Set<Role> findRolesByUserId(String id) {

        return clueRoleMapper.findRolesByUserId(id);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo insert(Role record,List<Integer> resourcesIds) {
        clueRoleMapper.insert(record);
        if(!CollectionUtils.isEmpty(resourcesIds)){
            clueRoleSourcesMapper.insertRoleResources(record.getId(),resourcesIds);
        }
        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    @Transactional
    @TargetDataSource(name="cluemining")
    public ResultVo deleteByPrimaryKey(Integer id) {
        clueRoleMapper.deleteByPrimaryKey(id);

        //同时删除该角色所绑定的资源信息
        clueRoleSourcesMapper.deleteRoleResourcesByRoleId(id);

        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public Role selectByPrimaryKey(Integer id) {
        return clueRoleMapper.selectByPrimaryKey(id);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo updateByPrimaryKeySelective(Role record) {
        clueRoleMapper.updateByPrimaryKeySelective(record);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }
    @Transactional
    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo insertRoleResources(Integer roleId,List<Integer> resourcesId){
        // 先删除该角色已有的权限信息
        clueRoleSourcesMapper.deleteRoleResourcesByRoleId(roleId);
        // 再插入
        clueRoleSourcesMapper.insertRoleResources(roleId,resourcesId);

        return ResultUtil.success("修改成功");
    }

    @Override
    @Transactional
    @TargetDataSource(name="cluemining")
    public ResultVo realUpdateRole(Role role, List<Integer> resourcesId) {
        Integer roleId = role.getId();
        this.updateByPrimaryKeySelective(role);
        this.insertRoleResources(roleId,resourcesId);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }
}
