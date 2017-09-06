package com.syx.pilotcontrol.module.manpower.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.manpower.service.IMessageConfigService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Msater Zg on 2017/8/31.
 */
@Service
public class MessageConfigService implements IMessageConfigService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject insertMessageConfig(String messageData) {
        String insertSql = SqlEasy.insertObject(messageData, "guidance_message_people");
        int result = baseDao.execute(insertSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateMessageConfig(String messageData, String messageId) {
        String updateSql = SqlEasy.updateObject(messageData, "guidance_message_people", "id = " + messageId);
        int result = baseDao.execute(updateSql);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteMessageConfig(String messageId) {
        String[] messageIds = messageId.split(",");
        int messageIdsLen = messageIds.length;
        int result = 0;
        for (int i = 0; i < messageIdsLen; i++) {
            String deleteSql = "DELETE FROM guidance_message_people WHERE id = ?";
            result = baseDao.execute(deleteSql, new String[]{messageIds[i]});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }
}
