package com.ksrs.clue.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ksrs.clue.config.shiro.AuthRealm;
import com.ksrs.clue.dto.*;
import com.ksrs.clue.enums.ResultEnum;
import com.ksrs.clue.exception.SysException;
import com.ksrs.clue.mapper.ClueminingUserMapper;
import com.ksrs.clue.mapper.RoleMapper;
import com.ksrs.clue.mapper.UserRoleMapper;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.model.UserRole;
import com.ksrs.clue.resourcesUtil.*;
import com.ksrs.clue.service.ClueminingUserService;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.util.HttpUtil;
import com.ksrs.clue.util.PasswordHelper;
import com.ksrs.clue.util.ResultUtil;
import com.ksrs.clue.vo.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
@EnableScheduling
public class ClueminingUserServiceImpl implements ClueminingUserService {
    @Resource
    ClueminingUserMapper clueminingUserMapper;

    @Resource
    UserRoleMapper userRoleMapper;

    @Resource
    RoleMapper roleMapper;

    @Autowired
    RoleService roleService;

    @Autowired
    RedisSessionDAO redisSessionDAO;



    private static final Logger logger = LoggerFactory.getLogger(ClueminingUserServiceImpl.class);

    @Override
    public ClueminingUser findByUsername(String username) {

        return clueminingUserMapper.findByUsername(username);
    }

    @Override
    @Transactional
    public ResultVo addUser(ChildUserDto childUserDto,List<Integer> provinces) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser clueminingUser = (ClueminingUser) subject.getPrincipal();
        String parentId = clueminingUser.getId();
        ClueminingUser realUser = clueminingUserMapper.selectByPrimaryKey(parentId);
        PasswordHelper helper = new PasswordHelper();
        String mdPassowrd = helper.encryptPassword(childUserDto.getUsername(),childUserDto.getPassword());
        String dateTime = String.valueOf(System.currentTimeMillis());
        String userNumber = helper.encryptPassword(childUserDto.getUsername(),dateTime);
        childUserDto.setPassword(mdPassowrd);
        childUserDto.setUserNumber(userNumber);

        Integer dowloadAble = realUser.getDowloadAble();
        //Integer usedowload = this.getSumDownload();
        // 开通子账号时，判断当前所有子账号的下载量之和是否大于父账号的可分配下载量
        Map map = new HashMap();
        map.put("parentId",realUser.getId());
        map.put("pageSize",0);
        map.put("limit",clueminingUserMapper.findChildUserTotalSelective(map));
        List<ClueUserDto> childUser = clueminingUserMapper.findChildUsersBySelectvite(map);
        Integer sumDowload = 0;
        for(ClueUserDto child:childUser){
            sumDowload+=child.getDowloadAble();
        }
        if(childUserDto.getDowloadAble()>dowloadAble){
            throw new SysException(ResultEnum.DOWLOAD_NOT_ENOUGH);
        }

        if(sumDowload+childUserDto.getDowloadAble()>dowloadAble){
            throw new SysException(ResultEnum.DOWLOAD_NOT_ENOUGH);
        }
        clueminingUser.setId(null);
        clueminingUser.setCreateTime(null);
        clueminingUser.setParentId(parentId);
        // 把父账号其余信息覆盖掉替换成用户所填入的子账号的信息
        BeanUtils.copyProperties(childUserDto,clueminingUser);
        try {
            clueminingUserMapper.addUser(clueminingUser);
            DownLoadDto downLoadDto = new DownLoadDto();
            downLoadDto.setUserId(userNumber);
            downLoadDto.setDownloadAble(clueminingUser.getDowloadAble());
            clueminingUserMapper.insertDowloadUpdate(downLoadDto);
        }catch (DuplicateKeyException e){
            logger.error("【系统异常】添加用户失败,该{}用户名已存在",clueminingUser.getUsername());
            throw new SysException(ResultEnum.USER_EXIST);
        }
        /*List<Integer> roleIds = roleService.findRoleIdByUserId(parentId);
        if(!CollectionUtils.isEmpty(roleIds)){
            // 插入角色信息
            for(Integer roleId:roleIds){
                UserRole userRole= new UserRole();
                userRole.setUserid(userNumber);
                userRole.setRoleid(roleId);
                userRoleMapper.insert(userRole);
            }
        }*/
        UserRole userRole= new UserRole();
        userRole.setUserid(userNumber);
        userRole.setRoleid(21);
        userRoleMapper.insert(userRole);
       List<WebType> webType = clueminingUserMapper.selectWebTypeIds(parentId);

