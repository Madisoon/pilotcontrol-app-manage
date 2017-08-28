package com.syx.pilotcontrol.module.manpower.web;


import com.syx.pilotcontrol.module.manpower.service.IOtherConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Msater Zg on 2017/8/25.
 */
@RestController
@RequestMapping(value = "/manpower")
@Api(value = "OtherConfigController", description = "管理导控其他配置的接口")
public class OtherConfigController {
    @Autowired
    IOtherConfigService iOtherConfigService;

    @PutMapping(value = "/insertOtherConfig")
    @ApiOperation(value = "insertOtherConfig", notes = "插入其他配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherData", value = "其他配置数据", required = true, dataType = "STRING")
    })
    public String insertManPower(@RequestParam("otherData") String otherData) {
        String result = iOtherConfigService.insertOtherConfig(otherData).toString();
        return result;
    }

    @PostMapping(value = "/deleteOtherConfig")
    @ApiOperation(value = "deleteOtherConfig", notes = "删除其他配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "删除其他配置", required = true, dataType = "STRING")
    })
    public String deleteOtherConfig(@RequestParam("configId") String configId) {
        String result = iOtherConfigService.deleteOtherConfig(configId).toString();
        return result;
    }

    @PostMapping(value = "/updateOtherConfig")
    @ApiOperation(value = "updateOtherConfig", notes = "插入其他配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "otherData", value = "其他配置数据", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "otherId", value = "其他配置id", required = true, dataType = "STRING")
    })
    public String insertManPower(@RequestParam("otherData") String otherData,
                                 @RequestParam("otherId") String otherId) {
        String result = iOtherConfigService.updateOtherConfig(otherData, otherId).toString();
        return result;
    }

    @PostMapping(value = "/getAllOtherConfig")
    @ApiOperation(value = "getAllOtherConfig", notes = "得到所有的其他配置")
    @ApiImplicitParams({
    })
    public String getAllOtherConfig() {
        String result = iOtherConfigService.getAllOtherConfig().toString();
        return result;
    }
}
