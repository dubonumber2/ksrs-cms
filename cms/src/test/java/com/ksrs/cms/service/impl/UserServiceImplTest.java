package com.ksrs.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ksrs.cms.dto.PageDto;
import com.ksrs.cms.dto.UserDto;
import com.ksrs.cms.mapper.UserMapper;
import com.ksrs.cms.model.User;
import com.ksrs.cms.service.RoleService;
import com.ksrs.cms.service.UserService;
import com.ksrs.cms.vo.ResultVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Test
    public void findByUsername() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void findUsers() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        for(int i=0;i<list.size();i++){
            roleService.inertUserRoles(11,list.get(i));
        }
    }

    @Test
    public void updateByPrimaryKeySelective(){
        User user = new User();
        user.setId(14);
        user.setRealName("赵无极");
        List<Integer> roleIds = new ArrayList<>();
        roleIds.add(2);
        roleIds.add(3);
        userService.updateByPrimaryKeySelective(user,roleIds);
    }

    @Test
    public void selectByPrimaryKey(){
        ResultVo resultVo =userService.selectByPrimaryKey(14);
        System.out.println(JSON.toJSONString(resultVo));
    }

}