        if(!CollectionUtils.isEmpty(webType)){
            // 插入网络推广标签信息
            for(WebType webTypeId:webType){
                this.insertWebTypeById(userNumber,webTypeId.getId());
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
    public ResultVo deleteUserById(String id) {
        clueminingUserMapper.deleteUserById(id);
        return ResultUtil.success(ResultEnum.DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResultVo updateUserById(UpdateClueUserDto user,  List<Integer> provinces) {
        ClueminingUser clueminingUser = new ClueminingUser();
        BeanUtils.copyProperties(user,clueminingUser);
        if(user != null){
            clueminingUserMapper.updateUserById(clueminingUser);
        }

        if(!CollectionUtils.isEmpty(provinces)){
            this.deleteProvincesById(clueminingUser.getId());
            for(Integer provinceId:provinces){
                this.insertProvinceById(clueminingUser.getId(),provinceId);
            }

        }


        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public int deleteWebTypeById(String id) {
        clueminingUserMapper.deleteWebTypeById(id);
        return 0;
    }

    @Override
    public int deleteProvincesById(String id) {
        clueminingUserMapper.deleteProvincesById(id);
        return 0;
    }

    @Override
    public int insertWebTypeById(String id, Integer webId) {
        clueminingUserMapper.insertWebTypeById(id,webId);
        return 0;
    }

    @Override
    public int insertProvinceById(String  userId, Integer provinceId) {
        clueminingUserMapper.insertProvinceById(userId, provinceId);
        return 0;
    }

    @Override
    public ResultVo findClueUsers(Integer page, Integer limit) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser)subject.getPrincipal();
        Integer pageSize = (page-1)*limit;
        List<ClueUserDto> list = clueminingUserMapper.findClueUsers(pageSize,limit,user.getId());

        for(ClueUserDto user1 : list){
            String id = user1.getId();
            String createTime = user1.getCreateTime();
            String realCreateTime = createTime.substring(0,createTime.length()-2);
            List<Province> provinces = clueminingUserMapper.selectProvinceIds(id);
            user1.setProvinces(provinces);
            user1.setCreateTime(realCreateTime);
        }
        PageDto pageDto = new PageDto();
        pageDto.setUsers(list);
        pageDto.setTotal(clueminingUserMapper.getUsersTotal(user.getId()));
        return ResultUtil.success(pageDto);
    }
    @Override
    public ResultVo getDetailClueUserMsg(String id){
        ClueminingUser user = clueminingUserMapper.selectByPrimaryKey(id);
        List<String> webTypes = clueminingUserMapper.selectWebType(id);
        List<String> provinces = clueminingUserMapper.selectProvince(id);
        HashSet<WebType> webTypeHashSet = new HashSet<>();
        HashSet<Province> provinceHashSet = new HashSet<>();
        SelectTag tag = new SelectTag();
       for(String web:webTypes){
           WebType webType = new WebType();
           webType.setWebTypeName(web);
           webTypeHashSet.add(webType);
       }
       tag.setWebType(webTypeHashSet);
       for(String province:provinces){
           Province province1 = new Province();
           province1.setProvinceName(province);
           provinceHashSet.add(province1);
       }
       tag.setProvince(provinceHashSet);
        ClueUserInfo clueUserInfo = new ClueUserInfo();
        BeanUtils.copyProperties(user,clueUserInfo);
        clueUserInfo.setSelectTag(tag);
        return ResultUtil.success(clueUserInfo);
    }

    @Override
    public ResultVo updatePasswordById(String oldPassword,String newPassword) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        PasswordHelper helper = new PasswordHelper();
        if(!this.identifyPassword(oldPassword)){
            return ResultUtil.fail(ResultEnum.PASSWORD_ERROR);
        }
        String id = user.getId();
        String md5NewPassword = helper.encryptPassword(user.getUsername(),newPassword);
        Map<String,Object> map = new HashMap<>(2);
        map.put("id",id);
        map.put("password",md5NewPassword);
        clueminingUserMapper.updatePasswordById(map);
        subject.logout();
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public boolean identifyPassword(String password) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        String username = user.getUsername();
        String oldPassword = user.getPassword();
        PasswordHelper helper = new PasswordHelper();
        String md5Password = helper.encryptPassword(username,password);
        if(oldPassword.equals("110")){
            oldPassword = this.findOldUserPassword(username);
            md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        }

        if(oldPassword.equals(md5Password)){
            return true;
        }
        return false;
    }

    /**
     * 用户登录之后得到地域信息及推广信息
     * @return
     */
    @Override
    @Transactional
    public ResultVo getLoginUserMessage() {
        ClueUserInfo userInfo = new ClueUserInfo();
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        ClueminingUser realUser = clueminingUserMapper.selectByPrimaryKey(user.getId());
        BeanUtils.copyProperties(realUser,userInfo);
        String userId = user.getId();
        List<Province> provinces = clueminingUserMapper.selectProvinceIds(userId);
        List<Integer> provinceIds = new ArrayList<>();
        SelectTag selectTag = new SelectTag();
        if(!CollectionUtils.isEmpty(provinces)){
            for(Province a:provinces){
                provinceIds.add(a.getId());
                Set<City> citySet = new TreeSet<City>(new Comparator<City>() {
                    @Override
                    public int compare(City o1, City o2) {
                        return o1.getId()-o2.getId();
                    }
                });

                Set<City> citySet1 = clueminingUserMapper.selectProvinceCity(a.getId());
                for(City city:citySet1){
                    citySet.add(city);
                }
                a.setCitys(citySet);
            }
            List<Province> unselect = clueminingUserMapper.selectProvinceIdsNotInList(provinceIds);
            for(Province a: unselect){
                provinces.add(a);
            }
            Set<Province> provinceSet = new TreeSet<Province>(new Comparator<Province>(){
                @Override
                public int compare(Province o1, Province o2) {
                    return o1.getSort()-o2.getSort();
                }
            });
            for(Province province:provinces){
                provinceSet.add(province);
            }
            selectTag.setProvince(provinceSet);
        }else{
            Set<Province> provinceSet = new TreeSet<Province>(new Comparator<Province>(){
                @Override
                public int compare(Province o1, Province o2) {
                    return o1.getSort()-o2.getSort();
                }
            });
            Set<Province> allProvince = clueminingUserMapper.selectAllProvince();
            for(Province province:allProvince){
                provinceSet.add(province);
            }
            selectTag.setProvince(provinceSet);
        }

        List<WebType> webTypes = clueminingUserMapper.selectWebTypeIds(userId);
        if(!CollectionUtils.isEmpty(webTypes)){
            List<Integer> webIds = new ArrayList<>();
            for(WebType id:webTypes){
                webIds.add(id.getId());
            }
            List<WebType> webTypes1 = clueminingUserMapper.selectInfoWebType(webIds);


            Set<WebType> webTypeSet = new HashSet<>();
            for(WebType webType:webTypes1){
                webTypeSet.add(webType);
            }

            selectTag.setWebType(webTypeSet);
        }else{
            selectTag.setWebType(clueminingUserMapper.selectAllWebType());
        }


        userInfo.setStartTime(this.timeUtil(userInfo.getStartTime()));
        userInfo.setStopTime(this.timeUtil(userInfo.getStopTime()));
        userInfo.setSelectTag(selectTag);
        return ResultUtil.success(userInfo);
    }

    @Override
    public ResultVo identifyParentId() {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        String parentId = user.getParentId();
        if(!parentId.equals("0")){
            //如果parentid不等于0 则返回false
            return ResultUtil.success("false");
        }
        return ResultUtil.success("true");
    }

    @Override
    public ClueminingUser selectByPrimaryKey(String id) {
        return clueminingUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 如果登录的是子账号，则去获取所属父账号的地域信息
     * 否则获取本身的地域信息
     * @param id
     * @return
     */
    @Override
    public ResultVo selectProvicnesByUserId(String id) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        List<Province> provinces=null;
        if(user.getParentId().equals("0") && user.getParentId().equals("-1")){
            provinces = clueminingUserMapper.selectProvinceIds(user.getId());
        }else{
            provinces = clueminingUserMapper.selectProvinceIds(user.getParentId());
        }
         provinces = clueminingUserMapper.selectProvinceIds(user.getParentId());
        Set<Province> provinceSet = new TreeSet<>(new Comparator<Province>() {
            @Override
            public int compare(Province o1, Province o2) {
                return o1.getSort()-o2.getSort();
            }
        });
        for(Province province:provinces){
            provinceSet.add(province);
        }

        return ResultUtil.success(provinceSet);
    }

    @Override
    public ResultVo getProvinceById(){
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        String id = user.getId();
        ProvicinceDto dto = new ProvicinceDto();
        dto.setData(clueminingUserMapper.selectProvinceIds(id));
        return ResultUtil.success(dto);
    }

    @Override
    public ResultVo closeIsPoint(String id) {
        clueminingUserMapper.closeIsPoint(id);
        return ResultUtil.success("开启不再提示");
    }

    @Override
    public ResultVo findIsPoint(String id) {
        int ispoint = clueminingUserMapper.findIsPoint(id);
        return ResultUtil.success(ispoint);
    }

    public String timeUtil(String time){
        String newTime = time.substring(0,time.length()-2);
        return newTime;
    }

    @Override
    @Transactional
    public ResultVo updateDowload(UpdateClueUserDto user,List<Integer> provinces){
        Subject subject = SecurityUtils.getSubject();

        //当前登录用户,父账号
        ClueminingUser user1 = (ClueminingUser) subject.getPrincipal();
        //拿到父账号id
        String parentId = user1.getId();
        //拿到数据库中的当前用户信息(没走缓存)
        ClueminingUser realUser = clueminingUserMapper.selectByPrimaryKey(parentId);
        //父账号当前下载量
        Integer parDowload = realUser.getDowloadAble();
        //要开通的下载量
        Integer ktDowload = user.getDowloadAble();
        //要开通账号的id
        String childId = user.getId();
        //该子账号的信息
        ClueminingUser childUser = clueminingUserMapper.selectByPrimaryKey(childId);
        //获取数据库中已有的该子账号的下载量
        Integer childDow = clueminingUserMapper.findDowloadByUserId(childUser.getId()).getDownloadAble();
        //如果用户要开通的下载量大于父账号下载量，直接返回
        // 子账户当日已下载的量之和
        //Integer usedowload = this.getSumDownload();

        if(ktDowload>parDowload){
            throw new SysException(ResultEnum.DOWLOAD_NOT_ENOUGH);
        }
        if(ktDowload<=childDow){

            user.setId(childId);
            user.setDowloadAble(null);
            this.updateUserById(user,provinces);
            if(this.findDownloadUserIds().contains(childId)){
                DownLoadDto downLoadDto = new DownLoadDto();
                downLoadDto.setUserId(childId);
                downLoadDto.setDownloadAble(ktDowload);
                this.updateDowloadUpdate(downLoadDto);
            }else{
                DownLoadDto downLoadDto = new DownLoadDto();
                downLoadDto.setUserId(childId);
                downLoadDto.setDownloadAble(ktDowload);
                this.insertDowloadUpdate(downLoadDto);
            }
        }else{
            Map map = new HashMap();
            map.put("parentId",parentId);
            map.put("pageSize",0);
            map.put("limit",clueminingUserMapper.findChildUserTotalSelective(map));
            List<ClueUserDto> childUsers = clueminingUserMapper.findChildUsersBySelectvite(map);
            Integer sumDowload = 0;
            if(!CollectionUtils.isEmpty(childUsers)){
                for(ClueUserDto child:childUsers){
                    sumDowload+=this.findDowloadByUserId(child.getId()).getDownloadAble();
                }
            }

            Integer tmp = ktDowload-childDow+sumDowload;
            if(tmp>parDowload){
                throw new SysException(ResultEnum.DOWLOAD_NOT_ENOUGH);
            }else{

                user.setId(childId);
                user.setDowloadAble(null);
                this.updateUserById(user,provinces);
                if(this.findDownloadUserIds().contains(childId)){
                    DownLoadDto downLoadDto = new DownLoadDto();
                    downLoadDto.setUserId(childId);
                    downLoadDto.setDownloadAble(ktDowload);
                    this.updateDowloadUpdate(downLoadDto);
                }else{
                    DownLoadDto downLoadDto = new DownLoadDto();
                    downLoadDto.setUserId(childId);
                    downLoadDto.setDownloadAble(ktDowload);
                    this.insertDowloadUpdate(downLoadDto);
                }
            }
        }


        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo closeEnable(String id) {
        clueminingUserMapper.closeEnable(id);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo openEnable(String id) {
        clueminingUserMapper.openEnable(id);
        return ResultUtil.success(ResultEnum.UPDATE_SUCCESS);
    }

    @Override
    public ResultVo findChildUsersBySelectvite(String username, String realName, String createTime, Integer page, Integer limit) {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        String parentId = user.getId();

        Map<String,Object> map = new HashMap<>();
        Integer pageSize= (page-1)*limit;
        map.put("parentId",parentId);
        map.put("pageSize",pageSize);
        map.put("limit",limit);
        map.put("username",username);
        map.put("realName",realName);
        map.put("createTime",createTime);
        List<ClueUserDto> list = clueminingUserMapper.findChildUsersBySelectvite(map);
        for(ClueUserDto user1:list){
            String id = user1.getId();
            String createTime1 = user1.getCreateTime();
            String realCreateTime = createTime1.substring(0,createTime1.length()-2);
            List<Province> provinces = clueminingUserMapper.selectProvinceIds(id);
            user1.setProvinces(provinces);
            user1.setCreateTime(realCreateTime);
        }
        PageDto pageDto = new PageDto();
        pageDto.setUsers(list);
        pageDto.setTotal(clueminingUserMapper.findChildUserTotalSelective(map));
        return ResultUtil.success(pageDto);
    }

    @Override
    public ResultVo findDowloadAble() {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        ClueminingUser real = clueminingUserMapper.selectByPrimaryKey(user.getId());
        Map map = new HashMap();
        map.put("parentId",user.getId());
        map.put("pageSize",0);
        map.put("limit",clueminingUserMapper.findChildUserTotalSelective(map));
        List<ClueUserDto> childUser = clueminingUserMapper.findChildUsersBySelectvite(map);
        Integer sumDowload = 0;
        if(!CollectionUtils.isEmpty(childUser)){
            for(ClueUserDto child:childUser){
                DownLoadDto downLoadDto = this.findDowloadByUserId(child.getId());
                sumDowload+=downLoadDto.getDownloadAble();
            }
        }

        Map mapRes = new HashMap();
        mapRes.put("dowloadAble",real.getDowloadAble()-sumDowload);
        return ResultUtil.success(mapRes);
    }

    @Override
    public String findOldUserPassword(String username) {
        return clueminingUserMapper.findOldUserPassword(username);
    }

    @Override
    public ResultVo isChagePassword() {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        String password = clueminingUserMapper.findByUsername(user.getUsername()).getPassword();
        if(password.equals("110")){
            return ResultUtil.success(true);
        }
        return ResultUtil.success(false);
    }

    @Override
    public List<String> childUserId() {
        Subject subject = SecurityUtils.getSubject();
        ClueminingUser user = (ClueminingUser) subject.getPrincipal();
        ClueminingUser real = clueminingUserMapper.selectByPrimaryKey(user.getId());
        Map map = new HashMap();
        map.put("parentId",user.getId());
        map.put("pageSize",0);
        map.put("limit",clueminingUserMapper.findChildUserTotalSelective(map));
        List<ClueUserDto> childUser = clueminingUserMapper.findChildUsersBySelectvite(map);
        List<String> childUserIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(childUser)){
            for(ClueUserDto child:childUser){
                childUserIds.add(child.getId());
            }
        }
        return childUserIds;
    }



    @Override
    public List<String> findDownloadUserIds() {
        return clueminingUserMapper.findDownloadUserIds();
    }

    @Override
    public void insertDowloadUpdate(DownLoadDto downLoadDto) {
        clueminingUserMapper.insertDowloadUpdate(downLoadDto);
    }

    @Override
    public void updateDowloadUpdate(DownLoadDto downLoadDto) {
        clueminingUserMapper.updateDowloadUpdate(downLoadDto);
    }

    @Override
    public List<DownLoadDto> findYesterday() {
        return clueminingUserMapper.findYesterday();
    }

    @Override
    @Scheduled(cron = "0 10 0  * * ?")
    public void updateSchedule() {
        List<DownLoadDto> downLoadDtos = this.findYesterday();
        downLoadDtos.forEach(downLoadDto ->{
            UpdateClueUserDto user = new UpdateClueUserDto();
            user.setId(downLoadDto.getUserId());
            user.setDowloadAble(downLoadDto.getDownloadAble());
            this.updateUserById(user,null);
            logger.info("【定时任务】更新下载量,userid:{},download:{}",downLoadDto.getUserId(),downLoadDto.getDownloadAble());
        });
        logger.info("【定时任务】更新下载量完成");

    }

    @Override
    public DownLoadDto findDowloadByUserId(String id) {
        return clueminingUserMapper.findDowloadByUserId(id);
    }
}
