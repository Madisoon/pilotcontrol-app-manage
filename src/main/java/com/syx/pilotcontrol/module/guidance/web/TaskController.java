package com.syx.pilotcontrol.module.guidance.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.guidance.service.ITaskService;
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
            @ApiImplicitParam(name = "taskContext", value = "语料信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "remarkContent", value = "语料信息", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "daokongTypeOrder", value = "语料信息", required = true, dataType = "STRING")
    })
    public String insertTask(@RequestParam("taskInfo") String taskInfo,
                             @RequestParam("taskContext") String taskContext,
                             @RequestParam("remarkContent") String remarkContent,
                             @RequestParam("daokongTypeOrder") String daokongTypeOrder) {
        iTaskService.insertTask(taskInfo, taskContext, remarkContent, daokongTypeOrder);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", 1);
        String result = jsonObject.toString();
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
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "STRING")
    })
    public String updateMonitorStatus(@RequestParam("taskId") String taskId) {
        String result = iTaskService.updateMonitorStatus(taskId).toString();
        return result;
    }

    @ApiOperation(value = "deleteTaskById", notes = "删除导控的一些任务")
    @RequestMapping(value = "/deleteTaskById", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "STRING")
    })
    public String deleteTaskById(@RequestParam("taskId") String taskId) {
        String result = iTaskService.deleteTaskById(taskId).toString();
        return result;
    }

    @ApiOperation(value = "getSinaRemark", notes = "获取评论")
    @RequestMapping(value = "/getSinaRemark", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "地址", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "host", value = "标识", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "STRING")
    })
    public String getSinaRemark(@RequestParam("urlPost") String url,
                                @RequestParam("host") String host,
                                @RequestParam("page") String page) {
        String result = iTaskService.getSinaRemark(url, host, page).toString();
        return result;
    }

    @PostMapping(value = "/uploadOrderFile")
    public String uploadHead(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String str = sdf.format(date);
            String filePath = request.getSession().getServletContext().getRealPath("/") + str + ""
                    + file.getOriginalFilename();//获取服务器的绝对路径+项目相对路径head/图片原名
            file.transferTo(new File(filePath));//讲客户端文件传输到服务器端
            return str + file.getOriginalFilename();
        }
        return "";
    }
}
