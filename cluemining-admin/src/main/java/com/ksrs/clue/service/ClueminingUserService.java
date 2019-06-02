package com.ksrs.clue.service;


import com.ksrs.clue.dto.ChildUserDto;
import com.ksrs.clue.dto.ClueUserDto;
import com.ksrs.clue.dto.DownLoadDto;
import com.ksrs.clue.dto.UpdateClueUserDto;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.resourcesUtil.City;
import com.ksrs.clue.resourcesUtil.Province;
import com.ksrs.clue.resourcesUtil.WebType;
import com.ksrs.clue.vo.ResultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueminingUserService {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    ClueminingUser findByUsername(String username);

    /**
     * 添加用户(包括用户信息，角色及其它信息)
     * @param childUserDto
     * @return
     */
    ResultVo addUser(ChildUserDto childUserDto,List<Integer> provinces);

    /**
     * 根据用户id删除用户(逻辑删除)
     * @param id 用户id
     */
    ResultVo deleteUserById(String id);

    /**
     * 修改用户信息及绑定的网络推广标签，城市，角色信息
     * @param user
     * @param provinces
     * @return
     */
    ResultVo updateUserById(UpdateClueUserDto user, List<Integer> provinces);

    /**
     * 根据用户id删除网络推广标签
     * @param id
     * @return
     */
    int deleteWebTypeById(String id);

    /**
     * 根据用户id删除绑定的城市信息
     * @param id
     * @return
     */
    int deleteProvincesById(String id);

    /**
     * 插入用户绑定的网络推广标签信息
     * @param id
     * @param webId
     * @return
     */
    int insertWebTypeById(String id,Integer webId);

    /**
     * 插入用户的城市信息
     * @param userId
     * @param provinceId
     * @return
     */
    int insertProvinceById(String userId,Integer provinceId);

    /**
     * 分页展示用户列表
     * @param page
     * @param limit
     * @return
     */
    ResultVo findClueUsers(Integer page,Integer limit);

    /**
     * 根据用户id查看用户详细信息
     * @param id
     * @return
     */
    ResultVo getDetailClueUserMsg(String id);

    /**
     * 根据用户id修改密码接口
     *
     */
    ResultVo updatePasswordById(String oldPassword,String newPassword);

    /**
     * 验证密码接口
     * @param password
     * @return
     */
    boolean identifyPassword(String password);

    /**
     * 得到登录用户信息
     * @return
     */
    ResultVo getLoginUserMessage();

    /**
     * 根据登录用户验证是否为父账号
     * @return
     */
    ResultVo identifyParentId();

    /**
     * 根据userid查询用户信息
     * @param id
     * @return
     */
    ClueminingUser selectByPrimaryKey(String id);

    /**
     * 根据id得到用户的地域信息
     * @param id
     * @return
     */
    ResultVo selectProvicnesByUserId(String id);

    ResultVo getProvinceById();

    /**
     * 不再提示
     * @param id
     * @return
     */
    ResultVo closeIsPoint(String id);

    /**
     * 判断是否提示
     * @param id
     * @return
     */
    ResultVo findIsPoint(String id);

    /**
     * 修改子账号信息接口
     * @param user
     * @param provinces
     * @return
     */
    ResultVo updateDowload(UpdateClueUserDto user,List<Integer> provinces);

    /**
     * 停用
     * @param id
     * @return
     */
    ResultVo closeEnable( String id);

    /**
     * 启用
     * @param id
     * @return
     */
    ResultVo openEnable(String id);

    /**
     * 根据条件搜索子账号
     * @return
     */
    ResultVo findChildUsersBySelectvite(String username,String realName,String createTime,Integer page,Integer limit );

    /**
     * 获取最新下载量的接口
     * @return
     */
    ResultVo findDowloadAble();

    /**
     * 得到老用户的密码
     * @param username
     * @return
     */
    String findOldUserPassword(String username);

    /**
     * 判断是否改密码
     * @return
     */
    ResultVo isChagePassword();

    /**
     * 所有子账号的id
     * @return
     */
    List<String> childUserId();


    /**
     * 修改用户下载量时得到用户的所有id
     * @return
     */
    List<String> findDownloadUserIds();
    /**
     * 有则更新无则插入
     * @param downLoadDto
     */
    void insertDowloadUpdate(DownLoadDto downLoadDto);

    /**
     * 有则更新无则插入
     * @param downLoadDto
     */
    void updateDowloadUpdate(DownLoadDto downLoadDto);

    /**
     * 得到当前时间戳为前一天的下载量(即昨日修改的下载量)
     * @return
     */
    List<DownLoadDto> findYesterday();

    /**
     * 定时任务，更新昨日修改的下载量
     */
    void updateSchedule();

    /**
     * 根据用户id查询出下载量(次日生效的)
     * @param id
     * @return
     */
    DownLoadDto findDowloadByUserId(String id);


}
