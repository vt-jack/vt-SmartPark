package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.StringUtils;
import com.snk.door.api.control.oper.ClearPasswordDateBaseService;
import com.snk.door.api.control.write.WritePasswordService;
import com.snk.door.domain.Device;
import com.snk.door.domain.Pwd;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDevicePwdService;
import com.snk.door.service.IDeviceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 开门密码信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/device/pwd")
public class DevicePwdController extends BaseController
{
    private String prefix = "door/device/pwd";

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IDevicePwdService devicePwdService;
    @Autowired
    private WritePasswordService writePasswordService;
    @Autowired
    private ClearPasswordDateBaseService clearPasswordDateBaseService;

    @RequiresPermissions("door:pwd:view")
    @GetMapping()
    public String model()
    {
        return prefix + "/pwd";
    }

    /**
     * 查询设备信息列表
     */
    @RequiresPermissions("door:pwd:list")
    @PostMapping("/deviceList")
    @ResponseBody
    public TableDataInfo deviceList(Device device)
    {
        startPage();
        device.setGroupBy("a.sn");
        List<Device> deviceList = deviceService.selectDeviceList(device);
        return getDataTable(deviceList);
    }

    /**
     * 查询开门密码信息列表
     */
    @RequiresPermissions("door:pwd:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Pwd pwd)
    {
        startPage();
        List<Pwd> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(pwd.getSn()) && !SymbolConstants.COMMA.equals(pwd.getSn())) {
            list = devicePwdService.selectPwdList(pwd);
        }
        return getDataTable(list);
    }

    /**
     * 新增开门密码信息
     */
    @GetMapping("/add/{sn}")
    public String add(@PathVariable("sn") String sn, ModelMap mmap)
    {
        mmap.put("deviceList", deviceService.selectDeviceBySn(sn));
        return prefix + "/add";
    }

    /**
     * 新增保存开门密码信息
     */
    @RequiresPermissions("door:pwd:add")
    @Log(title = "开门密码信息", businessType = BusinessType.INSERT)
    @PostMapping("/writePwd")
    @ResponseBody
    public AjaxResult addSave(@RequestBody Device device)
    {
        writePasswordService.writePassword(device, DoorOperation.WRITE_PASSWORD);
        return AjaxResult.success();
    }

    /**
     * 删除开门密码信息
     */
    @RequiresPermissions("door:pwd:remove")
    @Log(title = "开门密码信息", businessType = BusinessType.DELETE)
    @PostMapping( "/deletePwd")
    @ResponseBody
    public AjaxResult remove(@RequestBody Device device)
    {
        writePasswordService.writePassword(device, DoorOperation.DELETE_PASSWORD);
        return AjaxResult.success();
    }

    /**
     * 删除开门密码信息
     */
    @RequiresPermissions("door:pwd:clear")
    @Log(title = "开门密码信息", businessType = BusinessType.DELETE)
    @PostMapping( "/clearPwd")
    @ResponseBody
    public AjaxResult clear(@RequestBody Device device)
    {
        clearPasswordDateBaseService.clearPasswordDateBase(device);
        return AjaxResult.success();
    }

}
