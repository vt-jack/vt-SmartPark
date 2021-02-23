package com.snk.web.controller.door;

import com.github.pagehelper.PageHelper;
import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.CwaDevice;
import com.snk.door.domain.Device;
import com.snk.door.service.ICwaDeviceService;
import com.snk.door.service.IDeviceService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考勤设备Controller
 * 
 * @author snk
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/door/cwa/device")
public class CwaDeviceController extends BaseController
{
    private String prefix = "door/cwa/device";

    @Autowired
    private ICwaDeviceService cwaDeviceService;
    @Autowired
    private IDeviceService deviceService;

    @RequiresPermissions("door:cwaDevice:view")
    @GetMapping()
    public String user()
    {
        return prefix + "/device";
    }

    /**
     * 查询设备信息
     */
    @RequiresPermissions("door:cwaDevice:list")
    @PostMapping("/deviceList")
    @ResponseBody
    public TableDataInfo deviceList(Device device)
    {
        PageHelper.clearPage();
        List<Device> list = deviceService.selectDeviceList(device);
        List<Long> deviceIdList = cwaDeviceService.selectCwaDeviceList(new CwaDevice()).stream().map(CwaDevice::getDeviceId).collect(Collectors.toList());
        List<Device> filterList = list.stream().filter(item -> !deviceIdList.contains(item.getId())).collect(Collectors.toList());
        startPage();
        return getDataTableCustom(filterList);
    }

    /**
     * 查询考勤规则列表
     */
    @RequiresPermissions("door:cwaDevice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaDevice cwaDevice)
    {
        startPage();
        List<CwaDevice> list = cwaDeviceService.selectCwaDeviceList(cwaDevice);
        return getDataTable(list);
    }

    /**
     * 新增保存考勤设备
     */
    @RequiresPermissions("door:cwaDevice:add")
    @Log(title = "考勤设备", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody List<CwaDevice> cwaDeviceList)
    {
        cwaDeviceList.stream().forEach(cwaDevice -> {
            cwaDevice.setCreateBy(ShiroUtils.getLoginName());
            cwaDeviceService.insertCwaDevice(cwaDevice);
        });
        return AjaxResult.success();
    }

    /**
     * 删除考勤设备
     */
    @RequiresPermissions("door:cwaDevice:remove")
    @Log(title = "考勤设备", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cwaDeviceService.deleteCwaDeviceByIds(ids));
    }

}
