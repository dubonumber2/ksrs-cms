package com.ksrs.cms.service.impl.clue;

import com.ksrs.cms.config.datasource.TargetDataSource;
import com.ksrs.cms.mapper.clue.ClueUtilMapper;
import com.ksrs.cms.model.clue.Province;
import com.ksrs.cms.model.clue.WebType;
import com.ksrs.cms.service.ClueUtilService;
import com.ksrs.cms.util.ResultUtil;
import com.ksrs.cms.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ClueUtilServiceImpl implements ClueUtilService {

    @Resource
    private ClueUtilMapper clueUtilMapper;

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo selectProvice() {
        List<Province> list = clueUtilMapper.selectProvice();
        return ResultUtil.success(list);
    }

    @Override
    @TargetDataSource(name = "cluemining")
    public ResultVo selectWebType() {
        List<WebType> list = clueUtilMapper.selectWebType();
        return ResultUtil.success(list);
    }
}
