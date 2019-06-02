package com.ksrs.clue.service.impl;

import com.ksrs.clue.model.User;
import com.ksrs.clue.service.RoleService;
import com.ksrs.clue.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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
           // roleService.inertUserRoles(11,list.get(i));
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

}