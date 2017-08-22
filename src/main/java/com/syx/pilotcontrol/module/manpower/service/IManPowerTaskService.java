package com.syx.pilotcontrol.module.manpower.service;/*


/**
 * Created by Master  Zg on 2016/12/12.
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerationException;

public interface IManPowerTaskService {
    public JSONObject insertManPower(String manPowerData);

    public JSONObject getAllManPower(String userLoginName, String type, String pageSize, String pageNumber);


    public JSONObject changeOrderStatus(String orderId, String orderStatus);

    public JSONObject getAllManPowerByType(String type, String pageSize, String pageNumber);

    public JSONObject updateManPowerData(String id, String orderStatus, String orderData);

}