package com.syx.pilotcontrol.module.manpower.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.manpower.service.IManPowerTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/manpower")
@Api(value = "ManPowerTaskController", description = "人工导控模块")
public class ManPowerTaskController {
    @Autowired
    IManPowerTaskService iManPowerTaskService;

    @PutMapping(value = "/insertManPower")
    @ApiOperation(value = "insertManPower", notes = "插入人工任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "manPowerData", value = "人工任务数据", required = true, dataType = "STRING")
    })
    public String insertManPower(@RequestParam("manPowerData") String manPowerData) {
        String result = iManPowerTaskService.insertManPower(manPowerData).toString();
        return result;
    }

    @RequestMapping(value = "/getAllManPower", method = RequestMethod.POST)
    @ApiOperation(value = "getAllManPower", notes = "获取个人的导控的任务")
    @ApiImplicitParams({
    })
    public String getAllManPower(HttpServletRequest request) {
        try {
            String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String userLoginName = params.getString("userLoginName");
            String type = params.getString("type");
            String pageSize = params.getString("pageSize");
            String pageNumber = params.getString("pageNumber");
            String result = "";
            result = iManPowerTaskService.getAllManPower(userLoginName, type, pageSize, pageNumber).toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }

    @PostMapping(value = "/changeOrderStatus")
    @ApiOperation(value = "changeOrderStatus", notes = "改变状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态", required = true, dataType = "STRING")
    })
    public String changeOrderStatus(@RequestParam("orderId") String orderId, @RequestParam("orderStatus") String orderStatus) {
        String result = iManPowerTaskService.changeOrderStatus(orderId, orderStatus).toString();
        return result;
    }
}
