package com.syx.pilotcontrol.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/8/24.
 */
@Service
public class WebTokenUtil {
    @Value("${sys.code}")
    public String code;

    public String getUserNameByToken(HttpServletRequest request) {
        String webToken = request.getHeader("webToken");
        Claims claims = JwtConfig.parseJWT(webToken, code);
        JSONObject jsonObject = new JSONObject();
        String userLoginName = "";
        try {
            jsonObject = JSON.parseObject(claims.getSubject());
            userLoginName = jsonObject.getString("userLoginName");
        } catch (Exception e) {
            userLoginName = "";
        }
        return userLoginName;
    }
}
