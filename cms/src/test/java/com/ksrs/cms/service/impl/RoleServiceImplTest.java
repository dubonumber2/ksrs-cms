package com.ksrs.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.ksrs.cms.service.RoleService;
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
public class RoleServiceImplTest {

    @Autowired
    RoleService roleService;


    @Test
    public void findRoleByUserId() {
      ResultVo resultVo = roleService.findAllRole();
        System.out.println(JSON.toJSONString(resultVo));
    }

    @Test
    public void select(){
        List<Integer> list = new ArrayList<>();
        for(int i=1;i<=5;i++){
            list.add(i);
        }
        roleService.insertRoleResources(10,list);
    }
}