package com.ksrs.cms.service.impl.clue;

import com.alibaba.fastjson.JSON;
import com.ksrs.cms.service.ClueUtilService;
import com.ksrs.cms.vo.ResultVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ClueUtilServiceImplTest {

    @Autowired
    private ClueUtilService clueUtilService;
    @Test
    public void test(){
        ResultVo test = clueUtilService.selectProvice();
        System.out.println(JSON.toJSONString(test));
    }

}