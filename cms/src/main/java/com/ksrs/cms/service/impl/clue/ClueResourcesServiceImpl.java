package com.ksrs.cms.service.impl.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.dto.MenuDto;
import com.ksrs.cms.dto.ResourcesDto;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.ResourcesMapper;
import com.ksrs.cms.mapper.clue.ClueResourcesMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.model.User;
import com.ksrs.cms.model.clue.ClueminingUser;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.resourcesUtil.MenuTree;
import com.ksrs.cms.resourcesUtil.OutHandle;
import com.ksrs.cms.resourcesUtil.ResourcesTree;
import com.ksrs.cms.service.ClueResourcesService;
import com.ksrs.cms.service.ClueRoleService;
import com.ksrs.cms.service.ResourcesService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.ResultUtil;
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
public class ClueResourcesServiceImpl implements ClueResourcesService {

    @Resource
    private ClueResourcesMapper clueResourcesMapper;
    @Autowired
    private ClueRoleService clueRoleService;

    @Override
    @TargetDataSource(name = "cluemining")
    public OutHandle selectByRoleId(int roleId) {
        List<Menu> list = clueResourcesMapper.selectByRoleId(roleId);
        Role role = clueRoleService.selectByPrimaryKey(roleId);
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
    @TargetDataSource(name = "cluemining")
    public ResultVo queryAll() {
        List<Menu> menus = clueResourcesMapper.queryAll();
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
    @TargetDataSource(name = "cluemining")
    public ResultVo findResourcesByRoleIds() {
        // 拿到当前实体
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        // 根据当前登录用户的id查询出该用户拥有的角色id
        List<Integer> list = clueRoleService.findRoleIdByUserId(user.getId());
        if(CollectionUtils.isEmpty(list)){
            throw new SysException(ResultEnum.SYS_ERROR);
        }
         Set<Menu> menus = clueResourcesMapper.findResourcesByRoleIds(list);
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
    @TargetDataSource(name = "cluemining")
    private Set<ResourcesTree> productResourcesTree(Collection<Menu> list){
        Set<ResourcesTree> treeSet = new HashSet<>();
        for(Menu menu1:list){
            if(menu1.getParentId()==0){
                ResourcesTree resourcesTree = new ResourcesTree();
                BeanUtils.copyProperties(menu1,resourcesTree);
                Set<MenuTree> menuTrees = new HashSet<>();
                for(Menu menu2:list){
                    if(menu2.getParentId().equals(menu1.getId())){
                        MenuTree menuTree = new MenuTree();
                        BeanUtils.copyProperties(menu2,menuTree);
                        Set<Menu> menuSet = new HashSet<>();
                        for(Menu menu3:list){
                            if(menu3.getParentId().equals(menu2.getId())){
                                menuSet.add(menu3);
                            }
                        }
                        menuTree.setMenuTrees(menuSet);
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
    @TargetDataSource(name = "cluemining")
    public ResultVo findResourcesIdByRoleId(Integer roleId) {
        List<Integer> list = clueResourcesMapper.findResourcesIdByRoleId(roleId);
        ResourcesDto dto = new ResourcesDto();
        dto.setResourcesIds(list);
        return ResultUtil.success(dto);
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo insertResources(Menu menu) {
        clueResourcesMapper.insertResources(menu);
        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public int deleteRoleResourcesByIds(List<Integer> resourcesIds) {
        return clueResourcesMapper.deleteRoleResourcesByIds(resourcesIds);
    }

    @Override
    @Transactional
    @TargetDataSource(name = "cluemining")
    public ResultVo deleteResourcesByIds(List<Integer> resorucesId) {
        if(CollectionUtils.isEmpty(resorucesId)){
            throw new SysException(ResultEnum.SYS_ERROR);
        }
        // 如果要删除的资源不为空，则先删除resources表中对应的资源
        clueResourcesMapper.deleteResourcesByIds(resorucesId);
        // 再删除中间表role_resources中对应的资源
        this.deleteRoleResourcesByIds(resorucesId);
        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo updateByPrimaryKeySelective(Menu menu) {
        clueResourcesMapper.updateByPrimaryKeySelective(menu);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo selectByPrimaryKey(Integer id) {
        Menu menu = clueResourcesMapper.selectByPrimaryKey(id);
        return ResultUtil.success(menu);
    }
}
