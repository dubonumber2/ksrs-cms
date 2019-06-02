package com.ksrs.cms.service.impl.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.dto.*;
import com.ksrs.cms.enums.ResultEnum;
import com.ksrs.cms.exception.SysException;
import com.ksrs.cms.mapper.clue.ClueRoleMapper;
import com.ksrs.cms.mapper.clue.ClueminingUserMapper;
import com.ksrs.cms.mapper.RoleMapper;
import com.ksrs.cms.mapper.UserRoleMapper;
import com.ksrs.cms.model.Role;
import com.ksrs.cms.model.User;
import com.ksrs.cms.model.clue.ClueminingUser;
import com.ksrs.cms.model.UserRole;
import com.ksrs.cms.resourcesUtil.ClueUserInfo;
import com.ksrs.cms.resourcesUtil.Province;
import com.ksrs.cms.resourcesUtil.SelectTag;
import com.ksrs.cms.resourcesUtil.WebType;
import com.ksrs.cms.service.ClueRoleService;
import com.ksrs.cms.service.ClueminingUserService;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.util.PasswordHelper;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ClueminingUserServiceImpl implements ClueminingUserService {
    @Resource
    ClueminingUserMapper clueminingUserMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    ClueRoleMapper clueRoleMapper;

    @Autowired
    ClueRoleService clueRoleService;



    private static final Logger logger = LoggerFactory.getLogger(ClueminingUserServiceImpl.class);

    @Override
    @TargetDataSource(name="cluemining")
    public ClueminingUser findByUsername(String username) {

        return clueminingUserMapper.findByUsername(username);
    }

    @Override
    @Transactional
    @TargetDataSource(name="cluemining")
    public ResultVo addUser(ClueminingUser clueminingUser, List<Integer> roleIds,List<Integer> webTypes,List<Integer> provinces) {
        PasswordHelper helper = new PasswordHelper();
        String mdPassowrd = helper.encryptPassword(clueminingUser.getUsername(),clueminingUser.getPassword());
        String dateTime = String.valueOf(System.currentTimeMillis());
        String userNumber = helper.encryptPassword(clueminingUser.getUsername(),dateTime);
        clueminingUser.setPassword(mdPassowrd);
        clueminingUser.setUserNumber(userNumber);
        try {
            clueminingUserMapper.addUser(clueminingUser);
        }catch (DuplicateKeyException e){
            logger.error("【系统异常】添加用户失败,该{}用户名已存在",clueminingUser.getUsername());
            throw new SysException(ResultEnum.USER_EXIST);
        }

        if(!CollectionUtils.isEmpty(roleIds)){
            // 插入角色信息
            for(Integer roleId:roleIds){
                UserRole userRole= new UserRole();
                userRole.setUserid(userNumber);
                userRole.setRoleid(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        if(!CollectionUtils.isEmpty(webTypes)){
            // 插入网络推广标签信息
            for(Integer webTypeId:webTypes){
                this.insertWebTypeById(userNumber,webTypeId);
            }
        }
        if(!CollectionUtils.isEmpty(provinces)){
            for(Integer provinceId:provinces){
                this.insertProvinceById(userNumber,provinceId);
            }
        }


        return ResultUtil.success(ResultEnum.INSERT_SUCCESS);
    }

    @Override
    @TargetDataSource(name="cluemining")
    @Transactional
    public ResultVo deleteUserById(String id) {
        clueminingUserMapper.deleteUserById(id);
        this.deleteWebTypeById(id);
        this.deleteProvincesById(id);
        clueRoleService.deleteUserRoleByUserId(id);
        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    @Transactional
    @TargetDataSource(name="cluemining")
    public ResultVo updateUserById(UpdateClueUserDto user, List<Integer> roleIds, List<Integer> webTypes, List<Integer> provinces) {
        ClueminingUser clueminingUser = new ClueminingUser();


        BeanUtils.copyProperties(user,clueminingUser);
        if(user != null){
            clueminingUserMapper.updateUserById(clueminingUser);
        }
        //先清空中间表关联的数据
        if(!CollectionUtils.isEmpty(webTypes)){
            this.deleteWebTypeById(clueminingUser.getId());
            // 插入网络推广标签信息
            for(Integer webTypeId:webTypes){
                this.insertWebTypeById(clueminingUser.getId(),webTypeId);
            }
        }
        if(!CollectionUtils.isEmpty(provinces)){
            this.deleteProvincesById(clueminingUser.getId());
            for(Integer provinceId:provinces){
                this.insertProvinceById(clueminingUser.getId(),provinceId);
            }

        }
        if(!CollectionUtils.isEmpty(roleIds)){
            //清空角色信息
            clueRoleMapper.deleteUserRoleByUserId(clueminingUser.getId());
            for(Integer roleId:roleIds){
                UserRole userRole= new UserRole();
                userRole.setUserid(clueminingUser.getId());
                userRole.setRoleid(roleId);
                userRoleMapper.insert(userRole);
            }
        }


        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int deleteWebTypeById(String id) {
        clueminingUserMapper.deleteWebTypeById(id);
        return 0;
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int deleteProvincesById(String id) {
        clueminingUserMapper.deleteProvincesById(id);
        return 0;
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int insertWebTypeById(String id, Integer webId) {
        clueminingUserMapper.insertWebTypeById(id,webId);
        return 0;
    }

    @Override
    @TargetDataSource(name="cluemining")
    public int insertProvinceById(String userId, Integer provinceId) {
        clueminingUserMapper.insertProvinceById(userId, provinceId);
        return 0;
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo findClueUsers(Integer page, Integer limit) {
        Integer pageSize = (page-1)*limit;
        List<ClueUserDto> list = clueminingUserMapper.findClueUsers(pageSize,limit);
        //查询用户所拥有的角色
        for(int j=0;j<list.size();j++){
            List<String> roleNames = clueRoleService.findRoleByUserId(list.get(j).getId());
            StringBuffer stringBuffer = new StringBuffer();
            for(int i=0;i<roleNames.size();i++){
                if(i+1<roleNames.size()){
                    stringBuffer.append(roleNames.get(i)+",");
                }else{
                    stringBuffer.append(roleNames.get(i));
                }

            }
            list.get(j).setRoleName(stringBuffer.toString());
        }
        PageDto pageDto = new PageDto();
        pageDto.setUsers(list);
        pageDto.setTotal(this.getUsersTotal());
        return ResultUtil.success(pageDto);
    }
    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo getDetailClueUserMsg(String id){
        ClueminingUser user = clueminingUserMapper.selectByPrimaryKey(id);
        List<WebType> webTypes = clueminingUserMapper.selectWebType(id);
        List<Province> provinces = clueminingUserMapper.selectProvince(id);
        HashSet<WebType> webTypeHashSet = new HashSet<>();
        HashSet<Province> provinceHashSet = new HashSet<>();
        SelectTag tag = new SelectTag();
       for(WebType web:webTypes){
           webTypeHashSet.add(web);
       }
       for(Province province:provinces){

           provinceHashSet.add(province);
       }
        ClueUserInfo clueUserInfo = new ClueUserInfo();
        BeanUtils.copyProperties(user,clueUserInfo);
        clueUserInfo.setProvince(provinceHashSet);
        clueUserInfo.setWebType(webTypeHashSet);
        Set<Role> roles = clueRoleService.findRolesByUserId(id);
        clueUserInfo.setRoles(roles);
        return ResultUtil.success(clueUserInfo);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo closeEnable(String id) {
        clueminingUserMapper.closeEnable(id);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    @TargetDataSource(name="cluemining")
    public ResultVo openEnable(String id) {
        clueminingUserMapper.openEnable(id);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public Integer getUsersTotal() {
        return clueminingUserMapper.getUsersTotal();
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo updatePasswordById(String id, String password) {
        Map<String,Object> map = new HashMap<>(2);
        map.put("id",id);
        String username = clueminingUserMapper.selectByPrimaryKey(id).getUsername();
        PasswordHelper helper = new PasswordHelper();
        String md5Password = helper.encryptPassword(username,password);
        map.put("password",md5Password);
        clueminingUserMapper.updatePasswordById(map);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo findChildUsersBySelectvite(String code, Integer page, Integer limit) {
        Map<String,Object> map = new HashMap<>();
        Integer pageSize= (page-1)*limit;
        if(code!=null&&code.length()>0){
            ClueminingUser clueminingUser = clueminingUserMapper.selectByNum(code);
            if(null!=clueminingUser){
                map.put("parentId",clueminingUser.getId());
            }
        }

        map.put("pageSize",pageSize);
        map.put("limit",limit);

        List<ClueChildUserDto> list = clueminingUserMapper.findChildUsersBySelectvite(map);
        for(ClueChildUserDto user1:list){
            String id = user1.getId();
            String createTime1 = user1.getCreateTime();
            String realCreateTime = createTime1.substring(0,createTime1.length()-2);
            List<Province> provinces = clueminingUserMapper.selectProvinceIds(id);
            user1.setProvinces(provinces);
            user1.setCreateTime(realCreateTime);
        }
        PageDto pageDto = new PageDto();
        pageDto.setUsers(list);
        pageDto.setTotal(clueminingUserMapper.findChildUserTotal(map));
        return ResultUtil.success(pageDto);

    }


}
