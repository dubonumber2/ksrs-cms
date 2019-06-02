package com.ksrs.clue.service.impl;

import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.exception.SysException;
import com.ksrs.clue.mapper.ResourcesMapper;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.model.Resources;
import com.ksrs.clue.resourcesUtil.ResourcesTree;
import com.ksrs.clue.resourcesUtil.Menu;
import com.ksrs.clue.resourcesUtil.MenuTree;
import com.ksrs.clue.resourcesUtil.OutHandle;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.service.ResourcesService;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private ClueminingUserService clueminingUserService;

    @Autowired
    RoleService roleService;



    @Override
    public List<Resources> queryAll() {
        return resourcesMapper.queryAll();
    }

    /**
     * 如果用户有多个角色，调用该方法得到该用户所拥有的资源
     * @return
     */
    @Override
    public ResultVo findResourcesByRoleIds(List<Integer> list) {

        if(CollectionUtils.isEmpty(list)){
            throw new SysException(ResultEnum.SYS_ERROR);
        }
        Set<Menu> menus = resourcesMapper.findResourcesByRoleIds(list);
        Set<ResourcesTree> resourcesTreeSet = this.productResourcesTree(menus);
        ResultVo vo = clueminingUserService.identifyParentId();
        String res = (String) vo.getData();
        if(!res.equals("false")){
            ResourcesTree tree = new ResourcesTree();
            tree.setName("子账号管理");
            tree.setId(0);
            tree.setEnglishName("childAdmin");
            tree.setType(0);
            resourcesTreeSet.add(tree);
        }
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
}
