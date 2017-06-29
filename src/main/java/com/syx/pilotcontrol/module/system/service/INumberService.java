package com.syx.pilotcontrol.module.system.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/22.
 */
public interface INumberService {
    public JSONObject insertNumber(String numberData);

    public JSONObject deleteNumber(String numberId);

    public JSONObject updateNumber(String numberData, String numberId);

    public JSONObject updateNumberOpen(String numberId);

    public JSONObject getAllNumber(String configId, String numberType, String numberCreate, String pageSize, String pageNumber);
}
