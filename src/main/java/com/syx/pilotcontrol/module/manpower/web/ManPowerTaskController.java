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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @PostMapping(value = "/getAllManPowerByType")
    @ApiOperation(value = "getAllManPowerByType", notes = "获取所有人的导控请求")
    @ApiImplicitParams({
    })
    public String getAllManPowerByType(HttpServletRequest request) {
        try {
            String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String type = params.getString("type");
            String pageSize = params.getString("pageSize");
            String pageNumber = params.getString("pageNumber");
            String result = "";
            result = iManPowerTaskService.getAllManPowerByType(type, pageSize, pageNumber).toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }

    @PostMapping(value = "/updateManPowerData")
    @ApiOperation(value = "updateManPowerData", notes = "改变状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "orderStatus", value = "订单状态", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "orderData", value = "订单数据", required = true, dataType = "STRING")
    })
    public String updateManPowerData(@RequestParam("orderId") String orderId,
                                     @RequestParam("orderStatus") String orderStatus,
                                     @RequestParam("orderData") String orderData) {
        String result = iManPowerTaskService.updateManPowerData(orderId, orderStatus, orderData).toString();
        return result;
    }

    @PostMapping(value = "/uploadImage")
    @ApiOperation(value = "uploadImage", notes = "改变状态")
    @ApiImplicitParams({
    })
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        long str = System.currentTimeMillis();
        if (!file.isEmpty()) {
            String filePath = "C:/dummyPath/" + str + ""
                    + file.getOriginalFilename();//获取服务器的绝对路径+项目相对路径head/图片原名
            System.out.println(filePath);
            try {
                file.transferTo(new File(filePath));//讲客户端文件传输到服务器端
            } catch (IOException e) {
                e.printStackTrace();
            }
/*            int position = filePath.lastIndexOf("/");//
            System.out.println(position);
            String head = filePath.substring(position + 1);//获取真正的图片名字，如“1.png”*/
        }
        return str + file.getOriginalFilename();
    }
}
