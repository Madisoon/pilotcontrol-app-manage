package com.syx.pilotcontrol.module.corpus.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Msater Zg on 2017/6/20.
 */
public interface ICorpusContextService {
    public JSONObject getAllCorpusContext(String corpusId, String pageSize, String pageNumber);

    public JSONObject insertCorpusContext(String corpusId, String corpusContextNumber, String corpusContext);

    public JSONObject deleteCorpusContext(String corpusContextNumber);

    public JSONObject updateCorpusContext(String corpusContextNumber, String corpusContext);
}
