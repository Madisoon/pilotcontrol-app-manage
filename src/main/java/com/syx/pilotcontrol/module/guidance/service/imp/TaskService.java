package com.syx.pilotcontrol.module.guidance.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.guidance.service.ITaskService;
import com.syx.pilotcontrol.utils.HttpClientUtil;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Msater Zg on 2017/6/29.
 */
@Service
public class TaskService implements ITaskService {
    private final String pilcontrolUrl = "http://121.199.4.149:18080/api/guide";

    private final String PILCONTROLURLACCOUNT = "http://121.199.4.149:18080/api/verify";

    @Autowired
    BaseDao baseDao;

    @Override
    @Async
    public JSONObject insertTask(String taskInfo, String taskContext) {
        JSONObject jsonObject = JSON.parseObject(taskInfo);
        String configId = jsonObject.getString("config_id");
        String taskCreate = jsonObject.getString("task_create");
        String[] numberTypes = (jsonObject.getString("number_type")).split(",");
        String taskType = jsonObject.getString("task_type");
        String taskUrl = jsonObject.getString("task_url");
        String taskTitle = jsonObject.getString("task_title");
        String taskNumber = jsonObject.getString("task_number");
        String intervalTime = jsonObject.getString("interval_time");
        String sqlGetNumber = "";
        // 获取到所有的账号和语料
        JSONArray jsonArrayNumber = new JSONArray();
        if (numberTypes.length == 2) {
            // 说明系统账号和账号一起使用
            sqlGetNumber = "SELECT * FROM guidance_number a WHERE a.config_id = ? AND (a.number_create = ? OR a.number_sys_status = 1 OR a.number_open= 1 )";
            jsonArrayNumber = (JSONArray) JSON.toJSON(baseDao.rawQuery(sqlGetNumber, new String[]{configId, taskCreate}));
        } else {
            String numberTypesItem = numberTypes[0];
            if ("0".equals(numberTypesItem)) {
                //只使用私有的账号
                sqlGetNumber = "SELECT * FROM guidance_number a WHERE a.config_id = ? AND a.number_create = ?";
                jsonArrayNumber = (JSONArray) JSON.toJSON(baseDao.rawQuery(sqlGetNumber, new String[]{configId, taskCreate}));
            }
            if ("1".equals(numberTypesItem)) {
                sqlGetNumber = "SELECT * FROM guidance_number a WHERE a.config_id = ? AND ( a.number_sys_status = 1 OR a.number_open= 1 )";
                jsonArrayNumber = (JSONArray) JSON.toJSON(baseDao.rawQuery(sqlGetNumber, new String[]{configId,}));
            }
        }
        String[] taskContexts = taskContext.split(",");
        int taskContextsLen = taskContexts.length;
        String insertTaskSql = SqlEasy.insertObject(taskInfo, "guidance_task_main");
        int result = baseDao.execute(insertTaskSql);
        Map<String, String> map = baseDao.rawQueryForMap("SELECT id FROM guidance_task_main ORDER BY id  DESC  LIMIT 1");
        int taskId = Integer.parseInt(map.get("id"));
        if ("".equals(taskContext)) {
            // 浏览帖子
            int taskNumberInt = Integer.parseInt(taskNumber);
            for (int i = 0; i < taskNumberInt; i++) {

            }
        } else {
            // 回帖
            for (int i = 0; i < taskContextsLen; i++) {
                String insertCorpusSql = "INSERT INTO guidance_task_corpus (task_id, corpus_context) VALUES(?,?)";
                baseDao.execute(insertCorpusSql, new String[]{String.valueOf(taskId), taskContexts[i]});
            }
        }
        // 所有的系统的账号
        int jsonArrayNumberLen = jsonArrayNumber.size();
        // type 1代表发帖（需要标题和内容），2代表回帖 （只需要语料的内容）
        int numberSuccess = 0;
        for (int i = 0; i < Integer.parseInt(taskNumber, 10); i++) {
            int randomNumber = (int) (Math.random() * jsonArrayNumberLen);
            System.out.println(randomNumber);
            int randomContext = (int) (Math.random() * taskContextsLen);
            JSONObject jsonObjectNumber = jsonArrayNumber.getJSONObject(randomNumber);
            Map<String, String> mapPost = new HashMap<>();
            mapPost.put("type", taskType);
            mapPost.put("account", jsonObjectNumber.getString("number_name"));
            mapPost.put("password", jsonObjectNumber.getString("number_password"));
            mapPost.put("url", taskUrl);
            mapPost.put("title", taskTitle);
            mapPost.put("content", taskContexts[randomContext]);
            // 增加时间的间隔的判断
            try {
                TimeUnit.SECONDS.sleep(Integer.parseInt(intervalTime, 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject jsonObjectSuccess = HttpClientUtil.sendPost(pilcontrolUrl, mapPost);
            System.out.println(jsonObjectSuccess.toString());
            String valueSuccess = jsonObjectSuccess.getString("status");
            if (valueSuccess.equals("1")) {
                numberSuccess++;
            }
        }
        String updateSql = "UPDATE guidance_task_main SET task_number_success = ? WHERE id = ? ";
        baseDao.execute(updateSql, new String[]{String.valueOf(numberSuccess), String.valueOf(taskId)});
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

    @Override
    public JSONArray getAllMonitor() {
        String monitorSql = "SELECT a.*,GROUP_CONCAT(a.corpus_context) AS corpus_contexts  " +
                "FROM (SELECT  a.*,b.config_name,c.user_name,d.corpus_context " +
                "FROM  guidance_task_main a   " +
                "LEFT JOIN guidance_config b   " +
                "ON a.config_id = b.id LEFT JOIN sys_user c   " +
                "ON a.task_create = c.user_loginname LEFT JOIN guidance_task_corpus d ON a.id = d.task_id   " +
                "WHERE a.task_status = 0 ) a GROUP BY a.id";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(monitorSql));
        return jsonArray;
    }

    @Override
    public JSONObject updateMonitorStatus(String taskId) {
        String updateStatusSql = "UPDATE guidance_task_main SET task_status = 1 WHERE id = ? ";
        JSONObject jsonObject = new JSONObject();
        int result = baseDao.execute(updateStatusSql, new String[]{taskId});
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteTaskById(String taskId) {
        String deleteTask = "DELETE FROM guidance_task_main WHERE id = ? ";
        String deleteTaskCorpus = "DELETE FROM guidance_task_corpus WHERE task_id = ? ";
        String[] taskIdS = taskId.split(",");
        int taskIdSLen = taskIdS.length;
        int result = 0;
        for (int i = 0; i < taskIdSLen; i++) {
            result = baseDao.execute(deleteTask, new String[]{taskIdS[i]});
            baseDao.execute(deleteTaskCorpus, new String[]{taskIdS[i]});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int randomNumber = (int) (Math.random() * 10);
            System.out.println(randomNumber);
        }
    }
}
