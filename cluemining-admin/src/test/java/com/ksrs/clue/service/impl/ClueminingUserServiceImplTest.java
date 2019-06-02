package com.ksrs.clue.service.impl;

import com.ksrs.clue.dto.DownLoadDto;
import com.ksrs.clue.dto.UpdateClueUserDto;
import com.ksrs.clue.service.ClueminingUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClueminingUserServiceImplTest {
    @Autowired
    ClueminingUserService clueminingUserService;

    private static final Logger logger = LoggerFactory.getLogger(ClueminingUserServiceImplTest.class);
    @Test
    public void updateUserById(){
        UpdateClueUserDto updateClueUserDto = new UpdateClueUserDto();
        //updateClueUserDto.setId(16);
        updateClueUserDto.setDowloadAble(500);
    }

    @Test
    public void updateYes(){
        List<DownLoadDto> downLoadDtos = clueminingUserService.findYesterday();
        downLoadDtos.forEach(downLoadDto ->{
            UpdateClueUserDto user = new UpdateClueUserDto();
            user.setId(downLoadDto.getUserId());
            user.setDowloadAble(downLoadDto.getDownloadAble());
            clueminingUserService.updateUserById(user,null);
            logger.info("【定时任务】更新下载量,userid:{},download:{}",downLoadDto.getUserId(),downLoadDto.getDownloadAble());
        });
    }
}