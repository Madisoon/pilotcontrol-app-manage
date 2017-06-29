package com.syx.pilotcontrol.module.corpus.service.imp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.corpus.service.ICorpusContextService;
import com.syx.pilotcontrol.utils.SqlEasy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Msater Zg on 2017/6/20.
 */
@Service
public class CorpusContextService implements ICorpusContextService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONObject getAllCorpusContext(String corpusId, String pageSize, String pageNumber) {
        String[] corpusIds = corpusId.split(",");
        int corpusIdsLen = corpusIds.length;
        JSONObject jsonObject = new JSONObject();
        String getSql = "SELECT * FROM guidance_corpus_assistant WHERE corpus_id = " + corpusIds[0];
        for (int i = 1; i < corpusIdsLen; i++) {
            getSql += " OR corpus_id = " + corpusIds[i] + "";
        }
        List<Map<String, String>> list = baseDao.rawQuery(getSql);
        int listSize = list.size();
        getSql+= SqlEasy.limitPage(pageSize, pageNumber);
        List<Map<String, String>> listData = baseDao.rawQuery(getSql);
        JSONArray jsonArray = (JSONArray) JSON.toJSON(listData);
        if (listSize == 0) {
            jsonObject.put("total", 0);
        }else{
            jsonObject.put("total", listSize);
        }
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    @Override
    public JSONObject insertCorpusContext(String corpusId, String corpusContextNumber, String corpusContext) {
        String insertSql = "INSERT INTO  guidance_corpus_assistant (corpus_id,corpus_context,corpus_number) VALUES(?,?,?)";
        int result = baseDao.execute(insertSql, new String[]{corpusId, corpusContext, corpusContextNumber});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteCorpusContext(String corpusContextNumber) {
        String deleteSql = "DELETE FROM guidance_corpus_assistant WHERE corpus_number = ?";
        String[] corpusNumbers = corpusContextNumber.split(",");
        int corpusNumbersLen = corpusNumbers.length;
        int result = 0;
        for (int i = 0; i < corpusNumbersLen; i++) {
            result = baseDao.execute(deleteSql, new String[]{corpusNumbers[i]});
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject updateCorpusContext(String corpusContextNumber, String corpusContext) {
        String updateSql = "UPDATE guidance_corpus_assistant SET corpus_context = ? WHERE corpus_number = ?";
        int result = baseDao.execute(updateSql, new String[]{corpusContext, corpusContextNumber});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }
}
