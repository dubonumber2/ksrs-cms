package com.ksrs.cms.service.impl.clue;

import com.alibaba.fastjson.JSON;
import com.ksrs.cms.dto.ClueChildUserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ClueminingUserServiceImplTest {

    @Autowired
    ClueminingUserServiceImpl clueminingUserService;

    @Test
    public void findChildUsersBySelectvite() {
        ClueChildUserDto clueChildUserDto = new ClueChildUserDto();
        clueChildUserDto.setUsername("ydchild1");
        //System.out.println(JSON.toJSONString(clueminingUserService.findChildUsersBySelectvite(clueChildUserDto)));
    }
}