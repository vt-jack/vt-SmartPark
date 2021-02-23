package com.snk.web.controller.door;

import com.github.pagehelper.PageHelper;
import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.Manager;
import com.snk.door.service.IDeviceManagerService;
import com.snk.framework.util.ShiroUtils;
import com.snk.system.domain.SysUser;
import com.snk.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备权限信息
 * 
 * @author snk
 */
@Controller
@RequestMapping("/door/device/manager")
public class DeviceManagerController extends BaseController
{
    @Autowired
    private IDeviceManagerService deviceManagerService;

    @Autowired
    private ISysUserService userService;

    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Manager manager)
    {
        startPage();
        List<Manager> list = deviceManagerService.selectManagerList(manager);
        return getDataTable(list);
    }

    @PostMapping("/userList/{deviceId}")
    @ResponseBody
    public TableDataInfo userList(@PathVariable("deviceId") Long deviceId, SysUser user)
    {
        PageHelper.clearPage();
        List<SysUser> list = userService.selectUserList(user);
        Manager queryParam = new Manager();
        queryParam.setDeviceId(deviceId);
        List<Long> userIdList = deviceManagerService.selectManagerList(queryParam).stream().map(Manager::getUserId).collect(Collectors.toList());
        List<SysUser> filterList = list.stream().filter(item -> !userIdList.contains(item.getUserId())).collect(Collectors.toList());
        startPage();
        return getDataTableCustom(ShiroUtils.getSysUser().isAdmin() ? filterList.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList())
                : filterList.stream().filter(r -> !r.isSnkAdmin()).collect(Collectors.toList()));
    }

    /**
     * 新增保存设备权限
     */
    @RequiresPermissions("door:device:manager")
    @Log(title = "设备权限管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody List<Manager> managerList)
    {
        managerList.stream().forEach(item -> {
            item.setCreateBy(ShiroUtils.getLoginName());
        });
        return toAjax(deviceManagerService.insertManagerBatch(managerList));
    }

    /**
     * 删除设备权限信息
     *
     * @param ids 多个主键ID
     */
    @RequiresPermissions("door:device:manager")
    @Log(title = "设备权限管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(deviceManagerService.deleteManagerByIds(ids));
    }
}
