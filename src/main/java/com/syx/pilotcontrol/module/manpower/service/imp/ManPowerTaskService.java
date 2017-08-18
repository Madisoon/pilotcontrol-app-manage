package com.syx.pilotcontrol.module.manpower.service.imp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.manpower.service.IManPowerTaskService;
import com.syx.pilotcontrol.utils.SqlEasy;
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
        String sqlInsert = SqlEasy.insertObject(manPowerData, "guidance_manpower_task");
        int result = baseDao.execute(sqlInsert);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllManPower(String userLoginName, String type, String pageSize, String pageNumber) {
        String getAllMan = "SELECT a.*,GROUP_CONCAT(a.manpower_content) AS manpower_contents, " +
                "GROUP_CONCAT(a.manpower_time) AS manpower_times  " +
                "FROM (SELECT * FROM (SELECT * FROM guidance_manpower_task a  " +
                "WHERE a.task_type = ?  " +
                "AND a.task_create = ? " +
                "ORDER BY a.task_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + ") a  " +
                "LEFT JOIN guidance_manpower_feedback b " +
                "ON a.id = b.manpower_id) a GROUP BY a.id ORDER BY a.task_time DESC";

        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllMan, new String[]{type, userLoginName}));
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
}