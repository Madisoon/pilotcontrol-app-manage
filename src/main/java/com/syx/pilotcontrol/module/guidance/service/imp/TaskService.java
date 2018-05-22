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
    public JSONObject insertTask(String taskInfo, String taskContext, String remarkContent, String daokongTypeOrder) {
        System.out.println("导控类型" + daokongTypeOrder);
        JSONObject jsonObject = JSON.parseObject(taskInfo);
        String configId = jsonObject.getString("config_id");
        String taskCreate = jsonObject.getString("task_create");
        String[] numberTypes = (jsonObject.getString("number_type")).split(",");
        String taskType = jsonObject.getString("task_type");
        String taskUrl = jsonObject.getString("task_url");
        String taskTitle = jsonObject.getString("task_title");
        String taskNumber = jsonObject.getString("task_number");
        String intervalTime = jsonObject.getString("interval_time");
        String taskIntegration = jsonObject.getString("task_integration");
        String sqlGetNumber = "";
        String insertTaskSql = SqlEasy.insertObject(taskInfo, "guidance_task_main");
        int result = baseDao.execute(insertTaskSql);
        // 点赞的执行方法
        if (!"[]".equals(remarkContent)) {
            JSONArray jsonArray = JSON.parseArray(remarkContent);
            for (int i = 0, jsonArrayLen = jsonArray.size(); i < jsonArrayLen; i++) {
                JSONObject jsonObjectRemark = jsonArray.getJSONObject(i);
                System.out.println(jsonObjectRemark);
                String content = jsonObjectRemark.getString("content");
                String newsid = jsonObjectRemark.getString("newsid");
                String channel = jsonObjectRemark.getString("channel");
                String mid = jsonObjectRemark.getString("mid");
                Map map = new HashMap();
                /*host - 新闻站点的标识
                url - 新闻的链接
                times - 点赞的次数
                channel - 从评论列表返回的json中确定的一个参数
                mid - 从评论列表返回的json中确定的一个参数
                proxy - 传入代理（可以为空）*/
                map.put("host", "sina");
                map.put("url", taskUrl);
                map.put("times", taskNumber);
                map.put("channel", channel);
                map.put("mid", mid);
                map.put("newsid", newsid);
                HttpClientUtil.sendPost("http://121.199.4.149:18080/api/news/thumbUp", map);
            }
        } else {
            // 不是点赞
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

            // 获取内容
            String[] taskContexts = taskContext.split(",");
            int taskContextsLen = taskContexts.length;
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
                int randomContext = (int) (Math.random() * taskContextsLen);
                JSONObject jsonObjectNumber = jsonArrayNumber.getJSONObject(randomNumber);
                Map<String, String> mapPost = new HashMap<>();
                if ("1".equals(daokongTypeOrder)) {
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
                    JSONObject jsonObjectSuccess = HttpClientUtil.sendPost("http://121.199.4.149:18080/api/guide", mapPost);
                    String valueSuccess = jsonObjectSuccess.getString("status");
                    System.out.println(valueSuccess);
                    if (valueSuccess.equals("1")) {
                        numberSuccess++;
                    }
                    // 执行完之后所消耗的分数
                    int taskRealNumber = (numberSuccess / Integer.parseInt(taskNumber)) * Integer.parseInt(taskIntegration);
                    String updateMark = "UPDATE sys_user a SET a.user_mark =  a.user_mark-" + taskRealNumber + "  " +
                            "WHERE a.user_loginname = '" + taskCreate + "'";
                    baseDao.execute(updateMark);
                    String updateSql = "UPDATE guidance_task_main SET task_number_success = ? WHERE id = ? ";
                    baseDao.execute(updateSql, new String[]{String.valueOf(numberSuccess), String.valueOf(taskId)});
                } else {
                    mapPost.put("account", jsonObjectNumber.getString("number_name"));
                    mapPost.put("password", jsonObjectNumber.getString("number_password"));
                    mapPost.put("url", taskUrl);
                    mapPost.put("host", "sina");
                    mapPost.put("content", taskContexts[randomContext]);

                /*host - 新闻站点的标识
                url - 新闻的链接
                account - 发表评论的账号
                password - 发表评论账号的密码
                content - 发表评论的内容
                proxy - 使用的代理（可以为null）*/
                    // 增加时间的间隔的判断
                    try {
                        TimeUnit.SECONDS.sleep(Integer.parseInt(intervalTime, 10));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObjectSuccess = HttpClientUtil.sendPost("http://121.199.4.149:18080/api/news/reply", mapPost);
                    System.out.println(jsonObjectSuccess.toString());
                    String valueSuccess = jsonObjectSuccess.getString("status");
                    if (valueSuccess.equals("1")) {
                        numberSuccess++;
                    }
                    // 执行完之后所消耗的分数
                    int taskRealNumber = (numberSuccess / Integer.parseInt(taskNumber)) * Integer.parseInt(taskIntegration);
                    String updateMark = "UPDATE sys_user a SET a.user_mark =  a.user_mark-" + taskRealNumber + "  " +
                            "WHERE a.user_loginname = '" + taskCreate + "'";
                    baseDao.execute(updateMark);
                    String updateSql = "UPDATE guidance_task_main SET task_number_success = ? WHERE id = ? ";
                    baseDao.execute(updateSql, new String[]{String.valueOf(numberSuccess), String.valueOf(taskId)});
                }
            }
        }
        JSONObject jsonObjectReturn = new JSONObject();
        jsonObjectReturn.put("result", result);
        return jsonObjectReturn;
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

    @Override
    public JSONObject getSinaRemark(String url, String host, String page) {
        Map<String, String> mapPost = new HashMap<>();
        mapPost.put("url", url);
        mapPost.put("host", host);
        mapPost.put("page", page);
        JSONObject jsonObjectSuccess = HttpClientUtil.sendPost("http://121.199.4.149:18080/api/news/getReplyList", mapPost);
        JSONArray jsonArray = (jsonObjectSuccess.getJSONObject("result")).getJSONArray("cmntlist");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }
/*
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        taskService.getSinaNewsRemark("", "http://news.sina.com.cn/s/wh/2017-09-25/doc-ifymeswc9804017.shtml", "1");
    }*/
}
