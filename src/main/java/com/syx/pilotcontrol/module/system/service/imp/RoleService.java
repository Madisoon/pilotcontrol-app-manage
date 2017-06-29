package com.syx.pilotcontrol.module.system.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.fantasi.common.db.dao.BaseTableDao;
import com.syx.pilotcontrol.module.system.service.IRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Msater Zg on 2017/6/16.
 */
@Service
public class RoleService implements IRoleService {
    @Autowired
    BaseTableDao tableDao;

    @Autowired
    BaseDao baseDao;

    @Override
    public JSONArray getAllRole() {
        JSONArray jsonArray = new JSONArray();
        List<Map<String, String>> list = new ArrayList<>();
        list = tableDao.queryTable("sys_role");
        jsonArray = (JSONArray) JSON.toJSON(list);
        return jsonArray;
    }

    @Override
    public JSONObject deleteRole(String id) {
        JSONObject jsonObject = new JSONObject();
        String deleteRole = "DELETE FROM sys_role WHERE id= ? ";
        String deleteRoleMenu = "DELETE FROM sys_role_menu WHERE role_id = ? ";
        String deleteRoleUser = "DELETE FROM sys_role_user WHERE role_id = ? ";
        int result = baseDao.execute(deleteRole, new String[]{id});
        baseDao.execute(deleteRoleMenu, new String[]{id});
        baseDao.execute(deleteRoleUser, new String[]{id});
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getSingleRole(String id) {
        JSONArray returnData = new JSONArray();
        String sqlRole = "SELECT * FROM sys_menu WHERE menu_pid = '0' ";
        List<Map<String, String>> list = new ArrayList<>();
        list = baseDao.rawQuery(sqlRole);
        JSONArray jsonArrayRole = (JSONArray) JSON.toJSON(list);
        List<String> roleMenuSqlList = new ArrayList<>();
        roleMenuSqlList.add("SELECT * FROM ");
        roleMenuSqlList.add("(SELECT a.*,b.menu_name AS menu_first_name FROM sys_menu a,sys_menu b  ");
        roleMenuSqlList.add("WHERE a.menu_pid<>0 AND a.menu_pid = b.menu_id) a  ");
        roleMenuSqlList.add("LEFT JOIN ");
        roleMenuSqlList.add("(SELECT b.menu_id AS judge_menu_id FROM sys_role a,sys_role_menu b  ");
        roleMenuSqlList.add("WHERE a.id = b.role_id AND a.id = ? ) b  ");
        roleMenuSqlList.add("ON a.menu_id = b.judge_menu_id  ORDER BY a.menu_name ");
        String roleMenuSql = StringUtils.join(roleMenuSqlList, "");
        List<Map<String, String>> listRoleData = baseDao.rawQuery(roleMenuSql, new String[]{id});
        JSONArray jsonArrayRoleMenu = (JSONArray) JSON.toJSON(listRoleData);
        int jsonArrayRoleLen = jsonArrayRole.size();
        int jsonArrayRoleMenuLen = jsonArrayRoleMenu.size();
        for (int i = 0; i < jsonArrayRoleLen; i++) {
            JSONObject jsonObjectRole = jsonArrayRole.getJSONObject(i);
            JSONArray jsonArray = new JSONArray();
            String menu_id = jsonObjectRole.getString("menu_id");
            for (int j = 0; j < jsonArrayRoleMenuLen; j++) {
                JSONObject jsonObjectRoleMenu = jsonArrayRoleMenu.getJSONObject(j);
                if (menu_id.equals(jsonObjectRoleMenu.getString("menu_pid"))) {
                    jsonArray.add(jsonObjectRoleMenu);
                }
            }
            returnData.add(jsonArray);
        }
        return returnData;
    }

    @Override
    public JSONObject changeRole(String roleId, String menuId, String menuPid, String menuPurview) {
        JSONObject jsonObject = new JSONObject();
        int result = 0;
        if ("0".equals(menuPurview)) {
            //添加的逻辑
            String sql = "SELECT * FROM sys_role_menu WHERE role_id = ? AND menu_id = ? ";
            List<Map<String,String>> list = baseDao.rawQuery(sql, new String[]{roleId, menuPid});
            String sql_ = "INSERT INTO sys_role_menu (role_id,menu_id) VALUES(?,?)";
            result = baseDao.execute(sql_,new String[]{roleId, menuId});
            if (list.size() == 0) {
                sql_ = "INSERT INTO sys_role_menu (role_id,menu_id) VALUES(?,?)";
                baseDao.execute(sql_,new String[]{roleId, menuPid});
            }
        } else {
            //取消的逻辑的逻辑
            String deleteSql = "DELETE FROM sys_role_menu WHERE role_id = ? AND menu_id = ? ";
            result = baseDao.execute(deleteSql, new String[]{roleId, menuId});
            String selectSql = "SELECT * FROM sys_menu a ,sys_role_menu b WHERE a.menu_pid = ? AND a.menu_id = b.menu_id AND b.role_id= ? ";
            List<Map<String,String>> list = baseDao.rawQuery(selectSql, new String[]{menuPid, roleId});
            if (list.size() == 0) {
                deleteSql = "DELETE FROM sys_role_menu WHERE role_id = ? AND menu_id = ? ";
                baseDao.execute(deleteSql,new String[]{roleId, menuPid});
            }
        }
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject insertRole(String roleName) {
        JSONObject jsonObject = new JSONObject();
        String[] roleNames = roleName.split(",");
        int roleLen = roleNames.length;
        int result = 0;
        for (int i = 0; i < roleLen; i++) {
            String sql = "insert into sys_role (role_name) values (?)";
            result = baseDao.execute(sql, new String[]{roleNames[i]});
        }
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateRoleName(String roleId, String roleName) {
        JSONObject jsonObject = new JSONObject();
        String updateRoleNameSql = "UPDATE sys_role  SET  role_name = ?  WHERE id = ? ";
        int result = baseDao.execute(updateRoleNameSql, new String[]{roleName, roleId});
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getUserRole(String roleId) {
        JSONArray jsonArray = new JSONArray();
        String getUserRoleSql = "SELECT b.* FROM sys_role_user a,sys_user b WHERE a.role_id = ? AND a.user_id = b.user_loginname";
        List<Map<String,String>> list = new ArrayList<>();
        list = baseDao.rawQuery(getUserRoleSql, new String[]{roleId});
        jsonArray = (JSONArray) JSON.toJSON(list);
        return jsonArray;
    }
}
