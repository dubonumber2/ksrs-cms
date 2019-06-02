package com.ksrs.cms.controller.clue;

import com.ksrs.cms.service.ClueUtilService;
import com.ksrs.cms.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clue")
@Api(value = "线索挖掘回显相关信息模块")
@Validated
public class ClueUtilController {

    @Autowired
    private ClueUtilService clueUtilService;

    @GetMapping("/province")
    @ApiOperation(value = "查询出所有省份")
    public ResultVo selectProvince(){
        return clueUtilService.selectProvice();
    }

    @GetMapping("/webtype")
    @ApiOperation(value = "查询出所有网络推广标签")
    public ResultVo selectWebType(){
        return clueUtilService.selectWebType();
    }


}
