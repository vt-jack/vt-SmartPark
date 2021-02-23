package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.constant.Constants;
import com.snk.common.core.text.Convert;
import com.snk.common.utils.PageUtil;
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
import com.snk.door.mapper.DeviceAuthMapper;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.IDeviceService;
import com.snk.door.service.ITimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 开门权限管理 服务实现
 *
 * @author snk
 */
@Service
public class DeviceAuthServiceImpl implements IDeviceAuthService {

    @Autowired
    private DeviceAuthMapper deviceAuthMapper;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private ITimeSlotService timeSlotService;
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

    /**
     * 查询开门权限
     *
     * @param id 开门权限ID
     * @return 开门权限
     */
    @Override
    public Auth selectAuthById(Long id){
        return deviceAuthMapper.selectAuthById(id);
    }

    /**
     * 查询开门权限
     *
     * @param userId 人员ID
     * @return 开门权限
     */
    @Override
    public List<Auth> selectAuthByUserId(Long userId) {
        return deviceAuthMapper.selectAuthByUserId(userId);
    }

    /**
     * 查询开门权限
     *
     * @param deviceIds 设备ID
     * @return 开门权限
     */
    @Override
    public int deleteAuthByDeviceIds(String deviceIds) {
        return deviceAuthMapper.deleteAuthByDeviceIds(Convert.toStrArray(deviceIds));
    }

    /**
     * 查询开门权限列表
     *
     * @param auth 开门权限
     * @return 开门权限集合
     */
    @Override
    public Auth selectAuth(Auth auth) {
        return deviceAuthMapper.selectAuth(auth);
    }

    /**
     * 查询开门权限列表
     *
     * @param auth 开门权限
     * @return 开门权限集合
     */
    @Override
    @DataScopeSnk(deviceAlias = "c")
    public List<Auth> selectAuthList(Auth auth) {
        return deviceAuthMapper.selectAuthList(auth);
    }

    /**
     * 查询开门权限列表
     *
     * @param sn SN
     * @return 开门权限集合
     */
    @Override
    public List<Auth> selectAuthBySn(String sn) {
        Auth auth = new Auth();
        auth.setSn(sn);
        return deviceAuthMapper.selectAuthList(auth);
    }

    /**
     * 新增开门权限
     * @param auth 开门权限
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAuth(Auth auth) {
        int result = 0;
        result = deviceAuthMapper.updateAuth(auth);
        if (result == 0 && "0".equals(auth.getDelFlag())) {
            result = deviceAuthMapper.insertAuth(auth);
        }
        return result;
    }

    /**
     * 新增开门权限
     *
     * @param list 开门权限
     * @return 结果
     */
    @Override
    @Transactional
    public int insertAuth(List<Auth> list) {
        list.stream().forEach(item -> {
            if (deviceAuthMapper.updateAuth(item) == 0 && "0".equals(item.getDelFlag())) {
                deviceAuthMapper.insertAuth(item);
            }
        });
        return 1;
    }

    /**
     * 上传开门权限
     *
     * @param list 开门权限
     * @return 结果
     */
    @Override
    @Transactional
    public void uploadAuth(List<Auth> list) {
        // 按SN分组上传
        Map<String, List<Auth>> map = list.stream().filter(item -> !StringUtils.isEmpty(item.getSn())).collect(Collectors.groupingBy(auth -> auth.getSn()));
        for (String key : map.keySet()) {
            threadPoolTaskExecutor.execute(() -> {
                List<Device> deviceList = deviceService.selectDeviceBySn(key);  // 根据SN获取设备信息
                if (!CollectionUtils.isEmpty(deviceList)) {
                    Device od = deviceList.get(0);  // 取第一条设备信息
                    List<Auth> authList = map.get(key); // 权限数据
                    // 过来新增权限数据，然后按照时段ID分组
                    Map<Long, List<Auth>> mapSlotId = authList.stream().filter(item -> "0".equals(item.getDelFlag())).collect(Collectors.groupingBy(Auth::getSlotId));
                    for (Long slotId : mapSlotId.keySet()) {
                        TimeSlot timeSlot = timeSlotService.selectTimeSlotById(slotId); // 查询时段
                        // 时段下标不等于默认时段并且时段是系统时段时，需要将时段上传到控制板
                        if (timeSlot.getIdx() != 1 && timeSlot.getSn().contains(Constants.TIME_SLOT_DEFAULT_SN)) {
                            Device device = od;
                            timeSlot.setSn(device.getSn()); // 覆盖SN
                            device.setParam(timeSlot);
                            writeTimeGroupService.writeTimeGroup(device);   // 上传时段到控制板
                        }
                    }
                    if (DeviceType.CONTROL.getValue().equals(od.getDeviceType())) {
                        Device device = od;
                        device.setParam(authList);  // 上传权限数据
                        if (CardType.SORT.getValue().equals(device.getCardType())) {    // 排序区
                            writeCardListBySortService.writeCardListBySort(device, DoorOperation.WRITE_CARD_LIST, false);
                        } else {    // 非排序区
                            writeCardListBySequenceService.writeCardListBySequence(device, DoorOperation.WRITE_CARD_LIST, false);
                        }
                    } else {
                        List<Auth> addList = authList.stream().filter(item -> "0".equals(item.getDelFlag())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(addList)) {
                            Device device = od;
                            device.setParam(addList);
                            addPersonService.addPerson(device, false);
                            addPersonImageService.addPerson(device, false);
                        }
                        List<Auth> delList = authList.stream().filter(item -> "1".equals(item.getDelFlag())).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(delList)) {
                            Device device = od;
                            device.setParam(delList);
                            deletePersonService.deletePerson(device, false);
                        }
                    }
                }
            });
        }
    }

    /**
     * 删除开门权限
     *
     * @param list 开门权限
     * @return 结果
     */
    @Override
    public int deleteAuth(List<Auth> list) {
        list.stream().forEach(item -> deviceAuthMapper.deleteAuth(item));
        return 1;
    }

    /**
     * 删除开门权限
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteAuthByUserId(Long userId) {
        return deviceAuthMapper.deleteAuthByUserId(userId);
    }

    /**
     * 删除开门权限
     *
     * @param sn 设备SN
     * @return 结果
     */
    @Override
    public int deleteAuthBySn(String sn) {
        return deviceAuthMapper.deleteAuthBySn(sn);
    }

    /**
     * 批量删除开门权限
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDeviceAuthByIds(String ids) {
        String[] idArray = Convert.toStrArray(ids);
        return deviceAuthMapper.deleteDeviceAuthByIds(idArray);
    }

}
