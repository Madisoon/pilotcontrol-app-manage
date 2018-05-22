package com.syx.pilotcontrol.module.system.service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Master  Zg on 2016/11/15.
 */
public interface IModuleService {
    public JSONObject insertModule(String moduleValue, String moduleUrl, String moduleId);

    public JSONArray getAllModule();

    public JSONObject deleteModule(String moduleId);

    public JSONArray getAllSecondModule(String moduleId);

    public JSONObject updateModuleInfo(String menuId, String menuName, String menuContent);
}
