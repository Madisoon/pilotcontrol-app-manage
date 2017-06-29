package com.syx.pilotcontrol.module.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/15.
 */
public interface IConfigService {

    public JSONObject insertConfig(String configName, String configType, String configId);

    public JSONArray getAllConfig(String configId);

    public JSONObject deleteConfig(String configId);

    public JSONObject updateConfig(String configId, String configName, String configType);

    public JSONArray getAllTypeConfig(String userName);

    public JSONArray getGuidanceAuthority(String userName, String userType);
}
