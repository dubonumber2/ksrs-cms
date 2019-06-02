package com.ksrs.cms.service.impl;

import com.ksrs.cms.dto.MenuDto;
import com.ksrs.cms.dto.ResourcesDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.ResourcesMapper;
import com.ksrs.cms.model.Resources;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.model.User;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.resourcesUtil.MenuTree;
import com.ksrs.cms.resourcesUtil.OutHandle;
import com.ksrs.cms.resourcesUtil.ResourcesTree;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.util.ShiroUtil;
import com.ksrs.cms.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResourcesServiceImpl implements ResourcesService{

    @Resource
    private ResourcesMapper resourcesMapper;
    @Autowired
    private RoleService roleService;

    @Override
    public OutHandle selectByRoleId(int roleId) {
        List<Menu> list = resourcesMapper.selectByRoleId(roleId);
        Role role = roleService.selectByPrimaryKey(roleId);
        if(null == role){
            throw new SysException(ResultEnum.ROLE_NOT_EXIST);
        }
        String roleName = role.getRoleName();
        // 生成树形结构
        Set<ResourcesTree> treeSet = this.productResourcesTree(list);
        OutHandle out = new OutHandle();
        out.setPermissions(treeSet);
        out.setRoleName(roleName);
        return out;
    }

    @Override
    public ResultVo queryAll() {
        List<Menu> menus = resourcesMapper.queryAll();
        Set<ResourcesTree> treeSet = this.productResourcesTree(menus);
        OutHandle out = new OutHandle();
        out.setPermissions(treeSet);
        return ResultUtil.success(out);
    }

    /**
     * 如果用户有多个角色，调用该方法得到该用户所拥有的资源
     * @return
     */
    @Override
    public ResultVo findResourcesByRoleIds() {
        // 拿到当前实体
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if("admin".equals(user.getUsername())){
            //如果登录用户是admin 给他所有资源
            return this.queryAll();
        }
        // 根据当前登录用户的id查询出该用户拥有的角色id
        List<Integer> list = roleService.findRoleIdByUserId(user.getId());
        if(CollectionUtils.isEmpty(list)){
            return ResultUtil.success(new OutHandle());
        }
         Set<Menu> menus = resourcesMapper.findResourcesByRoleIds(list);
         Set<ResourcesTree> resourcesTreeSet = this.productResourcesTree(menus);
         OutHandle out = new OutHandle();
         out.setPermissions(resourcesTreeSet);
         return ResultUtil.success(out);
    }

    /**
     * 生成树形结构核心代码
     * @param list
     * @return
     */
    private Set<ResourcesTree> productResourcesTree(Collection<Menu> list){
        Set<ResourcesTree> treeSet = new HashSet<>();
        for(Menu menus:list){
            if(menus.getParentId()==0){
                ResourcesTree resourcesTree = new ResourcesTree();
                BeanUtils.copyProperties(menus,resourcesTree);
                Set<MenuTree> menuTrees = new HashSet<>();
                for(Menu menuss:list){
                    if(menuss.getParentId() .equals(menus.getId()) ){
                        MenuTree menuTree = new MenuTree();
                        BeanUtils.copyProperties(menuss,menuTree);
                        Set<Menu> menus1 = new HashSet<>();
                        for(Menu menusss:list){
                            if(menusss.getParentId().equals(menuss.getId())){
                                    menus1.add(menusss);
                            }
                        }
                        menuTree.setMenuTrees(menus1);
                        menuTrees.add(menuTree);
                    }
                }
                resourcesTree.setMenuTrees(menuTrees);
                treeSet.add(resourcesTree);
            }
        }
        return treeSet;
    }

    @Override
    public ResultVo findResourcesIdByRoleId(Integer roleId) {
        List<Integer> list = resourcesMapper.findResourcesIdByRoleId(roleId);
        ResourcesDto dto = new ResourcesDto();
        dto.setResourcesIds(list);
        return ResultUtil.success(dto);
    }

    @Override
    public ResultVo insertResources(Menu menu) {
        resourcesMapper.insertResources(menu);
        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    public int deleteRoleResourcesByIds(List<Integer> resourcesIds) {
        resourcesMapper.deleteRoleResourcesByIds(resourcesIds);
        return 0;
    }

    @Override
    @Transactional
    public ResultVo deleteResourcesByIds(List<Integer> resorucesId) {
        if(CollectionUtils.isEmpty(resorucesId)){
            throw new SysException(ResultEnum.SYS_ERROR);
        }
        // 如果要删除的资源不为空，则先删除resources表中对应的资源
        resourcesMapper.deleteResourcesByIds(resorucesId);
        // 再删除中间表role_resources中对应的资源
        this.deleteRoleResourcesByIds(resorucesId);
        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    public ResultVo updateByPrimaryKeySelective(Menu menu) {
        resourcesMapper.updateByPrimaryKeySelective(menu);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo selectByPrimaryKey(Integer id) {
        Menu menu = resourcesMapper.selectByPrimaryKey(id);
        return ResultUtil.success(menu);
    }
}
