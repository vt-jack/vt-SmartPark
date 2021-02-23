package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.config.Global;
import com.snk.common.constant.Constants;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.exception.BusinessException;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.file.FileUploadUtils;
import com.snk.common.utils.file.FileUtils;
import com.snk.door.api.control.oper.DeleteCardService;
import com.snk.door.api.face.DeletePersonService;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.Proof;
import com.snk.door.domain.User;
import com.snk.door.enums.DeviceType;
import com.snk.door.enums.ProofType;
import com.snk.door.mapper.UserMapper;
import com.snk.door.service.*;
import com.snk.system.domain.SysDept;
import com.snk.system.domain.SysPost;
import com.snk.system.service.ISysDeptService;
import com.snk.system.service.ISysPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 人员信息Service业务层处理
 * 
 * @author snk
 * @date 2020-08-03
 */
@Service
public class UserServiceImpl implements IUserService
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IProofService proofService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysPostService sysPostService;
    @Autowired
    private IDeviceAuthService deviceAuthService;
    @Autowired
    private DeleteCardService deleteCardService;
    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IBlacklistService blacklistService;
    @Autowired
    private DeletePersonService deletePersonService;

    /**
     * 查询人员信息
     * 
     * @param id 人员信息ID
     * @return 人员信息
     */
    @Override
    public User selectUserById(Long id)
    {
        return userMapper.selectUserById(id);
    }

    /**
     * 查询人员信息
     *
     * @param cardNo 卡号
     * @return 人员信息
     */
    @Override
    public User selectUserByCardNo(String cardNo)
    {
        return userMapper.selectUserByCardNo(cardNo);
    }

    /**
     * 查询人员信息列表
     * 
     * @param user 人员信息
     * @return 人员信息
     */
    @Override
    @DataScopeSnk(userAlias = "a")
    public List<User> selectUserList(User user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 新增人员信息
     * 
     * @param user 人员信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertUser(User user) {
        // 新增人员信息表
        user.setPhoto(this.uploadImage(user.getBase64()));
        user.setCreateTime(DateUtils.getNowDate());
        int rows = userMapper.insertUser(user);

        List<Proof> proofList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(user.getProofList())) {
            user.getProofList().stream().forEach(item -> {
                Proof proof = new Proof(user.getId(), item.getType(), item.getFinger(),
                        ProofType.FACE.getValue().equals(item.getType()) ? this.uploadImage(item.getValue()) : item.getValue());
                proof.setCreateBy(user.getCreateBy());
                proof.setCreateTime(DateUtils.getNowDate());
                proofList.add(proof);
            });
        }
        // 新增凭证
        if (!CollectionUtils.isEmpty(proofList)) {
            proofService.insertProofBatch(proofList);
        }
        return rows;
    }

    /**
     * 修改人员信息
     *
     * @param user 人员信息
     * @return 结果
     */
    @Transactional
    @Override
    public int userUpdate(User user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改人员信息
     *
     * @param user 人员信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateUser(User user)
    {
        // 查询凭证
        List<Proof> proofList = proofService.selectProofList(new Proof(user.getId()));
        List<Proof> insertList = new ArrayList<>();
        StringBuffer delIds = new StringBuffer();
        List<String> deleteFile = new ArrayList<>();

        // 更新凭证
        if (CollectionUtils.isEmpty(user.getProofList())) {
            proofList.stream().forEach(item -> delIds.append(SymbolConstants.COMMA).append(item.getId()));
        } else {
            // 筛选出新增的凭证
            for (Proof pf : user.getProofList()) {
                if (CollectionUtils.isEmpty(proofList.stream().filter(item -> pf.getValue().equals(item.getValue())).collect(Collectors.toList()))) {
                    Proof proof = new Proof(user.getId(), pf.getType(), pf.getFinger(),
                            ProofType.FACE.getValue().equals(pf.getType()) ? this.uploadImage(pf.getValue()) : pf.getValue());
                    proof.setCreateBy(user.getUpdateBy());
                    proof.setCreateTime(DateUtils.getNowDate());
                    insertList.add(proof);
                }
            }
            // 筛选出删除的凭证数据
            for (Proof proof : proofList) {
                if (CollectionUtils.isEmpty(user.getProofList().stream().filter(item -> item.getValue().equals(proof.getValue())).collect(Collectors.toList()))) {
                    if (ProofType.FACE.getValue().equals(proof.getType())) {
                        deleteFile.add(Global.getProfile() + StringUtils.substringAfter(proof.getValue(), Constants.RESOURCE_PREFIX));
                    }
                    delIds.append(SymbolConstants.COMMA).append(proof.getId());
                }
            }
        }

        // 新增凭证
        if (!CollectionUtils.isEmpty(insertList)) {
            proofService.insertProofBatch(insertList);
        }
        // 删除凭证
        if (StringUtils.isNotEmpty(delIds)) {
            proofService.deleteProofByIds(delIds.substring(1));
        }
        String photo = this.uploadImage(user.getBase64());
        if (StringUtils.isNotEmpty(photo)) {
            user.setPhoto(photo);  // 上传照片
        }
        User oldUser = userMapper.selectUserById(user.getId());
        if (StringUtils.isNotEmpty(oldUser.getPhoto()) && !oldUser.getPhoto().equals(user.getPhoto())) {
            deleteFile.add(Global.getProfile() + StringUtils.substringAfter(oldUser.getPhoto(), Constants.RESOURCE_PREFIX));
        }
        // 删除本地图片
        if (!CollectionUtils.isEmpty(deleteFile)) {
            deleteFile.stream().forEach(filePath -> FileUtils.deleteFile(filePath));
        }
        return userMapper.updateUser(user);
    }

    /**
     * 上传照片
     * @param base64
     * @return
     */
    private String uploadImage(String base64) {
        try{
            return FileUploadUtils.uploadBase64(Global.getUserPath(), base64);
        } catch (IOException e) {
            throw new BusinessException("上传照片出错！");
        }
    }

    /**
     * 删除人员信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteUserByIds(String ids)
    {
        List<String> deleteFile = new ArrayList<>();    // 需要删除保存在磁盘的图片
        String[] array = Convert.toStrArray(ids);
        for (String id : array) {   // 删除权限数据
            List<Auth> authList = deviceAuthService.selectAuthByUserId(Long.valueOf(id));
            Map<String, List<Auth>> map = authList.stream().collect(Collectors.groupingBy(item -> item.getSn()));
            for (String sn : map.keySet()) {
                List<Device> deviceList = deviceService.selectDeviceBySn(sn);
                if (!CollectionUtils.isEmpty(deviceList)) {
                    Device device = deviceList.get(0);
                    device.setParam(map.get(sn));
                    if (DeviceType.CONTROL.getValue().equals(device.getDeviceType())) {
                        deleteCardService.deleteCard(device);    // 删除卡片
                    } else {
                        deletePersonService.deletePerson(device, false);
                    }
                    deviceAuthService.deleteAuthByUserId(Long.valueOf(id));    // 删除权限数据
                }
            }
            List<Proof> proofList = proofService.selectProofByUserId(Long.valueOf(id));
            proofList.stream().forEach(item -> {
                if (ProofType.FACE.getValue().equals(item.getType())) {
                    deleteFile.add(Global.getProfile() + StringUtils.substringAfter(item.getValue(), Constants.RESOURCE_PREFIX));
                }
            });
            User user = userMapper.selectUserById(Long.valueOf(id));
            if (StringUtils.isNotEmpty(user.getPhoto())) {
                deleteFile.add(Global.getProfile() + StringUtils.substringAfter(user.getPhoto(), Constants.RESOURCE_PREFIX));
            }
        }
        proofService.deleteProofByUserIds(ids); // 删除凭证
        blacklistService.deleteBlacklistByUserIds(ids); // 删除黑名单
        int result = userMapper.deleteUserByIds(array);
        if (!CollectionUtils.isEmpty(deleteFile)) {
            deleteFile.stream().forEach(filePath -> FileUtils.deleteFile(filePath));
        }
        return result;
    }

    /**
     * 删除人员信息信息
     *
     * @param id 人员信息ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long id)
    {
        return userMapper.deleteUserById(id);
    }

    /**
     * 校验唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    @DataScopeSnk(userAlias = "a")
    public String checkUnique(User user)
    {
        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
        List<User> userList = userMapper.selectUserList(user);
        if (!CollectionUtils.isEmpty(userList) && userList.get(0).getId().longValue() != userId.longValue())
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }

    @Override
    public String importUser(List<User> userList, String loginName)
    {
        if (CollectionUtils.isEmpty(userList))
        {
            throw new BusinessException("导入人员信息不能为空！");
        }
        Map<String, Long> deptMap = this.selectDeptMap();
        Map<String, Long> postMap = sysPostService.selectPostAll().stream().collect(Collectors.toMap(SysPost::getPostName, SysPost::getPostId));
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (User user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                User phoneParam = new User();
                phoneParam.setPhone(user.getPhone());
                User idNoParam = new User();
                idNoParam.setIdNo(user.getIdNo());
                User cardNoParam = new User();
                cardNoParam.setCardNo(user.getCardNo());
                User userNoParam = new User();
                userNoParam.setUserNo(user.getUserNo());
                if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getPhone())
                    || StringUtils.isEmpty(user.getDeptName()) || StringUtils.isEmpty(user.getIdNo())
                        || ObjectUtils.isEmpty(user.getUserNo())){
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 请补充必填项");
                }
                else if (!Pattern.compile(Constants.REG_PHONE).matcher(user.getPhone()).matches()) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 手机号不正确");
                }
                else if (ObjectUtils.isEmpty(deptMap.get(user.getDeptName()))) {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 你没有此部门的数据权限");
                }
                else if (UserConstants.EXCEPTION.equals(this.checkUnique(phoneParam)))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 手机号已存在");
                }
                else if (UserConstants.EXCEPTION.equals(this.checkUnique(idNoParam)))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 证件号已存在");
                }
                else if (UserConstants.EXCEPTION.equals(this.checkUnique(cardNoParam)))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 卡号已存在");
                }
                else if (UserConstants.EXCEPTION.equals(this.checkUnique(userNoParam)))
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " + user.getName() + " 人员编号已存在");
                }
                else
                {
                    user.setDeptId(deptMap.get(user.getDeptName()));
                    user.setPostId(postMap.get(user.getPostName()));
                    user.setCreateBy(loginName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、姓名 " + user.getName() + " 导入成功");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、姓名 " + user.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    private Map<String, Long> selectDeptMap() {
        Map<String, Long> result = new HashMap<>();
        List<SysDept> deptList = sysDeptService.selectDeptList(new SysDept());
        List<Long> parentIds = deptList.stream().map(SysDept::getParentId).collect(Collectors.toList());
        Map<Long, String> deptMap = deptList.stream().collect(Collectors.toMap(SysDept::getDeptId, SysDept::getDeptName));
        for (SysDept item : deptList) {
            boolean isParent = false;
            for (Long parentId : parentIds) {
                if (parentId == item.getDeptId()) {
                    isParent = true;
                    break;
                }
            }
            if (isParent) {
                continue;
            }
            StringBuffer sb = new StringBuffer();
            String[] ancestors = item.getAncestors().split(SymbolConstants.COMMA);
            for (String deptId: ancestors) {
                if ("0".equals(deptId)) {
                    continue;
                }
                sb.append(deptMap.get(Long.valueOf(deptId))).append(SymbolConstants.VERTICAL_LINE);
            }
            sb.append(item.getDeptName());
            result.put(sb.toString(), item.getDeptId());
        }
        return result;
    }
}
