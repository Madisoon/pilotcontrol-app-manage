package com.syx.pilotcontrol.module.guidance.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/29.
 */
public interface ITaskService {
    public JSONObject insertTask(String taskInfo, String taskContext);

    public JSONArray getAllTaskByConfig(String configId, String userName);

    public JSONArray getAllMonitor();

    public JSONObject updateMonitorStatus(String taskId);

    public JSONObject deleteTaskById(String taskId);
}
