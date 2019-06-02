package com.ksrs.cms.service.impl;

import com.ksrs.cms.model.Resources;
import com.ksrs.cms.resourcesUtil.Menu;
import com.ksrs.cms.service.ResourcesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ResourcesServiceImplTest {


    @Autowired
    ResourcesService resourcesService;
    @Test
    public void selectByRoleId() {
    }

    @Test
    public void queryAll() {
    }

    @Test
    public void findResourcesByRoleIds() {
        List<Integer> list = new ArrayList<>();
        list.add(17);
        list.add(18);
        resourcesService.deleteResourcesByIds(list);

    }
}