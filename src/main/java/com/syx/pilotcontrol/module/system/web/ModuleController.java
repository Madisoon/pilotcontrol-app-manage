package com.syx.pilotcontrol.module.system.web;

import com.syx.pilotcontrol.module.system.service.IModuleService;
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
 * Created by Master  Zg on 2016/11/15.
 */
@RestController
@RequestMapping(value = "/system")
@Api(value = "系统模块", description = "管理系统模块的API")
public class ModuleController {
    @Autowired
    private IModuleService iModuleService;

    @RequestMapping(value = "/insertModule", method = RequestMethod.PUT)
    @ApiOperation(value = "insertModule", notes = "插入模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module_value", value = "模块名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "module_url", value = "模块内容", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "module_id", value = "模块id", required = true, dataType = "STRING")
    })
    public String insertModule(@RequestParam("module_value") String moduleValue,
                               @RequestParam("module_url") String moduleUrl,
                               @RequestParam("module_id") String moduleId) {
        String result = iModuleService.insertModule(moduleValue, moduleUrl, moduleId).toString();
        return result;
    }

    @RequestMapping(value = "/getAllModule", method = RequestMethod.GET)
    @ApiOperation(value = "getAllModule", notes = "得到所有模块")
    @ApiImplicitParams({})
    public String getAllModule() {
        String result = iModuleService.getAllModule().toString();
        return result;
    }

    @RequestMapping(value = "/deleteModule", method = RequestMethod.POST)
    @ApiOperation(value = "deleteModule", notes = "删除模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module_id", value = "模块id", required = true, dataType = "STRING")
    })
    public String deleteModule(@RequestParam("module_id") String moduleId) {
        String result = iModuleService.deleteModule(moduleId).toString();
        return result;
    }

    @RequestMapping(value = "/getAllSecondModule", method = RequestMethod.GET)
    @ApiOperation(value = "getAllSecondModule", notes = "根据id获取模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module_id", value = "模块id", required = true, dataType = "STRING")
    })
    public String getAllSecondModule(@RequestParam("module_id") String moduleId) {
        String result = iModuleService.getAllSecondModule(moduleId).toString();
        return result;
    }

    @RequestMapping(value = "/updateModuleInfo", method = RequestMethod.POST)
    @ApiOperation(value = "updateModuleInfo", notes = "修改模块")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "module_id", value = "模块ID", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "module_name", value = "模块名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "module_content", value = "模块内容", required = true, dataType = "STRING")
    })
    public String updateModuleInfo(@RequestParam("module_id") String moduleId,
                                   @RequestParam("module_name") String moduleName,
                                   @RequestParam("module_content") String moduleContent) {
        String result = iModuleService.updateModuleInfo(moduleId, moduleName, moduleContent).toString();
        return result;
    }
}
