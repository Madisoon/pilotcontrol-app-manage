package com.syx.pilotcontrol.module.config.web;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Msater Zg on 2017/6/14.
 */
@RestController
@RequestMapping(value = "/config")
@Api(value = "PrivateNumberController", description = "获取到私人账号的方法")
public class PrivateNumberController {
    @RequestMapping(value = "/addQq", method = RequestMethod.POST)
    @ApiOperation(value = "addQq", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户ID", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "adminJudge", value = "用户是否为超级管理员", required = true, dataType = "STRING")
    })
    public String addQq(@RequestParam("qqDate") String qqDate) {
        return "";
    }
}
