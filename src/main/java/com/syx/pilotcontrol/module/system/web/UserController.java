package com.syx.pilotcontrol.module.system.web;

import com.syx.pilotcontrol.config.JwtConfig;
import com.syx.pilotcontrol.module.system.service.IUserService;
import com.syx.pilotcontrol.utils.WebTokenUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by Msater Zg on 2017/6/14.
 */
@RestController
@RequestMapping(value = "/system")
@Api(value = "UserController", description = "管理系统用户的api")
public class UserController {

    @Autowired
    IUserService iUserService;
    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    WebTokenUtil webTokenUtil;

    @RequestMapping(value = "/insertSysUser", method = RequestMethod.PUT)
    @ApiOperation(value = "insertSysUser", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userData", value = "用户的信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userCorpus", value = "用户的语料权限", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userConfig", value = "用户的导控权限", required = true, dataType = "STRING")
    })
    public String insertSysUser(@RequestParam("userData") String userData, @RequestParam("userCorpus") String userCorpus, @RequestParam("userConfig") String userConfig) {
        String reuslt = iUserService.insertSysUser(userData, userCorpus, userConfig).toString();
        return reuslt;
    }

    @RequestMapping(value = "/getAllSysUser", method = RequestMethod.GET)
    @ApiOperation(value = "getAllSysUser", notes = "获取到所有的用户")
    @ApiImplicitParams({
    })
    public String getAllSysUser(HttpServletRequest request) {
        String reuslt = iUserService.getAllSysUser().toString();
        return reuslt;
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @ApiOperation(value = "deleteUser", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户的名称", required = true, dataType = "STRING")
    })
    public String deleteUser(@RequestParam("userName") String userName) {
        String reuslt = iUserService.deleteUser(userName).toString();
        return reuslt;
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation(value = "updateUser", notes = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userData", value = "用户的信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userCorpus", value = "用户的语料权限", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userConfig", value = "用户的导控权限", required = true, dataType = "STRING")
    })
    public String updateUser(@RequestParam("userData") String userData,
                             @RequestParam("userCorpus") String userCorpus,
                             @RequestParam("userConfig") String userConfig,
                             HttpServletRequest request) {
        String reuslt = iUserService.updateUser(userData, userCorpus, userConfig).toString();
        return reuslt;
    }

    @RequestMapping(value = "/judgeUser", method = RequestMethod.POST)
    @ApiOperation(value = "judgeUser", notes = "登陆判断")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户登陆账号", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userPassword", value = "用户登录密码", required = true, dataType = "STRING")
    })
    public String judgeUser(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword) {
        String reuslt = iUserService.judgeUser(userName, userPassword).toString();
        return reuslt;
    }

    @RequestMapping(value = "/getSysUser", method = RequestMethod.GET)
    @ApiOperation(value = "getSysUser", notes = "获取用户积分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户登陆账号", required = true, dataType = "STRING")
    })
    public String getSysUser(@RequestParam("userName") String userName) {
        String reuslt = iUserService.getSysUser(userName).toString();
        return reuslt;
    }

    @RequestMapping(value = "/updatePartUser", method = RequestMethod.POST)
    @ApiOperation(value = "updatePartUser", notes = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userData", value = "用户的信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userLoginName", value = "登陆名", required = true, dataType = "STRING")
    })
    public String updatePartUser(@RequestParam("userData") String userData,
                             @RequestParam("userCorpus") String userCorpus,
                             @RequestParam("userConfig") String userConfig,
                             HttpServletRequest request) {
        String reuslt = iUserService.updateUser(userData, userCorpus, userConfig).toString();
        return reuslt;
    }
}
