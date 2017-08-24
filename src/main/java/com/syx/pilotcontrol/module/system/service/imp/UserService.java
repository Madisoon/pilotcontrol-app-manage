package com.syx.pilotcontrol.module.system.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.config.JwtConfig;
import com.syx.pilotcontrol.module.system.service.IConfigService;
import com.syx.pilotcontrol.module.system.service.IUserService;
import com.syx.pilotcontrol.utils.Md5Azdg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Msater Zg on 2017/6/14.
 */
@Service
public class UserService implements IUserService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    JwtConfig jwtConfig;
    @Autowired
    IConfigService iConfigService;

    @Value("${sys.code}")
    String cipherCode;

    @Override
    public JSONObject insertSysUser(String userData, String userCorpus, String userConfig) {
        JSONObject jsonObject = JSONObject.parseObject(userData);
        String userLoginname = jsonObject.getString("user_loginname");
        String userName = jsonObject.getString("user_name");
        String userPhone = jsonObject.getString("user_phone");
        String userRole = jsonObject.getString("user_role");
        String passWord = Md5Azdg.md5s(jsonObject.getString("user_password"));
        String insertUserSql = "INSERT INTO sys_user (user_loginname, user_password, user_name, user_phone) VALUES(?,?,?,?)";
        String insertUserRole = "INSERT INTO  sys_role_user (role_id, user_id) VALUES(?,?)";
        String[] userCorpuss = userCorpus.split(",");
        int result = baseDao.execute(insertUserSql, new String[]{userLoginname, passWord, userName, userPhone});
        baseDao.execute(insertUserRole, new String[]{userRole, userLoginname});
        for (int i = 0, userCorpussLen = userCorpuss.length; i < userCorpussLen; i++) {
            String insertCorpus = "INSERT INTO user_corpus_authority (user_loginname, corpus_id) VALUES(?,?)";
            baseDao.execute(insertCorpus, new String[]{userLoginname, userCorpuss[i]});
        }
        String[] userConfigs = userConfig.split(",");
        for (int i = 0, userConfigLen = userConfigs.length; i < userConfigLen; i++) {
            String insertCorpus = "INSERT INTO user_guidance_authority (user_loginname, config_id) VALUES(?,?)";
            baseDao.execute(insertCorpus, new String[]{userLoginname, userConfigs[i]});
        }
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getAllSysUser() {
        List list = new ArrayList();
        list.add(" SELECT a.*,GROUP_CONCAT(a.config_id) AS config_ids, GROUP_CONCAT(a.config_name) AS config_names ");
        list.add(" FROM (SELECT a.*,c.id AS config_id,c.config_name FROM (SELECT a.*,GROUP_CONCAT(a.corpus_id) AS corpus_ids, ");
        list.add(" GROUP_CONCAT(a.corpus_name) AS corpus_names FROM (SELECT a.*,c.role_name ,c.id AS user_role ,e.id AS corpus_id,e.corpus_name FROM sys_user a, ");
        list.add(" sys_role_user b, sys_role c ");
        list.add(" ,user_corpus_authority d ");
        list.add(" ,guidance_corpus_main e ");
        list.add(" WHERE a.user_loginname = b.user_id AND b.role_id = c.id ");
        list.add(" AND a.user_loginname = d.user_loginname AND d.corpus_id = e.id) a GROUP BY a.id) a ,user_guidance_authority b,guidance_config c ");
        list.add(" WHERE a.user_loginname = b.user_loginname AND b.config_id = c.id ) a GROUP BY a.id ");
        List<Map<String, String>> listData = baseDao.rawQuery(StringUtils.join(list, ""));
        JSONArray jsonArray = (JSONArray) JSON.toJSON(listData);
        return jsonArray;
    }

    @Override
    public JSONObject deleteUser(String userName) {
        String[] userNames = userName.split(",");
        int userNamesLen = userNames.length;
        int result = 0;
        for (int i = 0; i < userNamesLen; i++) {
            String deleteSysUser = "DELETE FROM sys_user WHERE user_loginname = ?";
            String deleteCorpusUser = "DELETE FROM user_corpus_authority WHERE user_loginname = ?";
            String deleteRoleUser = "DELETE FROM sys_role_user WHERE user_id = ?";
            String deletePrivateCorpus = "DELETE FROM guidance_corpus_main WHERE corpus_create = ?";
            String deleteConfigUser = "DELETE FROM user_guidance_authority WHERE user_loginname = ?";
            result = baseDao.execute(deleteSysUser, new String[]{userNames[i]});
            baseDao.execute(deleteSysUser, new String[]{userNames[i]});
            baseDao.execute(deleteCorpusUser, new String[]{userNames[i]});
            baseDao.execute(deleteRoleUser, new String[]{userNames[i]});
            baseDao.execute(deletePrivateCorpus, new String[]{userNames[i]});
            baseDao.execute(deleteConfigUser, new String[]{userNames[i]});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateUser(String userData, String userCorpus, String userConfig) {
        JSONObject jsonObject = JSON.parseObject(userData);
        String userName = jsonObject.getString("user_loginname");
        String userPassword = jsonObject.getString("user_password");
        if (jsonObject.getString("user_role") != null && !"".equals(jsonObject.getString("user_role"))) {
            String sql = "UPDATE sys_role_user SET role_id = ? WHERE user_id = ?";
            baseDao.execute(sql, new String[]{jsonObject.getString("user_role"), userName});
        }
        if (userPassword != null && !"".equals(userPassword)) {
            jsonObject.put("user_password", Md5Azdg.md5s(userPassword));
        }
        Set<String> set = jsonObject.keySet();
        Iterator<String> iterator = set.iterator();
        List<String> list = new ArrayList<>();
        list.add("UPDATE sys_user SET ");
        int n = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!"user_role".equals(key)) {
                if (n == 0) {
                    list.add("" + key + " = '" + jsonObject.getString(key) + "' ");
                } else {
                    list.add("," + key + " = '" + jsonObject.getString(key) + "' ");
                }
                n++;
            }
        }
        list.add("WHERE user_loginname =  '" + userName + "'");
        baseDao.execute("DELETE FROM user_guidance_authority WHERE user_loginname = ?", new String[]{userName});
        baseDao.execute("DELETE FROM user_corpus_authority WHERE user_loginname = ?", new String[]{userName});
        String[] userCorpuss = userCorpus.split(",");
        for (int i = 0, userCorpussLen = userCorpuss.length; i < userCorpussLen; i++) {
            String insertCorpus = "INSERT INTO user_corpus_authority (user_loginname, corpus_id) VALUES(?,?)";
            baseDao.execute(insertCorpus, new String[]{userName, userCorpuss[i]});
        }
        String[] userConfigs = userConfig.split(",");
        for (int i = 0, userConfigLen = userConfigs.length; i < userConfigLen; i++) {
            String insertConfig = "INSERT INTO user_guidance_authority (user_loginname, config_id) VALUES(?,?)";
            baseDao.execute(insertConfig, new String[]{userName, userConfigs[i]});
        }
        int result = baseDao.execute(StringUtils.join(list, ""));
        JSONObject returnResult = new JSONObject();
        returnResult.put("result", result);
        return returnResult;
    }

    @Override
    public JSONObject judgeUser(String userName, String userPassword) {
        JSONObject jsonObjectData = getUserInfoSingle(userName);
        JSONObject returnJson = new JSONObject();
        if (jsonObjectData.isEmpty()) {
            returnJson.put("result", 0);
        } else {
            //密码会采用md5加密的方式
            if (jsonObjectData.getString("user_password").equals(Md5Azdg.md5s(userPassword))) {
                //密码匹配 得到菜单等一些其他的信息
                //放入用户的基础信息
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userLoginName", userName);
                String jwtToken = jwtConfig.createJWT(jsonObject, "zg", "pilotcontrol-app-manage", 604800, cipherCode);
                /*Claims claims = jwtConfig.parseJWT(jwtToken, "091418wa!");*/
                jsonObjectData.put("token", jwtToken);
                List<String> listModule = new ArrayList<>();
                listModule.add("SELECT f.menu_id,f.menu_pid,f.menu_name,f.menu_content,f.menu_attr ");
                listModule.add("FROM sys_user a,sys_role_user b,sys_role c,sys_role_menu d,sys_menu f  ");
                listModule.add("WHERE a.user_loginname = '" + userName + "'  ");
                listModule.add("AND a.user_loginname = b.user_id AND b.role_id = c.id  ");
                listModule.add("AND b.role_id = d.role_id AND d.menu_id = f.menu_id  ");
                listModule.add("AND f.menu_pid = 0 ORDER BY f.menu_sort ");
                List<String> listFunction = new ArrayList<>();
                listFunction.add("SELECT a.*,b.menu_name AS menu_parent_name,b.menu_attr AS menu_parent_attr FROM (SELECT f.menu_id,f.menu_pid,f.menu_name,f.menu_content,f.menu_attr ");
                listFunction.add("FROM sys_user a,sys_role_user b,sys_role c,sys_role_menu d,sys_menu f  ");
                listFunction.add(" WHERE a.user_loginname = '" + userName + "'  ");
                listFunction.add(" AND a.user_loginname = b.user_id AND b.role_id = c.id  ");
                listFunction.add("AND b.role_id = d.role_id AND d.menu_id = f.menu_id  ");
                listFunction.add("AND f.menu_pid <> 0) a LEFT JOIN sys_menu b ON a.menu_pid = b.menu_id ");
                String listModuleString = StringUtils.join(listModule, "");
                String listFunctionString = StringUtils.join(listFunction, "");
                List<Map<String, String>> execResultModule = baseDao.rawQuery(listModuleString);
                List<Map<String, String>> execResultFunction = baseDao.rawQuery(listFunctionString);
                JSONArray jsonArrayModule = (JSONArray) JSON.toJSON(execResultModule);
                JSONArray jsonArrayFunction = (JSONArray) JSON.toJSON(execResultFunction);
                JSONArray jsonObjectFunction = new JSONArray();
                for (int i = 0, len = jsonArrayModule.size(); i < len; i++) {
                    JSONObject jsModule = jsonArrayModule.getJSONObject(i);
                    JSONArray arryObject = new JSONArray();
                    String menu_id = jsModule.getString("menu_id");
                    for (int j = 0, lenF = jsonArrayFunction.size(); j < lenF; j++) {
                        JSONObject jsFunction = jsonArrayFunction.getJSONObject(j);
                        if (menu_id.equals(jsFunction.getString("menu_pid"))) {
                            arryObject.add(jsFunction);
                        }
                    }
                    jsonObjectFunction.add(arryObject);

                }
                returnJson.put("user", jsonObjectData);
                returnJson.put("module", jsonArrayModule);
                returnJson.put("function", jsonObjectFunction);
                returnJson.put("config", iConfigService.getAllTypeConfig(userName));
                returnJson.put("token", jwtToken);
                returnJson.put("result", 1);
                System.out.println(jwtToken);
            } else {
                //密码不匹配
                returnJson.put("result", 0);
            }
        }
        return returnJson;
    }

    @Override
    public JSONObject getUserInfoSingle(String userName) {
        String getUserInfoSql = "SELECT a.*,b.role_name FROM (SELECT a.*,b.role_id FROM sys_user a " +
                "LEFT JOIN sys_role_user b  " +
                "ON a.user_loginname = b.user_id WHERE a.user_loginname = ?) a  " +
                "LEFT JOIN  sys_role b " +
                "ON a.role_id = b.id ";
        Map map = baseDao.rawQueryForMap(getUserInfoSql, new String[]{userName});
        JSONObject jsonObject = (JSONObject) JSON.toJSON(map);
        return jsonObject;
    }

    @Override
    public JSONObject getSysUser(String userName) {
        String getSql = "SELECT * FROM sys_user a WHERE a.user_loginname = ? ";
        JSONObject jsonObject = (JSONObject) JSON.toJSON(baseDao.rawQueryForMap(getSql, new String[]{userName}));
        return jsonObject;
    }
}
