package com.ksrs.clue.mapper;

import com.ksrs.clue.dto.ClueUserDto;
import com.ksrs.clue.dto.DownLoadDto;
import com.ksrs.clue.model.ClueminingUser;
import com.ksrs.clue.resourcesUtil.City;
import com.ksrs.clue.resourcesUtil.Province;
import com.ksrs.clue.resourcesUtil.WebType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ClueminingUserMapper<T,V> {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    ClueminingUser findByUsername(@Param("username") String username);

    /**
     * 添加用户
     * @param clueminingUser
     * @return
     */
    int addUser(ClueminingUser clueminingUser);

    /**
     * 根据用户id删除用户
     * @param id 用户id
     */
    int deleteUserById(@Param("id") String id);

    /**
     * 根据用户id修改用户信息
     * @param
     * @return
     */
    int updateUserById(ClueminingUser user);

    /**
     * 根据用户id删除网络推广标签
     * @param id
     * @return
     */
    int deleteWebTypeById(@Param("userId") String id);

    /**
     * 根据用户id删除绑定的城市信息
     * @param id
     * @return
     */
    int deleteProvincesById(@Param("userId")String id);

    int insertWebTypeById(@Param("userId")String id,@Param("webTypeId")Integer webId);

    int insertProvinceById(@Param("userId")String userId,@Param("provinceId")Integer provinceId);

    ClueminingUser selectByPrimaryKey(@Param("id")String id);

    /**
     * 分页展示用户列表
     * @param pageSize
     * @param limit
     * @return
     */
    List<ClueUserDto> findClueUsers(@Param("pageSize")Integer pageSize, @Param("limit") Integer limit,@Param("userId")String id);
    // 分页总数
    Integer getUsersTotal(@Param("userId")String id);

    /**
     * 得到用户绑定的网络推广标签
     * @param id
     * @return
     */
    List<String> selectWebType(@Param("userId")String id);

    /**
     * 得到用户绑定的城市
     * @param id
     * @return
     */
    List<String> selectProvince(@Param("userId")String id);

    /**
     * 根据用户id修改密码接口
     * @param map
     */
    void updatePasswordById(Map<String,Object> map);

    /**
     * 查询出用户拥有的省份id
     * @param id
     * @return
     */
    List<Province> selectProvinceIds(@Param("userId")String id);

    /**
     *
     * @param ids
     * @return
     */
    List<Province> selectProvinceIdsNotInList(@Param("list") List<Integer> ids);

    /**
     *
     * @param id
     * @return
     */
    List<WebType> selectWebTypeIds(@Param("userId")String id);

    /**
     * 返回给前端的webtype
     * @param ids
     * @return
     */
    List<WebType> selectInfoWebType(@Param("list")List<Integer> ids);

    /**
     *
     * @param id
     * @return
     */
    Set<City> selectProvinceCity(@Param("provinceId")Integer id);

    Set<Province> selectAllProvince();

    Set<WebType> selectAllWebType();

    /**
     * 不再提示
     * @param id
     * @return
     */
    int closeIsPoint(@Param("id")String id);

    /**
     * 判断是否提示
     * @param id
     * @return
     */
    int findIsPoint(@Param("id")String id);

    /**
     * 停用
     * @param id
     * @return
     */
    int closeEnable(@Param("id") String id);

    /**
     * 启用
     * @param id
     * @return
     */
    int openEnable(@Param("id")String id);

    /**
     * 根据条件搜索子账号
     * @param map
     * @return
     */
    List<ClueUserDto> findChildUsersBySelectvite(Map<T,V> map);

    /**
     * 得到分页总数
     * @param map
     * @return
     */
    Integer findChildUserTotalSelective(Map<T,V> map);

    /**
     * 得到老用户的密码
     * @param username
     * @return
     */
    String findOldUserPassword(@Param("username") String username);

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
     * 根据用户id查询出下载量(次日生效的)
     * @param id
     * @return
     */
    DownLoadDto findDowloadByUserId(@Param("userId")String id);
}
