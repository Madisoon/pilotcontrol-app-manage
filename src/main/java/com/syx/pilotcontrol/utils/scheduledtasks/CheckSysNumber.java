
package com.syx.pilotcontrol.utils.scheduledtasks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Msater Zg on 2017/8/4.
 */
@Component
public class CheckSysNumber {
    @Autowired
    BaseDao baseDao;
    private final String PILCONTROLURL = "http://121.199.4.149:18080/api/verify";

    @Scheduled(cron = "0 0/60 * * * ?")
    /*@Scheduled(fixedRate = 1000 * 60*30)*/

    public void getDataNumber() {
        String getAllNumber = "SELECT a.id,a.number_name,a.number_password,b.config_type " +
                "FROM guidance_number a  " +
                "LEFT JOIN  guidance_config b  " +
                "ON a.config_id = b.id ";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllNumber));
        int jsonArrayLen = jsonArray.size();
        for (int i = 0; i < jsonArrayLen; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String numberId = jsonObject.getString("id");
            String numberName = jsonObject.getString("number_name");
            String numberPwd = jsonObject.getString("number_password");
            String numberType = jsonObject.getString("config_type");
            Map map = new HashMap();
            map.put("url", numberType);
            map.put("account", numberName);
            map.put("password", numberPwd);
            JSONObject jsonObjectReturn = HttpClientUtil.sendPost(PILCONTROLURL, map);
            String value = jsonObjectReturn.getString("status");

            if (!"1".equals(value)) {
                String updateSql = "UPDATE guidance_number SET number_check_status = ? WHERE id = ? ";
                baseDao.execute(updateSql, new String[]{"0", numberId});
            }
        }

    }
}

