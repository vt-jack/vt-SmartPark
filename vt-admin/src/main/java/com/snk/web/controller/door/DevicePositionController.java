package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.constant.UserConstants;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.domain.Ztree;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.StringUtils;
import com.snk.door.domain.Position;
import com.snk.door.service.IDevicePositionService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 位置信息
 * 
 * @author snk
 */
@Controller
@RequestMapping("/door/device/position")
public class DevicePositionController extends BaseController
{
    private String prefix = "door/device/position";

    @Autowired
    private IDevicePositionService devicePositionService;

    @RequiresPermissions("door:position:view")
    @GetMapping()
    public String position()
    {
        return prefix + "/position";
    }

    @RequiresPermissions("door:position:list")
    @PostMapping("/list")
    @ResponseBody
    public List<Position> list(Position position)
    {
        List<Position> positionList = devicePositionService.selectPositionList(position, true);
        return positionList;
    }

    /**
     * 新增位置
     */
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap)
    {
        mmap.put("position", devicePositionService.selectPositionById(parentId));
        return prefix + "/add";
    }

    /**
     * 新增保存位置
     */
    @Log(title = "位置管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("door:position:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated Position position)
    {
        if (UserConstants.POSITION_NAME_NOT_UNIQUE.equals(devicePositionService.checkPositionNameUnique(position)))
        {
            return error("新增位置'" + position.getName() + "'失败，位置名称已存在");
        }
        position.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(devicePositionService.insertPosition(position));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Position position = devicePositionService.selectPositionById(id);
        if (StringUtils.isNotNull(position) && 1 == id)
        {
            position.setParentName("无");
        }
        mmap.put("position", position);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "位置管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("door:position:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Position position)
    {
        if (UserConstants.DEPT_NAME_NOT_UNIQUE.equals(devicePositionService.checkPositionNameUnique(position)))
        {
            return error("修改位置'" + position.getName() + "'失败，位置名称已存在");
        }
        else if (position.getParentId().equals(position.getId()))
        {
            return error("修改位置'" + position.getName() + "'失败，上级位置不能是自己");
        }
        position.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(devicePositionService.updatePosition(position));
    }

    /**
     * 删除
     */
    @Log(title = "位置管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("door:position:remove")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Long id)
    {
        if (devicePositionService.selectPositionCount(id) > 0)
        {
            return AjaxResult.warn("存在下级位置,不允许删除");
        }
        if (devicePositionService.checkPositionExistDevice(id))
        {
            return AjaxResult.warn("位置存在设备,不允许删除");
        }
        return toAjax(devicePositionService.deletePositionById(id));
    }

    /**
     * 校验位置名称
     */
    @PostMapping("/checkPositionNameUnique")
    @ResponseBody
    public String checkPositionNameUnique(Position position)
    {
        return devicePositionService.checkPositionNameUnique(position);
    }

    /**
     * 选择位置树
     * 
     * @param id 位置ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = { "/selectPositionTree/{id}", "/selectPositionTree/{id}/{excludeId}" })
    public String selectPositionTree(@PathVariable("id") Long id,
            @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap)
    {
        mmap.put("position", devicePositionService.selectPositionById(id));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }

    /**
     * 加载位置列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = devicePositionService.selectPositionTree(new Position());
        return ztrees;
    }

    /**
     * 加载位置列表树（排除下级）
     */
    @GetMapping("/treeData/{excludeId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "excludeId", required = false) Long excludeId)
    {
        Position position = new Position();
        position.setId(excludeId);
        List<Ztree> ztrees = devicePositionService.selectPositionTreeExcludeChild(position);
        return ztrees;
    }

}
