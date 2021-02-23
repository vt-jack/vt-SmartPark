package com.snk.door.service.impl;

import com.snk.common.config.Global;
import com.snk.common.constant.Constants;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.text.Convert;
import com.snk.common.exception.BusinessException;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.file.FileUploadUtils;
import com.snk.common.utils.file.FileUtils;
import com.snk.door.domain.Model;
import com.snk.door.enums.DeviceType;
import com.snk.door.mapper.ModelMapper;
import com.snk.door.service.IModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备型号信息Service业务层处理
 * 
 * @author snk
 * @date 2020-08-03
 */
@Service
public class ModelServiceImpl implements IModelService
{
    private static final Logger log = LoggerFactory.getLogger(ModelServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 查询设备型号信息
     * 
     * @param id 设备型号信息ID
     * @return 设备型号信息
     */
    @Override
    public Model selectModelById(Long id)
    {
        return modelMapper.selectModelById(id);
    }

    /**
     * 查询设备型号信息
     *
     * @param model 设备型号
     * @return 设备型号信息
     */
    @Override
    public Model selectModelByModel(String model)
    {
        return modelMapper.selectModelByModel(model);
    }

    /**
     * 查询设备型号信息列表
     * 
     * @param model 设备型号信息
     * @return 设备型号信息
     */
    @Override
    public List<Model> selectModelList(Model model)
    {
        return modelMapper.selectModelList(model);
    }

    /**
     * 新增设备型号信息
     * 
     * @param model 设备型号信息
     * @return 结果
     */
    @Transactional
    @Override
    public int insertModel(Model model) {
        // 新增设备型号信息表
        model.setCreateTime(DateUtils.getNowDate());
        this.setModel(model);
        return modelMapper.insertModel(model);
    }

    /**
     * 修改设备型号信息
     *
     * @param model 设备型号信息
     * @return 结果
     */
    @Transactional
    @Override
    public int updateModel(Model model)
    {
        // 修改设备型号信息表
        model.setUpdateTime(DateUtils.getNowDate());
        this.setModel(model);
        Model oldModel = modelMapper.selectModelById(model.getId());
        List<String> deleteFile = new ArrayList<>();
        if (StringUtils.isNotEmpty(oldModel.getImage())
                && !oldModel.getImage().equals(model.getImage())) {
            deleteFile.add(Global.getProfile() + StringUtils.substringAfter(oldModel.getImage(), Constants.RESOURCE_PREFIX));
        }
        if (StringUtils.isNotEmpty(oldModel.getConScheme())
                && !oldModel.getConScheme().equals(model.getConScheme())) {
            deleteFile.add(Global.getProfile() + StringUtils.substringAfter(oldModel.getConScheme(), Constants.RESOURCE_PREFIX));
        }
        // 删除本地图片
        if (!CollectionUtils.isEmpty(deleteFile)) {
            deleteFile.stream().forEach(filePath -> FileUtils.deleteFile(filePath));
        }
        return modelMapper.updateModel(model);
    }

    private void setModel(Model model) {
        if (!DeviceType.CONTROL.getValue().equals(model.getDeviceType())) {
            model.setType(null);
        }
        String image = this.uploadImage(model.getImage());
        if (StringUtils.isNotEmpty(image)) {
            model.setImage(image);
        }
        String conScheme = this.uploadImage(model.getConScheme());
        if (StringUtils.isNotEmpty(conScheme)) {
            model.setConScheme(conScheme);
        }
    }

    /**
     * 上传照片
     * @param base64
     * @return
     */
    private String uploadImage(String base64) {
        try{
            return FileUploadUtils.uploadBase64(Global.getDoorPath(), base64);
        } catch (IOException e) {
            throw new BusinessException("上传照片出错！");
        }
    }

    /**
     * 删除设备型号信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteModelByIds(String ids)
    {
        List<String> deleteFile = new ArrayList<>();
        String[] array = Convert.toStrArray(ids);
        for (String id : array) {
            Model model = modelMapper.selectModelById(Long.valueOf(id));
            if (StringUtils.isNotEmpty(model.getImage())) {
                deleteFile.add(Global.getProfile() + StringUtils.substringAfter(model.getImage(), Constants.RESOURCE_PREFIX));
            }
            if (StringUtils.isNotEmpty(model.getConScheme())) {
                deleteFile.add(Global.getProfile() + StringUtils.substringAfter(model.getConScheme(), Constants.RESOURCE_PREFIX));
            }
        }
        int result = modelMapper.deleteModelByIds(array);
        if (!CollectionUtils.isEmpty(deleteFile)) {
            deleteFile.stream().forEach(filePath -> FileUtils.deleteFile(filePath));
        }
        return result;
    }

    /**
     * 删除设备型号信息信息
     *
     * @param id 设备型号信息ID
     * @return 结果
     */
    @Override
    public int deleteModelById(Long id)
    {
        return modelMapper.deleteModelById(id);
    }

    /**
     * 校验唯一
     *
     * @param model 设备型号信息
     * @return
     */
    @Override
    public String checkUnique(Model model)
    {
        Long modelId = StringUtils.isNull(model.getId()) ? -1L : model.getId();
        List<Model> modelList = modelMapper.selectModelList(model);
        if (!CollectionUtils.isEmpty(modelList) && modelList.get(0).getId().longValue() != modelId.longValue())
        {
            return UserConstants.EXCEPTION;
        }
        return UserConstants.NORMAL;
    }

}
