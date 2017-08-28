package com.syx.pilotcontrol.module.manpower.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/8/25.
 */
public interface IOtherConfigService {
    public JSONObject insertOtherConfig(String otherData);

    public JSONObject getAllOtherConfig();

    public JSONObject getOtherConfigByType(String getOtherConfigByType);

    public JSONObject deleteOtherConfig(String configId);

    public JSONObject updateOtherConfig(String otherData, String otherId);
}
