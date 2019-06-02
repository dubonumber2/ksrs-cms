package com.ksrs.cms.mapper.clue;

import com.ksrs.cms.model.clue.Province;
import com.ksrs.cms.model.clue.WebType;

import java.util.List;

public interface ClueUtilMapper {

    /**
     * 查询出所有的省份
     * @return
     */
    List<Province> selectProvice();

    /**
     * 查询出所有推广标签
     * @return
     */
    List<WebType> selectWebType();
}
