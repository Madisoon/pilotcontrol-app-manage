package com.syx.pilotcontrol.module.guidance.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

    @Override
    public JSONArray getAllTaskByConfig(String configId, String userName) {
        String getAllTaskSql = "SELECT a.*,GROUP_CONCAT(a.corpus_context) AS task_contexts FROM  " +
                "(SELECT a.*,b.corpus_context,c.user_name FROM  guidance_task_main a  " +
                "LEFT JOIN guidance_task_corpus b  " +
                "ON a.id = b.task_id LEFT JOIN sys_user c ON a.task_create = c.user_loginname  " +
                "WHERE a.config_id = ? AND a.task_create = ? ORDER BY a.task_time DESC) a GROUP BY a.id";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllTaskSql, new String[]{configId, userName}));
        return jsonArray;
    }
}
