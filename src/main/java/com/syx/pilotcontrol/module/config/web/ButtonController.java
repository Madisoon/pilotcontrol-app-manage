package com.syx.pilotcontrol.module.config.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.config.service.IButtonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/8/11.
 */
@RestController
@RequestMapping(value = "/button")
@Api(value = "ButtonController", description = "管理按钮的方法")
public class ButtonController {
    @Autowired
    IButtonService iButtonService;

    @PutMapping(value = "/insertButton")
    @ApiOperation(value = "insertButton", notes = "插入按钮属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buttonData", value = "按钮数据", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "markData", value = "分数数据", required = true, dataType = "STRING")
    })
    public String insertButton(@RequestParam("buttonData") String buttonData,
                               @RequestParam("markData") String markData) {
        String result = iButtonService.insertButton(buttonData, markData).toString();
        return result;
    }

    @PostMapping(value = "/updateButton")
    @ApiOperation(value = "updateButton", notes = "修改按钮属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buttonData", value = "按钮数据", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "markData", value = "分数数据", required = true, dataType = "STRING")
    })
    public String updateButton(@RequestParam("buttonData") String buttonData,
                               @RequestParam("buttonId") String buttonId,
                               @RequestParam("markData") String markData) {
        String result = iButtonService.updateButton(buttonData, markData, buttonId).toString();
        return result;
    }

    @PostMapping(value = "/deleteButton")
    @ApiOperation(value = "deleteButton", notes = "删除按钮属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buttonId", value = "按钮id", required = true, dataType = "STRING")
    })
    public String deleteButton(@RequestParam("buttonId") String buttonId) {
        String result = iButtonService.deleteButton(buttonId).toString();
        return result;
    }

    @GetMapping(value = "/getButtonByName")
    @ApiOperation(value = "getButtonByName", notes = "删除按钮属性")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buttonClass", value = "按钮id", required = true, dataType = "STRING")
    })
    public String getButtonByName(@RequestParam("buttonClass") String buttonClass) {
        String result = iButtonService.getButtonByName(buttonClass).toString();
        return result;
    }

    @GetMapping(value = "/getAllButton")
    @ApiOperation(value = "getAllButton", notes = "获取所有按钮")
    @ApiImplicitParams({
    })
    public String getAllButton(HttpServletRequest request) {
        try {
            /*String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String pageNumber = params.getString("pageNumber");
            String pageSize = params.getString("pageSize");*/
            String result = iButtonService.getAllButton().toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }
}
