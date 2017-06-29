package com.syx.pilotcontrol.module.guidance.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.guidance.service.ITaskService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Msater Zg on 2017/6/29.
 */
@Service
public class TaskService implements ITaskService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertTask(String taskInfo, String taskContext) {
        JSONObject jsonObject = new JSONObject();
        String[] taskContexts = taskContext.split(",");
        String insertTaskSql = SqlEasy.insertObject(taskInfo, "guidance_task_main");
        int result = baseDao.execute(insertTaskSql);
        if ("".equals(taskContext)) {
            // 浏览帖子
        } else {
        }
        jsonObject.put("result", result);
        return jsonObject;
    }
}
