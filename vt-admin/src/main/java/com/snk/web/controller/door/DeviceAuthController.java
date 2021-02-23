package com.snk.web.controller.door;

import com.alibaba.fastjson.JSON;
import com.snk.common.annotation.Log;
import com.snk.common.constant.Constants;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.poi.ExcelUtil;
import com.snk.door.api.control.write.WriteCardListBySequenceService;
import com.snk.door.api.control.write.WriteCardListBySortService;
import com.snk.door.api.control.write.WriteTimeGroupService;
import com.snk.door.api.face.AddPersonImageService;
import com.snk.door.api.face.AddPersonService;
import com.snk.door.api.face.DeletePersonService;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.TimeSlot;
import com.snk.door.enums.CardType;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.DoorOperation;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.ITimeSlotService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 开门权限
 * 
 * @author snk
 */
@Controller
@RequestMapping("/door/device/auth")
public class DeviceAuthController extends BaseController
{
    private String prefix = "door/device/auth";

    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private ITimeSlotService timeSlotService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private WriteCardListBySequenceService writeCardListBySequenceService;
    @Autowired
    private WriteCardListBySortService writeCardListBySortService;
    @Autowired
    private WriteTimeGroupService writeTimeGroupService;
    @Autowired
    private AddPersonImageService addPersonImageService;
    @Autowired
    private AddPersonService addPersonService;
    @Autowired
    private DeletePersonService deletePersonService;

    @RequiresPermissions("door:auth:view")
    @GetMapping()
    public String auth()
    {
        return prefix + "/auth";
    }

    @RequiresPermissions("door:auth:grant")
    @GetMapping("/grant")
    public String grant(ModelMap mmap)
    {
        mmap.put("times", timeSlotService.selectTimeSlotListByUserId(ShiroUtils.getUserId()));
        return prefix + "/grant";
    }

    @RequiresPermissions("door:auth:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Auth auth)
    {
        startPage();
        List<Auth> authList = deviceAuthService.selectAuthList(auth);
        return getDataTable(authList);
    }

    /**
     * 新增
     */
    @RequiresPermissions("door:auth:pass")
    @Log(title = "开门权限", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody List<Auth> list)
    {
        return toAjax(deviceAuthService.insertAuth(list));
    }

    /**
     * 删除
     */
    @RequiresPermissions("door:auth:remove")
    @Log(title = "开门权限", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(deviceAuthService.deleteDeviceAuthByIds(ids));
    }

    /**
     * 删除
     */
    @RequiresPermissions("door:auth:ban")
    @Log(title = "开门权限", businessType = BusinessType.DELETE)
    @PostMapping("/ban")
    @ResponseBody
    public AjaxResult ban(@RequestBody List<Auth> list)
    {
        return toAjax(deviceAuthService.deleteAuth(list));
    }

    /**
     * 根据用户删除
     */
    @Log(title = "开门权限", businessType = BusinessType.DELETE)
    @PostMapping("/remove/userId")
    @ResponseBody
    public AjaxResult removeByUserId(@RequestParam("userId") Long userId)
    {
        return toAjax(deviceAuthService.deleteAuthByUserId(userId));
    }

    /**
     * 权限上传
     */
    @RequiresPermissions("door:auth:upload")
    @Log(title = "开门权限", businessType = BusinessType.GRANT)
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestBody List<Auth> list)
    {
        deviceAuthService.uploadAuth(list);
        return AjaxResult.success();
    }

    /**
     * 导出权限列表
     */
    @RequiresPermissions("door:auth:export")
    @Log(title = "开门权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Auth auth)
    {
        List<Auth> list = deviceAuthService.selectAuthList(auth);
        ExcelUtil<Auth> util = new ExcelUtil<>(Auth.class);
        return util.exportExcel(list, "权限列表");
    }

    /**
     * 允许通行-多设备
     *
     * @param map {"deviceList": "设备集合", "authList": "权限集合"}
     */
    @RequiresPermissions("door:auth:passupload")
    @PostMapping("/writeCdb")
    @ResponseBody
    public AjaxResult writeCdb(@RequestBody Map<String, Object> map)
    {
        writeCardList(map, false);
        return AjaxResult.success();
    }

    /**
     * 写卡
     *
     * @param map {"deviceList": "设备集合", "authList": "权限集合"}
     * @param ban 禁止标识
     */
    private void writeCardList(Map<String, Object> map, Boolean ban) {
        // 设备数据
        List deviceMl = (List) map.get("deviceList");
        List<Device> deviceList = new ArrayList<>();
        deviceMl.stream().forEach(item -> deviceList.add(JSON.parseObject(JSON.toJSONString(item), Device.class)));
        Map<String, List<Device>> deviceMap = deviceList.stream().collect(Collectors.groupingBy(Device::getSn));
        // 权限数据
        List authMl = (List) map.get("authList");
        List<Auth> authList = new ArrayList<>();
        authMl.stream().forEach(item -> authList.add(JSON.parseObject(JSON.toJSONString(item), Auth.class)));
        for (String sn : deviceMap.keySet()) {
            threadPoolTaskExecutor.execute(() -> {
                List<Device> devices = deviceMap.get(sn);
                if (!CollectionUtils.isEmpty(devices)) {
                    Device device = deviceService.selectDeviceById(devices.get(0).getId());
                    List<Auth> auths = new ArrayList<>();
                    authList.stream().forEach(a -> devices.stream().forEach(d -> {
                        Auth auth = new Auth();
                        BeanUtils.copyProperties(a, auth);
                        auth.setSn(d.getSn());
                        auth.setDeviceId(d.getId());
                        auth.setPositionName(d.getPositionName());
                        auth.setControlPort(d.getControlPort());
                        auths.add(auth);
                    }));
                    if (!CollectionUtils.isEmpty(auths)) {
                        if (!ban) { // 允许通行才需要判断需不需要新增时段
                            Integer slotIdx = auths.get(0).getSlotIdx();
                            if (1 != slotIdx) { // 如果不是时段01 需要新增时段到设备
                                TimeSlot timeSlot = timeSlotService.selectTimeSlotById(auths.get(0).getSlotId());
                                timeSlot.setSn(device.getSn());
                                device.setParam(JSON.parseObject(JSON.toJSONString(timeSlot), HashMap.class));
                                writeTimeGroupService.writeTimeGroup(device);
                            }
                        }
                        DoorOperation operation = ban ? DoorOperation.BAN_CARD_LIST : DoorOperation.WRITE_CARD_LIST;
                        device.setParam(auths);
                        if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) {
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
                }
            });
        }
    }

    /**
     * 禁止通行-多设备
     *
     * @param map {"deviceList": "设备集合", "authList": "权限集合"}
     */
    @RequiresPermissions("door:auth:banupload")
    @PostMapping("/banCdb")
    @ResponseBody
    public AjaxResult banCdb(@RequestBody Map<String, Object> map)
    {
        writeCardList(map, true);
        return AjaxResult.success();
    }
}
