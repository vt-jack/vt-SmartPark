package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.domain.Ztree;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.DateUtils;
import com.snk.door.api.control.oper.*;
import com.snk.door.api.control.write.*;
import com.snk.door.api.face.AddPersonImageService;
import com.snk.door.api.face.AddPersonService;
import com.snk.door.api.face.ClearPersonDataBaseService;
import com.snk.door.api.face.DeletePersonService;
import com.snk.door.domain.Device;
import com.snk.door.domain.TimeSlot;
import com.snk.door.enums.CardType;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.ITimeSlotService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import zkteco.id100com.jni.id100sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/device")
public class DeviceController extends BaseController
{
    private String prefix = "door/device";

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ITimeSlotService timeSlotService;
    @Autowired
    private FormatControllerService formatControllerService;
    @Autowired
    private OpenDoorService openDoorService;
    @Autowired
    private CloseDoorService closeDoorService;
    @Autowired
    private WriteConnectPasswordService writeConnectPasswordService;
    @Autowired
    private ResetConnectPasswordService resetConnectPasswordService;
    @Autowired
    private HoldDoorService holdDoorService;
    @Autowired
    private LockDoorService lockDoorService;
    @Autowired
    private UnlockDoorService unlockDoorService;
    @Autowired
    private WriteRelayReleaseTimeService writeRelayReleaseTimeService;
    @Autowired
    private WriteRecordModeService writeRecordModeService;
    @Autowired
    private WriteDeadlineService writeDeadlineService;
    @Autowired
    private WriteTCPSettingService writeTCPSettingService;
    @Autowired
    private WritePushButtonSettingService writePushButtonSettingService;
    @Autowired
    private WriteAntiPassbackService writeAntiPassbackService;
    @Autowired
    private TestConnectService testConnectService;
    @Autowired
    private ClearTimeGroupService clearTimeGroupService;
    @Autowired
    private WriteTimeGroupService writeTimeGroupService;
    @Autowired
    private WriteCardListBySequenceService writeCardListBySequenceService;
    @Autowired
    private WriteCardListBySortService writeCardListBySortService;
    @Autowired
    private ClearCardDataBaseService clearCardDataBaseService;
    @Autowired
    private AddPersonService addPersonService;
    @Autowired
    private AddPersonImageService addPersonImageService;
    @Autowired
    private DeletePersonService deletePersonService;
    @Autowired
    private ClearPersonDataBaseService clearPersonDataBaseService;
    @Autowired
    private CloseAlarmService closeAlarmService;

    @RequiresPermissions("door:device:view")
    @GetMapping()
    public String device()
    {
        return prefix + "/device";
    }

    /**
     * 查询设备信息列表
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Device device)
    {
        startPage();
        List<Device> list = deviceService.selectDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 查询设备信息列表
     *
     */
    @RequiresPermissions("door:device:list")
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Device> deviceList = deviceService.selectDeviceList(new Device());
        List<Ztree> ztrees = new ArrayList<>();
        for (Device device : deviceList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(device.getId());
            ztree.setpId(0L);
            ztree.setName(device.getName());
            ztree.setTitle(device.getName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 新增设备信息
     *
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 批量新增保存设备信息
     *
     * @param deviceList 设备信息
     */
    @RequiresPermissions("door:device:add")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping("/addBatch")
    @ResponseBody
    public AjaxResult addBatch(@RequestBody List<Device> deviceList)
    {
        return toAjax(deviceService.insertDeviceBatch(deviceList));
    }

    /**
     * 修改设备信息
     *
     * @param id 主键id
     * @param mmap 页面对象
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap) {
        Device device = deviceService.selectDeviceById(id);
        mmap.put("device", device);
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSn(device.getSn());
        mmap.put("times", timeSlotService.selectTimeSlotList(timeSlot));
        return prefix + "/edit";
    }

    /**
     * 权限授权
     *
     * @param sn SN
     * @param mmap 页面对象
     */
    @RequiresPermissions("door:device:grant")
    @GetMapping("/grant")
    public String auth(@RequestParam("sn") String sn, ModelMap mmap) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setSn(sn);
        mmap.put("times", timeSlotService.selectTimeSlotList(timeSlot));
        return prefix + "/grant";
    }

    /**
     * 操作设备webSocket页面
     */
    @GetMapping("/operate")
    public String operate(@RequestParam DoorOperation operation, ModelMap mmap)
    {
        mmap.put("describe", operation.getDescribe());
        mmap.put("url", operation.getUrl());
        return prefix + "/operate";
    }

    /**
     * 自定义webSocket页面
     */
    @GetMapping("/custom")
    public String custom(@RequestParam DoorOperation operation, ModelMap mmap)
    {
        mmap.put("describe", operation.getDescribe());
        mmap.put("url", operation.getUrl());
        return prefix + "/custom";
    }

    /**
     * 修改保存设备信息
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:edit")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Device device)
    {
        device.setUpdateBy(ShiroUtils.getLoginName());
        device.setUpdateTime(DateUtils.getNowDate());
        return toAjax(deviceService.updateDevice(device));
    }

    /**
     * 删除设备信息
     *
     * @param ids 多个主键ID
     */
    @RequiresPermissions("door:device:remove")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(deviceService.deleteDeviceByIds(ids));
    }

