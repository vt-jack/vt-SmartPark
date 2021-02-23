package com.snk.door.service.impl;

import com.alibaba.fastjson.JSON;
import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.config.Global;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.exception.BusinessException;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.file.FileUploadUtils;
import com.snk.common.utils.security.PermissionUtils;
import com.snk.door.domain.*;
import com.snk.door.mapper.VisitRegMapper;
import com.snk.door.service.IDeviceAuthService;
import com.snk.door.service.ITimeSlotService;
import com.snk.door.service.IVisitAuthService;
import com.snk.door.service.IVisitRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 企业Service业务层处理
 * 
 * @author snk
 * @date 2020-11-18
 */
@Service
public class VisitRegServiceImpl implements IVisitRegService
{
    @Autowired
    private VisitRegMapper visitRegMapper;
    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private IVisitAuthService visitAuthService;
    @Autowired
    private ITimeSlotService timeSlotService;

    /**
     * 查询企业信息
     *
     * @param id 企业信息ID
     * @return 企业信息
     */
    @Override
    public VisitReg selectVisitRegById(Long id) {
        return visitRegMapper.selectVisitRegById(id);
    }

    /**
     * 查询来访登记
     *
     * @param cardNo 卡号
     * @return 来访登记
     */
    @Override
    public VisitReg selectVisitRegByCardNo(String cardNo) {
        return visitRegMapper.selectVisitRegByCardNo(cardNo);
    }

    /**
     * 根据卡号或者手机号查询登记信息
     * @param visitReg
     * @return
     */
    @Override
    @DataScopeSnk(userAlias = "c")
    public List<VisitReg> selectBySearchValue(VisitReg visitReg) {
        return visitRegMapper.selectBySearchValue(visitReg);
    }

    /**
     * 查询企业信息列表
     *
     * @param visitReg 企业信息
     * @return 企业信息集合
     */
    @Override
    @DataScopeSnk(userAlias = "c")
    public List<VisitReg> selectVisitRegList(VisitReg visitReg) {
        return visitRegMapper.selectVisitRegList(visitReg);
    }

    /**
     * 新增企业信息
     *
     * @param visitReg 企业信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertVisitReg(VisitReg visitReg) {
        String photo = this.uploadImage(visitReg.getPhoto());
        if (StringUtils.isNotEmpty(photo)) {
            visitReg.setPhoto(photo);
        }
        String idPhoto = this.uploadImage(visitReg.getIdPhoto());
        if (StringUtils.isNotEmpty(idPhoto)) {
            visitReg.setIdPhoto(idPhoto);
        }
        int result = visitRegMapper.insertVisitReg(visitReg);
        this.uploadAuth(visitReg, "0");
        return result;
    }

    @Transactional
    public void uploadAuth(VisitReg visitReg, String delFlag) {
        VisitAuth visitAuth = visitAuthService.selectVisitAuthById(visitReg.getAuthId());
        TimeSlot timeSlot = timeSlotService.selectTimeSlotById(visitAuth.getSlotId());
        List list = JSON.parseObject(visitAuth.getDeviceJson(), ArrayList.class);
        List<Device> deviceList = new ArrayList<>();
        list.stream().forEach(item -> deviceList.add(JSON.parseObject(item.toString(), Device.class)));
        List<Auth> authList = new ArrayList<>();
        deviceList.stream().forEach(item -> {
            Auth auth = new Auth();
            auth.setUserId(visitReg.getId());
            if ("1".equals(visitReg.getProofType())) {
                auth.setCardNo(visitReg.getProofValue());
            }
            auth.setUserNo(visitReg.getId() + "");
            auth.setPhoto(StringUtils.isEmpty(visitReg.getIdPhoto()) ? visitReg.getPhoto() : visitReg.getIdPhoto());
            auth.setUserName(visitReg.getName());
            auth.setSlotId(visitAuth.getSlotId());
            auth.setSlotIdx(timeSlot.getIdx());
            auth.setOpenNum(visitAuth.getOpenNum());
            auth.setExpTime(DateUtils.getTimeByLocalDateTime(LocalDateTime.now().plusMinutes((long) visitAuth.getExpTime())));
            auth.setCardStatus((byte) 0);   // 正常卡
            auth.setStatus("0");
            auth.setHoliday("0");   // 禁止节假日通行：否
            auth.setSpecial("0");   // 普通开门卡
            auth.setCreateBy(visitReg.getCreateBy());
            auth.setDelFlag(delFlag);
            auth.setSn(item.getSn());
            auth.setDeviceId(item.getId());
            auth.setDeviceName(item.getName());
            auth.setControlPort(item.getControlPort());
            auth.setPositionName(item.getPositionName());
            auth.setAdmin("2");
            authList.add(auth);
        });
        if (!CollectionUtils.isEmpty(authList)) {
            deviceAuthService.uploadAuth(authList);
        }
    }

    /**
     * 上传照片
     * @param base64
     * @return
     */
    private String uploadImage(String base64) {
        try{
            return FileUploadUtils.uploadBase64(Global.getVisitPath(), base64);
        } catch (IOException e) {
            throw new BusinessException("上传照片出错！");
        }
    }

    /**
     * 修改企业信息
     *
     * @param visitReg 企业信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateVisitReg(VisitReg visitReg) {
        return visitRegMapper.updateVisitReg(visitReg);
    }

    /**
     * 注销
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    @Transactional
    public void cancel(Long id) {
        VisitReg visitReg = this.selectVisitRegById(id);
        visitReg.setUpdateBy((String) PermissionUtils.getPrincipalProperty("loginName"));
        visitReg.setCancel("1");
        this.updateVisitReg(visitReg);
        this.uploadAuth(visitReg, "1");
    }

    /**
     * 删除企业信息
     *
     * @param id 企业信息ID
     * @return 结果
     */
    @Override
    public int deleteVisitRegById(Long id) {
        return visitRegMapper.deleteVisitRegById(id);
    }

    /**
     * 批量删除企业信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteVisitRegByIds(String ids) {
        return visitRegMapper.deleteVisitRegByIds(Convert.toStrArray(ids));
    }

    /**
     * 校验唯一
     *
     * @param visitReg 登记信息
     * @return
     */
    @Override
    @DataScopeSnk(userAlias = "c")
    public String checkUnique(VisitReg visitReg)
    {
        Long visitRegId = StringUtils.isNull(visitReg.getId()) ? -1L : visitReg.getId();
        visitReg.setCancel("0");
        List<VisitReg> visitRegList = visitRegMapper.selectVisitRegList(visitReg);
        if (!CollectionUtils.isEmpty(visitRegList) && visitRegList.get(0).getId().longValue() != visitRegId.longValue())
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }
}
