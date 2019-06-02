package com.ksrs.cms.service;

import com.ksrs.cms.model.clue.WebType;
import com.ksrs.cms.vo.ResultVo;

public interface ClueUtilService {
    /**
     * 查询出所有省市
     * @return
     */
    ResultVo selectProvice();

    /**
     * 查询出所有推广标签
     * @return
     */
    ResultVo selectWebType();
}