    /**
     * 校验唯一
     *
     * @param device 设备信息
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(Device device)
    {
        return deviceService.checkUnique(device);
    }

    /**
     * 初始化设备
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:init")
    @PostMapping("/init")
    @ResponseBody
    public AjaxResult init(@RequestBody Device device)
    {
        formatControllerService.formatController(device);
        return AjaxResult.success();
    }

    /**
     * 远程开门
     *
     * @param deviceList 设备信息集合
     */
    @RequiresPermissions("door:device:open")
    @PostMapping("/open")
    @ResponseBody
    public AjaxResult open(@RequestBody List<Device> deviceList)
    {
        openDoorService.openDoor(deviceList);
        return AjaxResult.success();
    }

    /**
     * 远程关门
     *
     * @param deviceList 设备信息集合
     */
    @RequiresPermissions("door:device:close")
    @PostMapping("/close")
    @ResponseBody
    public AjaxResult close(@RequestBody List<Device> deviceList)
    {
        closeDoorService.closeDoor(deviceList);
        return AjaxResult.success();
    }

    /**
     * 修改控制器密码
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeCp")
    @PostMapping("/writeCp")
    @ResponseBody
    public AjaxResult writeCp(@RequestBody Device device)
    {
        writeConnectPasswordService.writeConnectPassword(device);
        return AjaxResult.success();
    }

    /**
     * 重置控制器密码
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:resetCp")
    @PostMapping("/resetCp")
    @ResponseBody
    public AjaxResult resetCp(@RequestBody Device device)
    {
        resetConnectPasswordService.resetConnectPassword(device);
        return AjaxResult.success();
    }

    /**
     * 远程保持门常开
     *
     * @param deviceList 设备信息集合
     */
    @RequiresPermissions("door:device:hold")
    @PostMapping("/hold")
    @ResponseBody
    public AjaxResult hold(@RequestBody List<Device> deviceList)
    {
        holdDoorService.holdDoor(deviceList);
        return AjaxResult.success();
    }

    /**
     * 远程锁定门
     *
     * @param deviceList 设备信息集合
     */
    @RequiresPermissions("door:device:lock")
    @PostMapping("/lock")
    @ResponseBody
    public AjaxResult lock(@RequestBody List<Device> deviceList)
    {
        lockDoorService.lockDoor(deviceList);
        return AjaxResult.success();
    }

    /**
     * 远程解除锁定门
     *
     * @param deviceList 设备信息集合
     */
    @RequiresPermissions("door:device:unlock")
    @PostMapping("/unlock")
    @ResponseBody
    public AjaxResult unlock(@RequestBody List<Device> deviceList)
    {
        unlockDoorService.unlockDoor(deviceList);
        return AjaxResult.success();
    }

