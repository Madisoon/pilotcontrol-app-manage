package com.syx.pilotcontrol.module.system.web;

import com.syx.pilotcontrol.module.system.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Msater Zg on 2017/6/16.
 */
@RequestMapping(value = "/system")
@RestController
@Api(value = "系统角色管理", description = "管理角色的API")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;

    @ApiOperation(value = "获取所有的角色", notes = "无")
    @RequestMapping(value = "/getAllRole", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginName", value = "用户ID", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "adminJudge", value = "用户是否为超级管理员", required = true, dataType = "STRING")
    })
    public String getAllDep() {
        String result = iRoleService.getAllRole().toString();
        return result;
    }

    @RequestMapping(value = "/getSingleRole", method = RequestMethod.GET)
    @ApiOperation(value = "getSingleRole", notes = "获取单个角色的所有的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "STRING")
    })
    public String getSingleRole(@RequestParam("roleId") String roleId) {
        String result = iRoleService.getSingleRole(roleId).toString();
        return result;
    }

    @RequestMapping(value = "/changeRole", method = RequestMethod.POST)
    @ApiOperation(value = "changeRole", notes = "改变角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "menuId", value = "菜单id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "menuPid", value = "菜单父级id", required = true, dataType = "STRING"),
            @ApiImplicitParam(name = "menuPurview", value = "操作权限", required = true, dataType = "STRING")
    })
    public String changeRole(@RequestParam("roleId") String roleId,
                             @RequestParam("menuId") String menuId,
                             @RequestParam("menuPid") String menuPid,
                             @RequestParam("menuPurview") String menuPurview) {
        String result = iRoleService.changeRole(roleId, menuId, menuPid, menuPurview).toString();
        return result;
    }

    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    @ApiOperation(value = "deleteRole", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "STRING")
    })
    public String deleteRole(@RequestParam("roleId") String roleId) {
        String result = iRoleService.deleteRole(roleId).toString();
        return result;
    }

    @RequestMapping(value = "/insertRole", method = RequestMethod.PUT)
    @ApiOperation(value = "insertRole", notes = "插入角色")
    public String insertRole(@RequestParam("roleName") String roleName) {
        String result = iRoleService.insertRole(roleName).toString();
        return result;
    }

    @RequestMapping(value = "/updateRoleName", method = RequestMethod.POST)
    @ApiOperation(value = "updateRoleName", notes = "修改角色")
    public String updateRoleName(@RequestParam("roleName") String roleName,
                                 @RequestParam("roleId") String roleId) {
        String result = iRoleService.updateRoleName(roleId, roleName).toString();
        return result;
    }

    @RequestMapping(value = "/getUserRole", method = RequestMethod.GET)
    @ApiOperation(value = "getUserRole", notes = "获取角色相关的个人信息")
    public String getUserRole(@RequestParam("roleId") String roleId) {
        String result = iRoleService.getUserRole(roleId).toString();
        return result;
    }
}
