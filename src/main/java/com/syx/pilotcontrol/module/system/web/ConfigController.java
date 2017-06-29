package com.syx.pilotcontrol.module.system.web;

import com.syx.pilotcontrol.module.system.service.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Msater Zg on 2017/6/15.
 */
@RestController
@RequestMapping(value = "/system")
@Api(value = "导控配置模块", description = "管理系统导控模块")
public class ConfigController {
    @Autowired
    private IConfigService iConfigService;

    @RequestMapping(value = "/insertConfig", method = RequestMethod.PUT)
    @ApiOperation(value = "insertConfig", notes = "插入导控模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configName", value = "导控配置名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "configType", value = "导控配置类型", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "configId", value = "导控配置ID", required = true, dataType = "STRING")
    })
    public String insertConfig(@RequestParam("configName") String configName,
                               @RequestParam("configType") String configType,
                               @RequestParam("configId") String configId) {
        String result = iConfigService.insertConfig(configName, configType, configId).toString();
        return result;
    }

    @RequestMapping(value = "/getAllConfig", method = RequestMethod.GET)
    @ApiOperation(value = "getAllConfig", notes = "得到所有导控模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "导控配置父级ID", required = true, dataType = "STRING")
    })
    public String getAllConfig(@RequestParam("configId") String configId) {
        String result = iConfigService.getAllConfig(configId).toString();
        return result;
    }

    @RequestMapping(value = "/deleteConfig", method = RequestMethod.POST)
    @ApiOperation(value = "deleteConfig", notes = "删除导控模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "导控配置id", required = true, dataType = "STRING")
    })
    public String deleteConfig(@RequestParam("configId") String configId) {
        String result = iConfigService.deleteConfig(configId).toString();
        return result;
    }

    @RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
    @ApiOperation(value = "updateConfig", notes = "修改导控模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "模块ID", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "configName", value = "模块名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "configType", value = "模块内容", required = true, dataType = "STRING")
    })
    public String updateConfig(@RequestParam("configId") String configId,
                               @RequestParam("configName") String configName,
                               @RequestParam("configType") String configType) {
        String result = iConfigService.updateConfig(configId, configName, configType).toString();
        return result;
    }

    @RequestMapping(value = "/getAllTypeConfig", method = RequestMethod.GET)
    @ApiOperation(value = "getAllTypeConfig", notes = "获取到所有的导控权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "STRING")
    })
    public String getAllConfigByType(@RequestParam("userName") String userName) {
        String result = iConfigService.getAllTypeConfig(userName).toString();
        return result;
    }

    @RequestMapping(value = "/getGuidanceAuthority", method = RequestMethod.GET)
    @ApiOperation(value = "getGuidanceAuthority", notes = "获取到所有的导控权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userType", value = "用户是否为管理员", required = true, dataType = "STRING")
    })
    public String getGuidanceAuthority(@RequestParam("userName") String userName, @RequestParam("userType") String userType) {
        String result = iConfigService.getGuidanceAuthority(userName, userType).toString();
        return result;
    }


}
