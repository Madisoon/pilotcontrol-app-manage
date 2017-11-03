package com.syx.pilotcontrol.module.config.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.config.service.IButtonService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Msater Zg on 2017/8/11.
 */
@Service
public class ButtonService implements IButtonService {

    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertButton(String buttonData, String markData) {
        String insertSql = SqlEasy.insertObject(buttonData, "sys_butfton");
        JSONArray jsonArray = JSON.parseArray(markData);
        int result = baseDao.execute(insertSql);
        JSONObject jsonObject = new JSONObject();
        if (result == 1) {
            String getSelect = "SELECT * FROM sys_button ORDER BY id DESC LIMIT 0,1";
            JSONObject jsonObjectData = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(getSelect));
            String buttonId = jsonObjectData.getString("id");
            int jsonArrayLen = jsonArray.size();
            for (int i = 0; i < jsonArrayLen; i++) {
                JSONObject jsonObjectMark = jsonArray.getJSONObject(i);
                jsonObjectMark.put("button_id", buttonId);
                baseDao.execute(SqlEasy.insertObject(jsonObjectMark.toJSONString(), "sys_button_mark"));
            }
            jsonObject.put("result", 1);
        } else {
            jsonObject.put("result", 0);
        }
        return jsonObject;
    }

    @Override
    public JSONObject updateButton(String buttonData, String markData, String buttonId) {
        String updateSql = SqlEasy.updateObject(buttonData, "sys_button", "id = " + buttonId);
        JSONArray jsonArray = JSON.parseArray(markData);
        int result = baseDao.execute(updateSql);
        JSONObject jsonObject = new JSONObject();
        if (result == 1) {
            baseDao.execute("DELETE FROM sys_button_mark WHERE  button_id = " + buttonId);
            int jsonArrayLen = jsonArray.size();
            for (int i = 0; i < jsonArrayLen; i++) {
                JSONObject jsonObjectMark = jsonArray.getJSONObject(i);
                jsonObjectMark.put("button_id", buttonId);
                baseDao.execute(SqlEasy.insertObject(jsonObjectMark.toJSONString(), "sys_button_mark"));
            }
            jsonObject.put("result", 1);
        } else {
            jsonObject.put("result", 0);
        }
        return jsonObject;
    }

    @Override
    public JSONObject deleteButton(String buttonId) {
        String[] buttonIds = buttonId.split(",");
        int buttonIdsLen = buttonIds.length;
        int result = 0;
        for (int i = 0; i < buttonIdsLen; i++) {
            String buttonArrayId = buttonIds[i];
            result = baseDao.execute("DELETE FROM  sys_button WHERE  id= ?", new String[]{buttonArrayId});
            baseDao.execute("DELETE FROM  sys_button_mark WHERE  button_id= ?", new String[]{buttonArrayId});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllButton() {
        String allButtonSql = "SELECT a.*,GROUP_CONCAT(a.mark_limit) AS mark_limits, " +
                "GROUP_CONCAT(a.mark_name) AS mark_names, " +
                "GROUP_CONCAT(a.mark_number) AS mark_numbers " +
                "FROM (SELECT a.*,b.mark_limit,b.mark_name, " +
                "b.mark_number FROM sys_button a  " +
                "LEFT JOIN sys_button_mark b " +
                "ON a.id = b.button_id) a GROUP BY a.id ";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(allButtonSql));
        JSONObject jsonObject = new JSONObject();
        if (jsonArray == null) {
            jsonObject.put("total", 0);
        } else {
            jsonObject.put("total", jsonArray.size());
            jsonObject.put("data", jsonArray);
        }
        return jsonObject;
    }

    @Override
    public JSONObject getButtonByName(String buttonClass) {
        String buttonNameSql = " SELECT a.*,GROUP_CONCAT(a.mark_limit) AS mark_limits, " +
                " GROUP_CONCAT(a.mark_name) AS mark_names, " +
                " GROUP_CONCAT(a.mark_number) AS mark_numbers " +
                " FROM (SELECT a.*,b.mark_limit,b.mark_name, " +
                " b.mark_number FROM sys_button a  " +
                " LEFT JOIN sys_button_mark b  " +
                " ON a.id = b.button_id WHERE button_class = ? ) a GROUP BY a.id  ";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectButton = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(buttonNameSql, new String[]{buttonClass}));
        jsonObject.put("data", jsonObjectButton);
        return jsonObject;
    }
}
