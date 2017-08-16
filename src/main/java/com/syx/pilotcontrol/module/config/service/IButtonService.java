package com.syx.pilotcontrol.module.config.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/8/11.
 */
public interface IButtonService {
    public JSONObject insertButton(String buttonData, String markData);

    public JSONObject updateButton(String buttonData, String markData, String buttonId);

    public JSONObject deleteButton(String buttonId);

    public JSONObject getAllButton();

    public JSONObject getButtonByName(String buttonClass);
}
