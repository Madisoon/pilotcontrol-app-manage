package com.syx.pilotcontrol.module.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/14.
 */
public interface IUserService {
    public JSONObject insertSysUser(String userData, String userCorpus, String userConfig);

    public JSONArray getAllSysUser();

    public JSONObject deleteUser(String userName);

    public JSONObject updateUser(String userData, String userCorpus, String userConfig);

    public JSONObject judgeUser(String userName, String userPassword);

    public JSONObject getUserInfoSingle(String userName);

    public JSONObject getSysUser(String userName);

}
