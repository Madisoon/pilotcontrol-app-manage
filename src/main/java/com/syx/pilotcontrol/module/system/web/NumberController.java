package com.syx.pilotcontrol.module.system.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.system.service.INumberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/6/23.
 */
@RequestMapping(value = "/system")
@RestController
@Api(value = "NumberController", description = "账号管理")
public class NumberController {
    @Autowired
    INumberService iNumberService;

    @RequestMapping(value = "/insertNumber", method = RequestMethod.PUT)
    @ApiOperation(value = "insertNumber", notes = "插入账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numberInfo", value = "账号信息", required = true, dataType = "STRING")
    })
    public String insertNumber(@RequestParam("numberInfo") String numberInfo) {
        String result = iNumberService.insertNumber(numberInfo).toString();
        return result;
    }

    @RequestMapping(value = "/getAllNumber", method = RequestMethod.POST)
    @ApiOperation(value = "getAllNumber", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "导控配置id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "numberType", value = "账号的类型", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "numberCreate", value = "账号的创建者", required = true, dataType = "STRING")
    })
    public String getAllNumber(HttpServletRequest request) {
        try {
            String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String pageNumber = params.getString("pageNumber");
            String pageSize = params.getString("pageSize");
            String configId = params.getString("configId");
            String numberType = params.getString("numberType");
            String numberCreate = params.getString("numberCreate");
            String result = "";
            result = iNumberService.getAllNumber(configId, numberType, numberCreate, pageSize, pageNumber).toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }

    @RequestMapping(value = "/updateNumberOpen", method = RequestMethod.POST)
    @ApiOperation(value = "updateNumberOpen", notes = "开放账号给系统")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numberId", value = "账号id", required = true, dataType = "STRING")
    })
    public String updateNumberOpen(@RequestParam("numberId") String numberId) {
        String result = iNumberService.updateNumberOpen(numberId).toString();
        return result;
    }

    @RequestMapping(value = "/updateNumber", method = RequestMethod.POST)
    @ApiOperation(value = "updateNumber", notes = "修改账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numberInfo", value = "账号信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "numberId", value = "账号id", required = true, dataType = "STRING")
    })
    public String updateNumber(@RequestParam("numberInfo") String numberInfo, @RequestParam("numberId") String numberId) {
        String result = iNumberService.updateNumber(numberInfo, numberId).toString();
        return result;
    }

    @RequestMapping(value = "/deleteNumber", method = RequestMethod.POST)
    @ApiOperation(value = "deleteNumber", notes = "插入账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numberId", value = "账号id", required = true, dataType = "STRING")
    })
    public String deleteNumber(@RequestParam("numberId") String numberId) {
        String result = iNumberService.deleteNumber(numberId).toString();
        return result;
    }
}
