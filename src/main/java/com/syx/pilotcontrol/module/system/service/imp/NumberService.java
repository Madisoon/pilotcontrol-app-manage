package com.syx.pilotcontrol.module.system.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.system.service.INumberService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Msater Zg on 2017/6/22.
 */
@Service
public class NumberService implements INumberService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertNumber(String numberData) {
        String insertNumberSql = SqlEasy.insertObject(numberData, "guidance_number");
        int result = baseDao.execute(insertNumberSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteNumber(String numberId) {
        String[] numberIds = numberId.split(",");
        String deleteSql = "DELETE FROM guidance_number WHERE id = " + numberIds[0] + "";
        for (int i = 1, numberIdsLen = numberIds.length; i < numberIdsLen; i++) {
            deleteSql += " OR id = " + numberIds[i] + "";
        }
        int result = baseDao.execute(deleteSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateNumber(String numberData, String numberId) {
        String updateSql = SqlEasy.updateObject(numberData, "guidance_number", "id = " + numberId);
        System.out.println(updateSql);
        int result = baseDao.execute(updateSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllNumber(String configId, String numberType, String numberCreate, String pageSize, String pageNumber) {
        // 获取数据的数量的sql
        String sqlTotal = new String();
        String sqlNumber = "";
        if ("1".equals(numberType)) {
            // 获取所有的系统的账号
            sqlTotal = "SELECT COUNT(id) AS total FROM guidance_number  WHERE number_sys_status = 1 AND config_id = " + configId;
            sqlNumber = "SELECT a.*,b.user_name FROM  guidance_number a " +
                    "LEFT JOIN sys_user b " +
                    "ON a.number_create = b.user_loginname " +
                    "WHERE (a.number_sys_status = 1 OR number_open = 1) AND config_id = " + configId + " ORDER BY a.number_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + "";
        } else {
            if ("".equals(numberCreate)) {
                // 系统获取所有私人的
                sqlTotal = "SELECT COUNT(id) AS total FROM guidance_number  WHERE number_sys_status = 0 AND config_id = " + configId;
                sqlNumber = " SELECT a.*,b.user_name FROM  guidance_number a, sys_user b " +
                        "WHERE a.number_sys_status = 0 AND config_id = " + configId + " AND a.number_create = b.user_loginname " +
                        "ORDER BY a.number_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + "";
            } else {
                // 个人获取私人的账号
                sqlTotal = "SELECT COUNT(id) AS total FROM guidance_number  WHERE number_sys_status = 1 AND  number_create = '" + numberCreate + "' AND config_id = " + configId;
                sqlNumber = " SELECT a.*,b.user_name FROM  guidance_number a, sys_user b " +
                        "WHERE a.number_sys_status = 0 AND config_id = " + configId + " AND a.number_create = b.user_loginname AND a.number_create = '" + numberCreate + "' " +
                        "ORDER BY a.number_time DESC " + SqlEasy.limitPage(pageSize, pageNumber) + "";
            }
        }
        Map<String, String> map = baseDao.rawQueryForMap(sqlTotal);
        JSONObject jsonObject = new JSONObject();
        List<Map<String, String>> listData = baseDao.rawQuery(sqlNumber);
        JSONArray jsonArray = (JSONArray) JSON.toJSON(listData);
        jsonObject.put("total", map.get("total"));
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject updateNumberOpen(String numberId) {
        String updateOpenStatus = "UPDATE guidance_number SET number_open = 1 WHERE id = ?";
        int result = baseDao.execute(updateOpenStatus, new String[]{numberId});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }
}
