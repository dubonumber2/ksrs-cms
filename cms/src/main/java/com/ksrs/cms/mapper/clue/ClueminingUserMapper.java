package com.ksrs.cms.mapper.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.dto.ClueChildUserDto;
import com.ksrs.cms.dto.ClueUserDto;
import com.ksrs.cms.model.clue.ClueminingUser;
import com.ksrs.cms.resourcesUtil.Province;
import com.ksrs.cms.resourcesUtil.WebType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@TargetDataSource(name = "cluemining")
public interface ClueminingUserMapper<T,V> {
    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    @TargetDataSource(name = "cluemining")
    ClueminingUser findByUsername(@Param("username") String username);

    /**
     * 添加用户
     * @param clueminingUser
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int addUser(ClueminingUser clueminingUser);

    /**
     * 根据用户id删除用户
     * @param id 用户id
     */
    @TargetDataSource(name = "cluemining")
    int deleteUserById(@Param("id") String id);

    /**
     * 根据用户id修改用户信息
     * @param
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int updateUserById(ClueminingUser user);

    /**
     * 根据用户id删除网络推广标签
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int deleteWebTypeById(@Param("userId") String id);

    /**
     * 根据用户id删除绑定的城市信息
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int deleteProvincesById(@Param("userId") String id);
    @TargetDataSource(name = "cluemining")
    int insertWebTypeById(@Param("userId") String id, @Param("webTypeId") Integer webId);
    @TargetDataSource(name = "cluemining")
    int insertProvinceById(@Param("userId") String userId, @Param("provinceId") Integer provinceId);
    @TargetDataSource(name = "cluemining")
    ClueminingUser selectByPrimaryKey(@Param("id") String id);

    /**
     * 分页展示用户列表
     * @param pageSize
     * @param limit
     * @return
     */
    @TargetDataSource(name = "cluemining")
    List<ClueUserDto> findClueUsers(@Param("pageSize") Integer pageSize, @Param("limit") Integer limit);

    /**
     * 总数
     * @return
     */
    @TargetDataSource(name = "cluemining")
    Integer getUsersTotal();

    /**
     * 得到用户绑定的网络推广标签
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    List<WebType> selectWebType(@Param("userId") String id);

    /**
     * 得到用户绑定的城市
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    List<Province> selectProvince(@Param("userId") String id);

    /**
     * 停用
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int closeEnable(@Param("id") String id);

    /**
     * 启用
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int openEnable(@Param("id")String id);

    /**
     * 根据用户id修改密码接口
     * @param map
     * @return
     */
    @TargetDataSource(name = "cluemining")
    int updatePasswordById(Map<String,Object> map);

    /**
     * 根据父id查询出子用户列表
     * @param map
     * @return
     */
    @TargetDataSource(name = "cluemining")
    List<ClueChildUserDto> findChildUsersBySelectvite(Map map);

    /**
     * 对应上面的分页信息
     * @param map
     * @return
     */
    @TargetDataSource(name = "cluemining")
    Integer findChildUserTotal(Map map);

    /**
     * 查询出用户拥有的省份id
     * @param id
     * @return
     */
    @TargetDataSource(name = "cluemining")
    List<Province> selectProvinceIds(@Param("userId")String id);
    @TargetDataSource(name = "cluemining")
    ClueminingUser selectByNum(@Param("usernumber")String userNum);

}
