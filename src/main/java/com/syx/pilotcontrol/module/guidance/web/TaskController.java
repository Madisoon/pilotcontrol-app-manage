package com.syx.pilotcontrol.module.guidance.web;

import com.syx.pilotcontrol.module.guidance.service.ITaskService;
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
}
