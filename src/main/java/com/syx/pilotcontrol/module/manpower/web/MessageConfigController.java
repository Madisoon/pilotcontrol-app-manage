package com.syx.pilotcontrol.module.manpower.web;

import com.syx.pilotcontrol.module.manpower.service.IMessageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Msater Zg on 2017/8/31.
 */
@RestController
@RequestMapping(value = "/manpower")
@Api(value = "MessageConfigController", description = "管理短信配置的接口")
public class MessageConfigController {
    @Autowired
    IMessageConfigService iMessageConfigService;

    @PutMapping(value = "/insertMessageConfig")
    @ApiOperation(value = "insertMessageConfig", notes = "插入其他配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageData", value = "短信配置数据", required = true, dataType = "STRING")
    })
    public String insertManPower(@RequestParam("messageData") String messageData) {
        String result = iMessageConfigService.insertMessageConfig(messageData).toString();
        return result;
    }

    @PostMapping(value = "/updateMessageConfig")
    @ApiOperation(value = "updateMessageConfig", notes = "插入其他配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageData", value = "短信配置数据", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "messageId", value = "短信配置id", required = true, dataType = "STRING")
    })
    public String updateMessageConfig(@RequestParam("messageData") String messageData, @RequestParam("messageId") String messageId) {
        String result = iMessageConfigService.updateMessageConfig(messageData, messageId).toString();
        return result;
    }

    @PostMapping(value = "/deleteMessageConfig")
    @ApiOperation(value = "deleteMessageConfig", notes = "删除短信配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "短信配置id", required = true, dataType = "STRING")
    })
    public String deleteMessageConfig(@RequestParam("messageId") String messageId) {
        String result = iMessageConfigService.deleteMessageConfig(messageId).toString();
        return result;
    }
}
