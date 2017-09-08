package com.syx.pilotcontrol.module.manpower.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.manpower.service.IMessageConfigService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Msater Zg on 2017/8/31.
 */
@Service
public class MessageConfigService implements IMessageConfigService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertMessageConfig(String messageData) {
        String insertSql = SqlEasy.insertObject(messageData, "guidance_message_people");
        int result = baseDao.execute(insertSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateMessageConfig(String messageData, String messageId) {
        String updateSql = SqlEasy.updateObject(messageData, "guidance_message_people", "id = " + messageId);
        int result = baseDao.execute(updateSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteMessageConfig(String messageId) {
        String[] messageIds = messageId.split(",");
        int messageIdsLen = messageIds.length;
        int result = 0;
        for (int i = 0; i < messageIdsLen; i++) {
            String deleteSql = "DELETE FROM guidance_message_people WHERE id = ?";
            result = baseDao.execute(deleteSql, new String[]{messageIds[i]});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getMessageConfig() {
        String getSql = "SELECT a.*,b.plan_name FROM guidance_message_people a " +
                "LEFT JOIN sys_plan b ON a.people_plan = b.id";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getSql));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject addPlan(String planData) {
        String sql = SqlEasy.insertObject(planData, "sys_plan");
        int result = baseDao.execute(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllPlan() {
        String sql = "SELECT * FROM sys_plan";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(sql));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject deletePlan(String idData) {
        String[] idS = idData.split(",");
        int idSLen = idS.length;
        List<String> list = new ArrayList<>();
        list.add("DELETE FROM sys_plan WHERE id=" + idS[0]);
        for (int i = 1; i < idSLen; i++) {
            list.add("OR id = " + idS[i]);
        }
        int result = baseDao.execute(StringUtils.join(list, ""));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updatePlan(String planData, String id) {
        String sql = SqlEasy.updateObject(planData, "sys_plan", "id = " + id + "");
        int result = baseDao.execute(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }
}
