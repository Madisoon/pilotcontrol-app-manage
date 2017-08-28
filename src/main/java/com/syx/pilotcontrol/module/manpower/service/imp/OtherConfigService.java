package com.syx.pilotcontrol.module.manpower.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.manpower.service.IOtherConfigService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Msater Zg on 2017/8/25.
 */
@Service
public class OtherConfigService implements IOtherConfigService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertOtherConfig(String otherData) {
        String insertSql = SqlEasy.insertObject(otherData, "guidance_other_config");
        int result;
        result = baseDao.execute(insertSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getAllOtherConfig() {
        String getAllOtherConfigSql = "SELECT * FROM guidance_other_config";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllOtherConfigSql));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject deleteOtherConfig(String configId) {
        String[] configIdS = configId.split(",");
        int configIdSLen = configIdS.length;
        List list = new ArrayList();
        list.add("DELETE FROM guidance_other_config WHERE (id = " + configIdS[0] + "");
        for (int i = 1; i < configIdSLen; i++) {
            list.add(" OR id = " + configIdS[i] + "");
        }
        list.add(" )");
        int reuslt = baseDao.execute(StringUtils.join(list, ""));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", reuslt);
        return jsonObject;
    }

    @Override
    public JSONObject updateOtherConfig(String otherData, String otherId) {
        String sql = SqlEasy.updateObject(otherData, "guidance_other_config", " id = " + otherId);
        int result = baseDao.execute(sql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject getOtherConfigByType(String getOtherConfigByType) {
        String getOtherConfig = "SELECT * FROM guidance_other_config a WHERE a.other_type = ? AND other_status =1";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getOtherConfig, new String[]{getOtherConfigByType}));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }
}
