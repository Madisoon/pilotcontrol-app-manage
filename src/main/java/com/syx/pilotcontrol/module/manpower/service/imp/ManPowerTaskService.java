package com.syx.pilotcontrol.module.manpower.service.imp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.sun.org.apache.regexp.internal.RE;
import com.syx.pilotcontrol.module.manpower.service.IManPowerTaskService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Master  Zg on 2016/12/12.
 */

@Service
public class ManPowerTaskService implements IManPowerTaskService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertManPower(String manPowerData) {
        long id = System.currentTimeMillis();
        JSONObject manPower = JSONObject.parseObject(manPowerData);
        String realMark = manPower.getString("task_start_mark");
        String taskCreate = manPower.getString("task_create");
        manPower.put("id", id);
        String sqlInsert = SqlEasy.insertObject(manPower.toJSONString(), "guidance_manpower_task");
        int result = baseDao.execute(sqlInsert);
        String updateMark = "UPDATE sys_user a SET a.user_mark =  a.user_mark-" + Integer.parseInt(realMark, 10) + "  " +
                "WHERE a.user_loginname = '" + taskCreate + "'";
        baseDao.execute(updateMark);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllManPower(String userLoginName, String type, String pilotControlType, String pageSize, String pageNumber) {
        JSONArray jsonArray = new JSONArray();
        if ("1".equals(pilotControlType)) {
            String[] types = type.split(",");
            int typesLen = types.length;
            List list = new ArrayList();
            list.add(" a.task_type = '" + types[0] + "' ");
            for (int i = 1; i < typesLen; i++) {
                list.add(" OR a.task_type = '" + types[i] + "' ");
            }
            String getAllMan = "SELECT a.*,GROUP_CONCAT(a.manpower_content) AS manpower_contents, " +
                    "GROUP_CONCAT(a.manpower_time) AS manpower_times  " +
                    "FROM (SELECT * FROM (SELECT * FROM guidance_manpower_task a  " +
                    "WHERE a.task_create = ? AND a.task_daokong_type = " + pilotControlType + " AND " +
                    " (" + StringUtils.join(list, "") + ") " +
                    "ORDER BY a.task_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + ") a  " +
                    "LEFT JOIN guidance_manpower_feedback b " +
                    "ON a.id = b.manpower_id) a GROUP BY a.id ORDER BY a.task_time DESC";
            jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllMan, new String[]{userLoginName}));
        } else {
            // 其他的数据
            String getAllMan = "SELECT a.*,GROUP_CONCAT(a.manpower_content) AS manpower_contents, " +
                    "GROUP_CONCAT(a.manpower_time) AS manpower_times  " +
                    "FROM (SELECT * FROM (SELECT * FROM guidance_manpower_task a  " +
                    "WHERE a.task_create = ? AND a.task_daokong_type = " + pilotControlType + "  " +
                    "ORDER BY a.task_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + ") a  " +
                    "LEFT JOIN guidance_manpower_feedback b " +
                    "ON a.id = b.manpower_id) a GROUP BY a.id ORDER BY a.task_time DESC";
            JSONArray jsonArrayData = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllMan, new String[]{userLoginName}));
            if (jsonArrayData != null) {
                for (int i = 0, jsonArrayLen = jsonArrayData.size(); i < jsonArrayLen; i++) {
                    JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                    String taskType = jsonObject.getString("task_type");
                    String taskTypeSql = "SELECT * FROM guidance_other_config WHERE id = ?";
                    JSONObject jsonObjectType = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(taskTypeSql, new String[]{taskType}));
                    String otherSite = jsonObjectType.getString("other_site");
                    String otherType = jsonObjectType.getString("other_type");
                    String otherTypeName = "";
                    switch (otherType) {
                        case "1":
                            otherTypeName = "阅读量";
                            break;
                        case "2":
                            otherTypeName = "发表评论";
                            break;
                        case "3":
                            otherTypeName = "评论点赞";
                            break;
                        case "4":
                            otherTypeName = "文章收藏";
                            break;
                        default:
                            otherTypeName = "数据错误";
                            break;
                    }
                    jsonObject.put("task_type_name", otherSite + "-" + otherTypeName);
                    jsonArray.add(jsonObject);
                }
            }

        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject changeOrderStatus(String orderId, String orderStatus) {
        String changeStatusSql = "UPDATE guidance_manpower_task SET task_status = ? WHERE id = ?";
        int result = baseDao.execute(changeStatusSql, new String[]{orderStatus, orderId});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllManPowerByType(String type, String pageSize, String pageNumber) {

        List getManTypeList = new ArrayList();
        getManTypeList.add("SELECT a.*,GROUP_CONCAT(a.manpower_content) AS manpower_contents , ");
        getManTypeList.add("GROUP_CONCAT(a.manpower_time) AS manpower_times ");
        getManTypeList.add("FROM (SELECT a.*,b.manpower_content,b.manpower_time  ");
        getManTypeList.add("FROM guidance_manpower_task a  ");
        getManTypeList.add("LEFT JOIN guidance_manpower_feedback  b  ");
        getManTypeList.add("ON a.id = b.manpower_id ");
        if ("1".equals(type)) {
            getManTypeList.add("WHERE a.task_check_status = 1 ");
        } else if ("3".equals(type)) {
            getManTypeList.add("WHERE a.task_check_status = 3 ");
        } else {
            getManTypeList.add("WHERE a.task_check_status = 0 OR a.task_check_status = 2 ");
        }
        getManTypeList.add(" ) a GROUP BY a.id ORDER BY a.task_time DESC  " + SqlEasy.limitPage(pageSize, pageNumber) + " ");
        List getManTypeAllList = new ArrayList();
        String getManType = StringUtils.join(getManTypeList, "");
        getManTypeAllList.add("SELECT a.*,GROUP_CONCAT(a.manpower_content) AS manpower_contents ,  ");
        getManTypeAllList.add("GROUP_CONCAT(a.manpower_time) AS manpower_times  ");
        getManTypeAllList.add("FROM (SELECT a.*,b.manpower_content,b.manpower_time  ");
        getManTypeAllList.add("FROM guidance_manpower_task a ");
        getManTypeAllList.add("LEFT JOIN guidance_manpower_feedback  b  ");
        getManTypeAllList.add("ON a.id = b.manpower_id ");
        if ("1".equals(type)) {
            getManTypeAllList.add("WHERE a.task_check_status = 1 ");
        } else if ("3".equals(type)) {
            getManTypeAllList.add("WHERE a.task_check_status = 3 ");
        } else {
            getManTypeAllList.add("WHERE a.task_check_status = 0 OR a.task_check_status = 2 ");
        }
        getManTypeAllList.add(" ) a GROUP BY a.id ORDER BY a.task_time ");
        String getManTypeAll = StringUtils.join(getManTypeAllList, "");
        JSONArray jsonArrayData = (JSONArray) JSON.toJSON(baseDao.rawQuery(getManType));
        JSONArray jsonArrayAll = (JSONArray) JSON.toJSON(baseDao.rawQuery(getManTypeAll));
        JSONArray jsonArray = new JSONArray();
        if (jsonArrayData != null) {
            for (int i = 0, jsonArrayLen = jsonArrayData.size(); i < jsonArrayLen; i++) {
                JSONObject jsonObject = jsonArrayData.getJSONObject(i);
                String taskType = jsonObject.getString("task_type");
                String taskTypeSql = "SELECT * FROM guidance_other_config WHERE id = ?";
                JSONObject jsonObjectType = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(taskTypeSql, new String[]{taskType}));
                String otherSite = jsonObjectType.getString("other_site");
                String otherType = jsonObjectType.getString("other_type");
                String otherTypeName = "";
                switch (otherType) {
                    case "1":
                        otherTypeName = "阅读量";
                        break;
                    case "2":
                        otherTypeName = "发表评论";
                        break;
                    case "3":
                        otherTypeName = "评论点赞";
                        break;
                    case "4":
                        otherTypeName = "文章收藏";
                        break;
                    default:
                        otherTypeName = "数据错误";
                        break;
                }
                jsonObject.put("task_type_name", otherSite + "-" + otherTypeName);
                jsonArray.add(jsonObject);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        if (jsonArrayAll == null) {
            jsonObject.put("total", 0);
        } else {
            jsonObject.put("total", jsonArrayAll.size());
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateManPowerData(String id, String orderStatus, String orderData) {
        String insertPowerData = "";
        String updatePowerData = "";
        JSONObject orderDataObject = JSON.parseObject(orderData);
        int result = 0;
        if ("1".equals(orderStatus)) {
            // 完成的逻辑
            String mark = orderDataObject.getString("mark");
            String content = orderDataObject.getString("content");
            updatePowerData = "UPDATE guidance_manpower_task SET task_finish_mark = ?,task_check_status = '1',task_end_time = NOW() WHERE id = ? ";
            insertPowerData = "INSERT INTO guidance_manpower_feedback (manpower_id, manpower_content) VALUES (?,?)";
            result = baseDao.execute(updatePowerData, new String[]{mark, id});
            baseDao.execute(insertPowerData, new String[]{id, content});
            JSONObject jsonObject = new JSONObject();
            String getSql = "SELECT * FROM guidance_manpower_task a WHERE a.id = ?";
            jsonObject = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(getSql, new String[]{id}));
            String taskStartMark = jsonObject.getString("task_start_mark");
            String taskCreate = jsonObject.getString("task_create");
            int disparity = Integer.parseInt(taskStartMark) - Integer.parseInt(mark);
            String updateMark = "UPDATE sys_user a SET a.user_mark =  a.user_mark+" + disparity + "  " +
                    "WHERE a.user_loginname = '" + taskCreate + "'";
            baseDao.execute(updateMark);
        } else if ("2".equals(orderStatus)) {
            // 暂停的逻辑
            String content = orderDataObject.getString("content");
            updatePowerData = "UPDATE guidance_manpower_task SET task_check_status = '2' WHERE id = ? ";
            insertPowerData = "INSERT INTO guidance_manpower_feedback (manpower_id, manpower_content) VALUES (?,?)";
            result = baseDao.execute(updatePowerData, new String[]{id});
            baseDao.execute(insertPowerData, new String[]{id, content});
        } else if ("3".equals(orderStatus)) {
            // 拒绝的逻辑
            String content = orderDataObject.getString("content");
            updatePowerData = "UPDATE guidance_manpower_task SET task_check_status = '3',task_end_time = NOW()  WHERE id = ? ";
            insertPowerData = "INSERT INTO guidance_manpower_feedback (manpower_id, manpower_content) VALUES (?,?)";
            result = baseDao.execute(updatePowerData, new String[]{id});
            baseDao.execute(insertPowerData, new String[]{id, content});
            JSONObject jsonObject = new JSONObject();
            String getSql = "SELECT * FROM guidance_manpower_task a WHERE a.id = ?";
            jsonObject = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(getSql, new String[]{id}));
            String taskStartMark = jsonObject.getString("task_start_mark");
            String taskCreate = jsonObject.getString("task_create");
            String updateMark = "UPDATE sys_user a SET a.user_mark =  a.user_mark+" + Integer.parseInt(taskStartMark) + "  " +
                    "WHERE a.user_loginname = '" + taskCreate + "'";
            baseDao.execute(updateMark);
        } else if ("0".equals(orderStatus)) {
            // 反馈的逻辑
            String content = orderDataObject.getString("content");
            insertPowerData = "INSERT INTO guidance_manpower_feedback (manpower_id, manpower_content) VALUES (?,?)";
            updatePowerData = "UPDATE guidance_manpower_task SET task_check_status = '0'  WHERE id = ? ";
            result = baseDao.execute(insertPowerData, new String[]{id, content});
            baseDao.execute(updatePowerData, new String[]{id});
        } else {
            String content = orderDataObject.getString("content");
            insertPowerData = "INSERT INTO guidance_manpower_feedback (manpower_id, manpower_content) VALUES (?,?)";
            result = baseDao.execute(insertPowerData, new String[]{id, content});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    /*    public static void InsertSort(int[] arr) {
        int i, j;
        int n = arr.length;
        int target;


        //假定第一个元素被放到了正确的位置上
        //这样，仅需遍历1 - n-1
        for (i = 1; i < n; i++) {
            j = i;
            target = arr[i];

            while (j > 0 && target < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }

            arr[j] = target;
        }
    }*/
}