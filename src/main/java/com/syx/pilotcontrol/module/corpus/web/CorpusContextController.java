package com.syx.pilotcontrol.module.corpus.web;

import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.corpus.service.ICorpusContextService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/6/20.
 */
@RestController
@RequestMapping(value = "/corpus")
@Api(value = "CorpusContextController", description = "管理语料内容的方法")
public class CorpusContextController {
    @Autowired
    ICorpusContextService iCorpusContextService;

    @RequestMapping(value = "/insertCorpusContext", method = RequestMethod.PUT)
    @ApiOperation(value = "insertCorpusContext", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusId", value = "语料id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusContextNumber", value = "语料内容编号", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusContext", value = "语料内容", required = true, dataType = "STRING")
    })
    public String insertCorpusContext(@RequestParam("corpusId") String corpusId,
                                      @RequestParam("corpusContextNumber") String corpusContextNumber,
                                      @RequestParam("corpusContext") String corpusContext) {
        String result = iCorpusContextService.insertCorpusContext(corpusId, corpusContextNumber, corpusContext).toString();
        return result;
    }

    @RequestMapping(value = "/updateCorpusContext", method = RequestMethod.POST)
    @ApiOperation(value = "updateCorpusContext", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusContextNumber", value = "语料内容编号", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusContext", value = "语料内容", required = true, dataType = "STRING")
    })
    public String updateCorpusContext(@RequestParam("corpusContextNumber") String corpusContextNumber,
                                      @RequestParam("corpusContext") String corpusContext) {
        String result = iCorpusContextService.updateCorpusContext(corpusContextNumber, corpusContext).toString();
        return result;
    }

    @RequestMapping(value = "/deleteCorpusContext", method = RequestMethod.POST)
    @ApiOperation(value = "deleteCorpusContext", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusContextNumber", value = "语料内容编号", required = true, dataType = "STRING")
    })
    public String deleteCorpusContext(@RequestParam("corpusContextNumber") String corpusContextNumber) {
        String result = iCorpusContextService.deleteCorpusContext(corpusContextNumber).toString();
        return result;
    }

    @RequestMapping(value = "/getAllCorpusContext", method = RequestMethod.POST)
    @ApiOperation(value = "getAllCorpusContext", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusId", value = "语料内容编号", required = true, dataType = "STRING")
    })
    public String getAllCorpusContext(HttpServletRequest request) {
        try {
            String param = IOUtils.toString(request.getInputStream(), "utf-8");
            JSONObject params = JSONObject.parseObject(param);
            String pageNumber = params.getString("pageNumber");
            String pageSize = params.getString("pageSize");
            String corpusId = params.getString("corpusId");
            String result = "";
            result = iCorpusContextService.getAllCorpusContext(corpusId, pageSize, pageNumber).toString();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "数据异常";
        }
    }
}
