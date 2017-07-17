package com.syx.pilotcontrol.module.guidance.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.guidance.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/6/29.
 */
@RequestMapping(value = "/guidance")
@RestController
@Api(value = "TaskController", description = "导控任务的Api")
public class TaskController {
    @Autowired
    ITaskService iTaskService;

    @ApiOperation(value = "insertTask", notes = "插入任务")
    @RequestMapping(value = "/insertTask", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskInfo", value = "任务的信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "taskContext", value = "语料信息", required = true, dataType = "STRING")
    })
    public String insertTask(@RequestParam("taskInfo") String taskInfo, @RequestParam("taskContext") String taskContext) {
        String result = iTaskService.insertTask(taskInfo, taskContext).toString();
        return result;
    }

    @RequestMapping(value = "/getAllTaskByConfig", method = RequestMethod.POST)
    @ApiOperation(value = "getAllTaskByConfig", notes = "获取个人的导控的任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "configId", value = "导控的类型", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "STRING")
    })
    public String getAllTaskByConfig(HttpServletRequest request) {
        try {
            String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String configId = params.getString("configId");
            String userName = params.getString("userName");
            String result = "";
            result = iTaskService.getAllTaskByConfig(configId, userName).toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }

    @ApiOperation(value = "getAllMonitor", notes = "获取所有的导控任务列表")
    @RequestMapping(value = "/getAllMonitor", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public String getAllMonitor() {
        String result = iTaskService.getAllMonitor().toString();
        return result;
    }

    @ApiOperation(value = "updateMonitorStatus", notes = "获取所有的导控任务列表")
    @RequestMapping(value = "/updateMonitorStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "任务id", value = "taskId", required = true, dataType = "STRING")
    })
    public String updateMonitorStatus(@RequestParam("taskId") String taskId) {
        String result = iTaskService.updateMonitorStatus(taskId).toString();
        return result;
    }
}
