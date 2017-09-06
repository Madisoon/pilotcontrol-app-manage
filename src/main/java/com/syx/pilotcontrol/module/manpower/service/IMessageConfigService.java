package com.syx.pilotcontrol.module.manpower.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/8/31.
 */
public interface IMessageConfigService {
    public JSONObject insertMessageConfig(String messageData);

    public JSONObject updateMessageConfig(String messageData, String messageId);

    public JSONObject deleteMessageConfig(String messageId);
}
