package com.ksrs.cms.service;


import com.ksrs.cms.dto.ClueChildUserDto;
import com.ksrs.cms.dto.UpdateClueUserDto;
import com.ksrs.cms.model.clue.ClueminingUser;
import com.ksrs.cms.vo.ResultVo;
import java.util.List;

public interface ClueminingUserService {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    ClueminingUser findByUsername(String username);

    /**
     * 添加用户(包括用户信息，角色及其它信息)
     * @param clueminingUser
     * @return
     */
    ResultVo addUser(ClueminingUser clueminingUser, List<Integer> roleIds, List<Integer> webTypes, List<Integer> provinces);

    /**
     * 根据用户id删除用户(逻辑删除)
     * @param id 用户id
     */
    ResultVo deleteUserById(String id);

    /**
     * 修改用户信息及绑定的网络推广标签，城市，角色信息
     * @param user
     * @param roleIds
     * @param webTypes
     * @param provinces
     * @return
     */
    ResultVo updateUserById(UpdateClueUserDto user, List<Integer> roleIds, List<Integer> webTypes, List<Integer> provinces);

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
    int insertWebTypeById(String id, Integer webId);

    /**
     * 插入用户的城市信息
     * @param userId
     * @param provinceId
     * @return
     */
    int insertProvinceById(String userId, Integer provinceId);

    /**
     * 分页展示用户列表
     * @param page
     * @param limit
     * @return
     */
    ResultVo findClueUsers(Integer page, Integer limit);

    /**
     * 根据用户id查看用户详细信息
     * @param id
     * @return
     */
    ResultVo getDetailClueUserMsg(String id);

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

    // 分页总数
    Integer getUsersTotal();

    /**
     * 根据用户id修改密码接口
     * @param
     * @return
     */
    ResultVo updatePasswordById(String id,String password);

    /**
     * 查询子账号列表
     * @param page
     * @param limit
     * @return
     */
    ResultVo findChildUsersBySelectvite(String code,Integer page,Integer limit);





}
