package com.snk.door.service.impl;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.constant.SymbolConstants;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.domain.Ztree;
import com.snk.common.utils.DateUtils;
import com.snk.common.utils.StringUtils;
import com.snk.door.domain.Auth;
import com.snk.door.domain.Device;
import com.snk.door.domain.Position;
import com.snk.door.mapper.DevicePositionMapper;
import com.snk.door.service.IDevicePositionService;
import com.snk.door.service.IDeviceService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 位置管理 服务实现
 * 
 * @author snk
 */
@Service
public class DevicePositionServiceImpl implements IDevicePositionService
{
    @Autowired
    private DevicePositionMapper devicePositionMapper;

    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备位置管理数据
     *
     * @param position 位置信息
     * @param isDefault 为空是否需要默认数据
     * @return 位置信息集合
     */
    @Override
    @DataScopeSnk(userAlias = "a")
    public List<Position> selectPositionList(Position position, Boolean isDefault)
    {
        List<Position> positionList = devicePositionMapper.selectPositionList(position);
        List<Position> allPosition = devicePositionMapper.selectAllPosition();
        List<Position> result = new ArrayList<>();
        for (Position item : positionList) {
            for (Position ap : allPosition) {
                if (ap.getId().equals(item.getId())) {
                    result.add(item);
                    String[] positionIds = item.getAncestors().split(SymbolConstants.COMMA);
                    for (String positionId : positionIds) {
                        if (!CollectionUtils.isEmpty(result.stream().filter(t -> t.getId().equals(Long.valueOf(positionId))).collect(Collectors.toList()))) {
                            continue;
                        }
                        result.addAll(allPosition.stream().filter(t -> t.getId().equals(Long.valueOf(positionId))).collect(Collectors.toList()));
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(result) && isDefault) {
            result.add(devicePositionMapper.selectPositionById(1l));
        }
        return result.stream().sorted(Comparator.comparing(Position::getId)).collect(Collectors.toList());
    }

    /**
     * 查询位置管理树
     *
     * @param position 位置信息
     * @return 所有位置信息
     */
    @Override
    @DataScopeSnk(userAlias = "a")
    public List<Ztree> selectPositionTree(Position position)
    {
        List<Position> positionList = this.selectPositionList(position, false);
        List<Ztree> ztrees = initZtree(positionList);
        return ztrees;
    }

    /**
     * 查询位置管理树（排除下级）
     *
     * @param position 位置信息
     * @return 所有位置信息
     */
    @Override
    public List<Ztree> selectPositionTreeExcludeChild(Position position)
    {
        Long id = position.getId();
        List<Position> positionList = devicePositionMapper.selectPositionList(position);
        Iterator<Position> it = positionList.iterator();
        while (it.hasNext())
        {
            Position d = it.next();
            if (d.getId().intValue() == id
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), id + ""))
            {
                it.remove();
            }
        }
        List<Ztree> ztrees = initZtree(positionList);
        return ztrees;
    }

    /**
     * 对象转位置树
     *
     * @param positionList 位置列表
     * @return 树结构列表
     */
    private List<Ztree> initZtree(List<Position> positionList)
    {

        List<Ztree> ztrees = new ArrayList<>();
        for (Position position : positionList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(position.getId());
            ztree.setpId(position.getParentId());
            ztree.setName(position.getName());
            ztree.setTitle(position.getName());
            ztrees.add(ztree);
        }
        return ztrees;
    }

    /**
     * 查询位置人数
     *
     * @param parentId 位置ID
     * @return 结果
     */
    @Override
    public int selectPositionCount(Long parentId)
    {
        Position position = new Position();
        position.setParentId(parentId);
        return devicePositionMapper.selectPositionCount(position);
    }

    /**
     * 查询位置是否存在用户
     *
     * @param id 位置ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkPositionExistDevice(Long id)
    {
        int result = devicePositionMapper.checkPositionExistDevice(id);
        return result > 0 ? true : false;
    }

    /**
     * 删除位置管理信息
     *
     * @param id 位置ID
     * @return 结果
     */
    @Override
    public int deletePositionById(Long id)
    {
        return devicePositionMapper.deletePositionById(id);
    }

    /**
     * 新增保存位置信息
     *
     * @param position 位置信息
     * @return 结果
     */
    @Override
    public int insertPosition(Position position)
    {
        Position info = devicePositionMapper.selectPositionById(position.getParentId());
        position.setAncestors(info.getAncestors() + "," + position.getParentId());
        position.setCreateTime(DateUtils.getNowDate());
        return devicePositionMapper.insertPosition(position);
    }

    /**
     * 修改保存位置信息
     *
     * @param position 位置信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePosition(Position position)
    {
        Position newParentPosition = devicePositionMapper.selectPositionById(position.getParentId());
        Position oldPosition = selectPositionById(position.getId());
        if (StringUtils.isNotNull(newParentPosition) && StringUtils.isNotNull(oldPosition))
        {
            String newAncestors = newParentPosition.getAncestors() + "," + newParentPosition.getId();
            String oldAncestors = oldPosition.getAncestors();
            position.setAncestors(newAncestors);
            updatePositionChildren(position.getId(), newAncestors, oldAncestors);
        }
        int result = devicePositionMapper.updatePosition(position);
        return result;
    }

    /**
     * 修改子元素关系
     *
     * @param deptId 被修改的位置ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updatePositionChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<Position> children = devicePositionMapper.selectChildrenPositionById(deptId);
        for (Position child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            devicePositionMapper.updatePositionChildren(children);
        }
    }

    /**
     * 根据位置ID查询信息
     *
     * @param id 位置ID
     * @return 位置信息
     */
    @Override
    public Position selectPositionById(Long id)
    {
        return devicePositionMapper.selectPositionById(id);
    }

    /**
     * 校验位置名称是否唯一
     *
     * @param position 位置信息
     * @return 结果
     */
    @Override
    public String checkPositionNameUnique(Position position)
    {
        Long id = StringUtils.isNull(position.getId()) ? -1L : position.getId();
        Position info = devicePositionMapper.checkPositionNameUnique(position.getName(), position.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return UserConstants.POSITION_NAME_NOT_UNIQUE;
        }
        return UserConstants.POSITION_NAME_UNIQUE;
    }
}