    /**
     * 远程关闭所有报警
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:closeAlarm")
    @PostMapping("/closeAlarm")
    @ResponseBody
    public AjaxResult closeAlarm(@RequestBody Device device)
    {
        closeAlarmService.closeAlarm(device);
        return AjaxResult.success();
    }

    /**
     * 设置开门保持时长
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeRrt")
    @PostMapping("/writeRrt")
    @ResponseBody
    public AjaxResult writeRrt(@RequestBody Device device)
    {
        writeRelayReleaseTimeService.writeRelayReleaseTime(device);
        return AjaxResult.success();
    }

    /**
     * 设置存储方式
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeRm")
    @PostMapping("/writeRm")
    @ResponseBody
    public AjaxResult writeRm(@RequestBody Device device)
    {
        writeRecordModeService.writeRecordMode(device);
        return AjaxResult.success();
    }

    /**
     * 设置设备有效期
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeDl")
    @PostMapping("/writeDl")
    @ResponseBody
    public AjaxResult writeDl(@RequestBody Device device)
    {
        writeDeadlineService.writeDeadline(device);
        return AjaxResult.success();
    }

    /**
     * 修改TCP网络参数
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeTcp")
    @PostMapping("/writeTcp")
    @ResponseBody
    public AjaxResult writeTcp(@RequestBody Device device)
    {
        writeTCPSettingService.writeTCPSetting(device);
        return AjaxResult.success();
    }

    /**
     * 写入出门按钮
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writePbs")
    @PostMapping("/writePbs")
    @ResponseBody
    public AjaxResult writePbs(@RequestBody Device device)
    {
        writePushButtonSettingService.writePushButtonSetting(device);
        return AjaxResult.success();
    }

    /**
     * 设置防潜回
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:writeAp")
    @PostMapping("/writeAp")
    @ResponseBody
    public AjaxResult writeAp(@RequestBody Device device)
    {
        writeAntiPassbackService.writeAntiPassback(device);
        return AjaxResult.success();
    }

    @PostMapping("/connect")
    @ResponseBody
    public AjaxResult connect(@RequestBody Device device)
    {
        testConnectService.testConnect(device);
        return AjaxResult.success();
    }

    /**
     * 数据监控webSocket页面
     */
    @RequiresPermissions("door:device:watch")
    @GetMapping("/watch")
    public String watch(ModelMap mmap)
    {
        mmap.put("url", DoorOperation.BEGIN_WATCH.getUrl());
        return prefix + "/watch";
    }

    /**
     * 清空开门时段
     *
     * @param device 设备
     */
    @RequiresPermissions("door:device:clearTg")
    @PostMapping("/clearTg")
    @ResponseBody
    public AjaxResult clearTg(@RequestBody Device device)
    {
        clearTimeGroupService.clearTimeGroup(device);
        return AjaxResult.success();
    }

    /**
     * 设置开门时段
     *
     * @param device 设备
     */
    @RequiresPermissions("door:device:writeTg")
    @PostMapping("/writeTg")
    @ResponseBody
    public AjaxResult writeTg(@RequestBody Device device)
    {
        writeTimeGroupService.writeTimeGroup(device);
        return AjaxResult.success();
    }

    /**
     * 允许通行-单设备
     *
     * @param device 设备信息
     */
    @RequiresPermissions("door:device:grant")
    @PostMapping("/writeCdb")
    @ResponseBody
    public AjaxResult writeCdb(@RequestBody Device device)
    {
        writeCardList(device, false);
        return AjaxResult.success();
    }

    /**
     * 写卡
     *
     * @param device 设备信息
     * @param ban 禁止标识
     */
    private void writeCardList(Device device, Boolean ban) {
        DoorOperation operation = ban ? DoorOperation.BAN_CARD_LIST : DoorOperation.WRITE_CARD_LIST;
        if (isControl(device)) {
            if (CardType.SORT.getValue().equals(device.getCardType())) {
                writeCardListBySortService.writeCardListBySort(device, operation, true);
            } else {
                writeCardListBySequenceService.writeCardListBySequence(device, operation, true);
            }
        } else {
            if (ban) {
                deletePersonService.deletePerson(device, true);
            } else {
                addPersonService.addPerson(device, true);
                addPersonImageService.addPerson(device, true);
            }
        }
    }

    /**
     * 禁止通行
     *
     * @param device 设备
     */
    @RequiresPermissions("door:device:grant")
    @PostMapping("/banCdb")
    @ResponseBody
    public AjaxResult banCdb(@RequestBody Device device)
    {
        writeCardList(device, true);
        return AjaxResult.success();
    }

    /**
     * 清空卡片数据
     *
     * @param device 设备
     */
    @RequiresPermissions("door:device:clearCdb")
    @PostMapping("/clearCdb")
    @ResponseBody
    public AjaxResult clearCdb(@RequestBody Device device)
    {
        if (isControl(device)) {
            clearCardDataBaseService.clearCardDataBase(device, true);
        } else {
            clearPersonDataBaseService.clearPersonDataBase(device);
        }
        return AjaxResult.success();
    }

    private Boolean isControl(Device device) {
        if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) {
            return true;
        }
        return false;
    }

}
