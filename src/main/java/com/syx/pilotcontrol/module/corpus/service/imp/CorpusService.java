package com.syx.pilotcontrol.module.corpus.service.imp;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantasi.common.db.dao.BaseDao;
import com.syx.pilotcontrol.module.corpus.service.ICorpusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Master  Zg on 2016/12/12.
 */

@Service
public class CorpusService implements ICorpusService {
    @Autowired
    BaseDao baseDao;

    @Override
    public JSONArray getCorpusByUserName(String userName, String corpusType) {
        List<Map<String, String>> listData = new ArrayList<>();
        if(corpusType.equals("0")) {
            String getAllCorpus = " SELECT * FROM guidance_corpus_main WHERE corpus_create = ? AND corpus_type = ? ";
            listData = baseDao.rawQuery(getAllCorpus, new String[]{userName, corpusType});
        }else{
            String getAllCorpus = " SELECT * FROM guidance_corpus_main WHERE corpus_type = ? ";
            listData = baseDao.rawQuery(getAllCorpus, new String[]{corpusType});
        }
        JSONArray jsonArray = (JSONArray) JSON.toJSON(listData);
        return jsonArray;
    }

    @Override
    public JSONObject insertCorpus(String corpusName, String corpusId, String corpusType, String createUser) {
        String insertCorpusSql = "INSERT INTO  guidance_corpus_main (corpus_name,corpus_pid,corpus_type,corpus_create) " +
                "VALUES(?,?,?,?)";
        int result = baseDao.execute(insertCorpusSql, new String[]{corpusName, corpusId, corpusType, createUser});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONObject deleteCorpus(String corpusId) {
        String[] corpusIds = corpusId.split(",");
        int corpusIdsLen = corpusIds.length;
        return null;
    }

    @Override
    public JSONObject updateCorpus(String corpusId, String corpusName) {
        String updateCorpusSql = "UPDATE guidance_corpus_main SET corpus_name = ? WHERE id = ?";
        int result = baseDao.execute(updateCorpusSql, new String[]{corpusName, corpusId});
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public JSONArray getCorpusByAuthority(String userName) {
        String getCorpusByAuthoritySql = "SELECT b.* FROM user_corpus_authority a,guidance_corpus_main b " +
                "WHERE a.corpus_id = b.id AND a.user_loginname = ?";
        List<Map<String, String>> list = baseDao.rawQuery(getCorpusByAuthoritySql, new String[]{userName});
        return (JSONArray) JSON.toJSON(list);
    }
}