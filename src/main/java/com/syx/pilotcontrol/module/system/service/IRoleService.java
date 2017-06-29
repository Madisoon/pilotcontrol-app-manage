package com.syx.pilotcontrol.module.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/16.
 */
public interface IRoleService {

    public JSONArray getAllRole();

    public JSONObject deleteRole(String id);

    public JSONArray getSingleRole(String id);

    public JSONObject changeRole(String roleId, String menuId, String menuPid, String menuPurview);

    public JSONObject insertRole(String roleName);

    public JSONObject updateRoleName(String roleId, String roleName);

    public JSONArray getUserRole(String roleId);
}
