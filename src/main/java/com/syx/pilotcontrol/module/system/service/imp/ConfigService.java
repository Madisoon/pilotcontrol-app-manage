package com.syx.pilotcontrol.module.system.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alienlab.starter.JwtService;
import com.fantasi.common.db.dao.BaseDao;
import com.fantasi.common.db.dao.BaseTableDao;
import com.syx.pilotcontrol.module.system.service.IConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Msater Zg on 2017/6/15.
 */
@Service
public class ConfigService implements IConfigService {
    @Autowired
    BaseTableDao tableDao;

    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertConfig(String configName, String configType, String configId) {
        JSONArray jsonArray = JSON.parseArray(configName);
        JSONArray jsonArrayUrl = JSON.parseArray(configType);
        int jsonLength = jsonArray.size();
        int result = 0;
        if ("".equals(configId)) {
            for (int i = 0; i < jsonLength; i++) {
                String sql = "INSERT INTO guidance_config (config_name,config_pid) VALUE(?,0)";
                result = baseDao.execute(sql, new String[]{(String) jsonArray.get(i)});
            }
        } else {
            for (int i = 0; i < jsonLength; i++) {
                String sql = "INSERT INTO guidance_config (config_name,config_pid,config_type) VALUE(?,?,?)";
                result = baseDao.execute(sql, new String[]{(String) jsonArray.get(i), configId, (String) jsonArrayUrl.get(i)});
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getAllConfig(String configId) {
        String updateModuleSql = "";
        List<Map<String, String>> returnList = new ArrayList<>();
        if ("".equals(configId)) {
            updateModuleSql = "SELECT * FROM guidance_config WHERE config_pid = 0";
            returnList = baseDao.rawQuery(updateModuleSql);
        } else {
            updateModuleSql = "SELECT * FROM guidance_config WHERE config_pid = ? ";
            returnList = baseDao.rawQuery(updateModuleSql, new String[]{configId});
        }
        JSONArray jsonArray = (JSONArray) JSON.toJSON(returnList);
        return jsonArray;
    }

    @Override
    public JSONObject deleteConfig(String configId) {
        String deleteModuleSql = "DELETE FROM guidance_config WHERE id = ? OR config_pid = ?";
        String deleteMenuModuleSql = "DELETE FROM guidance_user_authority WHERE config_id = ?";
        int result = baseDao.execute(deleteModuleSql, new String[]{configId, configId});
        baseDao.execute(deleteMenuModuleSql, new String[]{configId});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateConfig(String configId, String configName, String configType) {
        String updateModuleSql = "";
        int result = 0;
        if ("".equals(configType)) {
            updateModuleSql = "UPDATE guidance_config SET config_name = ? WHERE id = ?  ";
            result = baseDao.execute(updateModuleSql, new String[]{configName, configId});
        } else {
            updateModuleSql = "UPDATE guidance_config SET config_name = ? ,config_type = ?  WHERE id = ?  ";
            result = baseDao.execute(updateModuleSql, new String[]{configName, configType, configId});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getAllTypeConfig(String userName) {
        JSONArray returnData = new JSONArray();
        String sqlConfig = "SELECT * FROM guidance_config WHERE config_pid = 0 ";
        List<Map<String, String>> list = baseDao.rawQuery(sqlConfig);
        JSONArray jsonArrayConfigf = (JSONArray) JSON.toJSON(list);
        String roleMenuSql = "";
        if (!"".equals(userName)) {
            roleMenuSql = "SELECT a.*,b.config_name AS config_parent_name FROM (SELECT b.* " +
                    "FROM  user_guidance_authority a , guidance_config b " +
                    "WHERE a.user_loginname = '" + userName + "' AND a.config_id = b.id) a ,guidance_config b " +
                    "WHERE a.config_pid = b.id ";
            List<Map<String, String>> listRoleData = baseDao.rawQuery(roleMenuSql);
            JSONArray jsonArrayConfigc = (JSONArray) JSON.toJSON(listRoleData);
            int jsonArrayConfigfLen = jsonArrayConfigf.size();
            int jsonArrayConfigcLen = jsonArrayConfigc.size();
            for (int i = 0; i < jsonArrayConfigfLen; i++) {
                JSONObject jsonObjectRole = jsonArrayConfigf.getJSONObject(i);
                JSONArray jsonArray = new JSONArray();
                String id = jsonObjectRole.getString("id");
                for (int j = 0; j < jsonArrayConfigcLen; j++) {
                    JSONObject jsonObject = jsonArrayConfigc.getJSONObject(j);
                    if (id.equals(jsonObject.getString("config_pid"))) {
                        jsonArray.add(jsonObject);
                    }
                }
                if (jsonArray.size() != 0) {
                    returnData.add(jsonArray);
                }
            }
        }
        if ("".equals(userName)) {
            String sqlConfigAll = "SELECT * FROM guidance_config";
            List<Map<String, String>> listAll = baseDao.rawQuery(sqlConfigAll);
            JSONArray jsonArrayAll = (JSONArray) JSON.toJSON(listAll);
            return jsonArrayAll;
        } else {
            return returnData;
        }
    }

    @Override
    public JSONArray getGuidanceAuthority(String userName, String userType) {
        JSONArray returnJson = new JSONArray();
        String getAllConfigType = "SELECT * FROM guidance_config a WHERE a.config_pid = 0";
        JSONArray jsonArray = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllConfigType));
        String getAllConfig = "";
        if ("1".equals(userType)) {
            getAllConfig = "SELECT a.*,b.config_name AS config_parent_name FROM (SELECT * FROM guidance_config a " +
                    "WHERE a.config_pid <> 0) a  " +
                    "LEFT JOIN guidance_config b ON a.config_pid = b.id ";
        } else {
            getAllConfig = "SELECT a.*,b.config_name AS config_parent_name  FROM " +
                    "(SELECT b.* FROM user_guidance_authority a LEFT JOIN  " +
                    "guidance_config b ON a.config_id = b.id " +
                    "WHERE user_loginname = '" + userName + "') a LEFT JOIN guidance_config b  ON a.config_pid = b.id ";
        }
        JSONArray jsonArrayAuthority = (JSONArray) JSON.toJSON(baseDao.rawQuery(getAllConfig));
        for (int i = 0, jsonArrayLen = jsonArray.size(); i < jsonArrayLen; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONArray jsonArraySingle = new JSONArray();
            String configId = jsonObject.getString("id");
            for (int j = 0, jsonArrayAuthorityLen = jsonArrayAuthority.size(); j < jsonArrayAuthorityLen; j++) {
                JSONObject jsonObjectSingle = jsonArrayAuthority.getJSONObject(j);
                if (configId.equals(jsonObjectSingle.getString("config_pid"))) {
                    jsonArraySingle.add(jsonObjectSingle);
                }
            }
            if (jsonArraySingle.size() != 0) {
                returnJson.add(jsonArraySingle);
            }
        }
        return returnJson;
    }
}
