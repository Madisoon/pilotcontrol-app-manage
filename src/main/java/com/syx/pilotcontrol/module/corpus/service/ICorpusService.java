package com.syx.pilotcontrol.module.corpus.service;/*


/**
 * Created by Master  Zg on 2016/12/12.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ICorpusService {
    public JSONArray getCorpusByUserName(String userName,String corpusType);

    public JSONObject insertCorpus(String corpusName, String corpusId, String corpusType, String createUser);

    public JSONObject deleteCorpus(String corpusId);

    public JSONObject updateCorpus(String corpusId,String corpusName);

    public JSONArray getCorpusByAuthority(String userName);
}