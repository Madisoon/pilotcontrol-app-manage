package com.syx.pilotcontrol.module.corpus.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.syx.pilotcontrol.module.corpus.service.ICorpusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Msater Zg on 2017/6/14.
 */
@RestController
@RequestMapping(value = "/corpus")
@Api(value = "CorpusController", description = "管理语料的方法")
public class CorpusController {
    @Autowired
    ICorpusService iCorpusService;

    @RequestMapping(value = "/getCorpusByUserName", method = RequestMethod.GET)
    @ApiOperation(value = "getCorpusByUserName", notes = "获取语料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户账号", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusType", value = "语料分组类型", required = true, dataType = "STRING")
    })
    public String getCorpusByUserName(@RequestParam("userName") String userName,
                                      @RequestParam("corpusType") String corpusType) {
        String result = iCorpusService.getCorpusByUserName(userName, corpusType).toString();
        return result;
    }

    @RequestMapping(value = "/insertCorpus", method = RequestMethod.PUT)
    @ApiOperation(value = "insertCorpus", notes = "添加配置qq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusName", value = "语料名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusId", value = "语料父级id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusType", value = "语料类型", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "createUser", value = "创建者", required = true, dataType = "STRING")
    })
    public String insertCorpus(@RequestParam("corpusName") String corpusName,
                               @RequestParam("corpusId") String corpusId,
                               @RequestParam("corpusType") String corpusType,
                               @RequestParam("createUser") String createUser) {
        String result = iCorpusService.insertCorpus(corpusName, corpusId, corpusType, createUser).toString();
        return result;
    }

    @RequestMapping(value = "/updateCorpus", method = RequestMethod.POST)
    @ApiOperation(value = "updateCorpus", notes = "修改语料组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusName", value = "语料名称", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "corpusId", value = "语料父级id", required = true, dataType = "STRING")
    })
    public String updateCorpus(@RequestParam("corpusName") String corpusName,
                               @RequestParam("corpusId") String corpusId) {
        String result = iCorpusService.updateCorpus(corpusId, corpusName).toString();
        return result;
    }

    @RequestMapping(value = "/deleteCorpus", method = RequestMethod.POST)
    @ApiOperation(value = "deleteCorpus", notes = "删除语料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "corpusId", value = "语料", required = true, dataType = "STRING")
    })
    public String deleteCorpus(@RequestParam("corpusId") String corpusId) {
        String result = iCorpusService.deleteCorpus(corpusId).toString();
        return result;
    }

    @RequestMapping(value = "/getCorpusByAuthority", method = RequestMethod.GET)
    @ApiOperation(value = "getCorpusByAuthority", notes = "根据权限获取系统语料")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名称", required = true, dataType = "STRING")
    })
    public String getCorpusByAuthority(@RequestParam("userName") String userName) {
        String result = iCorpusService.getCorpusByAuthority(userName).toString();
        return result;
    }

}
