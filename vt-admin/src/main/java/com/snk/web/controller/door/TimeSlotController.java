package com.snk.web.controller.door;

import com.snk.common.constant.Constants;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.exception.base.BaseException;
import com.snk.common.utils.DateUtils;
import com.snk.door.domain.TimeSlot;
import com.snk.door.service.ITimeSlotService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开门时段信息Controller
 * 
 * @author snk
 * @date 2020-08-03
 */
@Controller
@RequestMapping("/door/device/timeslot")
public class TimeSlotController extends BaseController
{

    private String prefix = "door/device/timeslot";

    @Autowired
    private ITimeSlotService timeSlotService;

    @RequiresPermissions("door:timeslot:view")
    @GetMapping()
    public String timeslot(ModelMap mmap)
    {
        mmap.put("times", timeSlotService.selectTimeSlotListByUserId(ShiroUtils.getUserId()));
        return prefix + "/timeslot";
    }

    /**
     * 查询开门时段信息列表
     */
    @PostMapping("/list")
    @ResponseBody
    public List<TimeSlot> list(TimeSlot timeSlot)
    {
        return timeSlotService.selectTimeSlotList(timeSlot);
    }

    /**
     * 新增开门时段信息
     */
    @RequiresPermissions("door:timeslot:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TimeSlot timeSlot)
    {
        timeSlot.setCreateBy(ShiroUtils.getLoginName());
        timeSlot.setCreateTime(DateUtils.getNowDate());
        return toAjax(timeSlotService.insertTimeSlot(timeSlot));
    }

    /**
     * 新增开门时段信息
     */
    @RequiresPermissions("door:timeslot:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TimeSlot timeSlot)
    {
        timeSlot.setUpdateBy(ShiroUtils.getLoginName());
        timeSlot.setUpdateTime(DateUtils.getNowDate());
        TimeSlot ts = timeSlotService.selectTimeSlotByIdx(timeSlot.getSn(), timeSlot.getIdx());
        if (ObjectUtils.isEmpty(ts)) {
            throw new BaseException("数据已被删除，请刷新页面");
        }
        timeSlot.setId(ts.getId());
        return toAjax(timeSlotService.updateTimeSlot(timeSlot));
    }

    /**
     * 删除开门时段信息
     */
    @RequiresPermissions("door:timeslot:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(TimeSlot timeSlot)
    {
        return toAjax(timeSlotService.deleteTimeSlotByIdx(timeSlot.getSn(), timeSlot.getIdx()));
    }

    /**
     * 查询开门时段信息
     */
    @GetMapping("/{sn}/{idx}")
    @ResponseBody
    public TimeSlot selectTimeSlotByIdx(@PathVariable("sn") String sn, @PathVariable("idx") Integer idx)
    {
        return timeSlotService.selectTimeSlotByIdx(sn, idx);
    }

    /**
     * 查询最大开门时段
     */
    @GetMapping("/maxIdx/{sn}")
    @ResponseBody
    public int selectMaxIdx(@PathVariable("sn") String sn)
    {
        return timeSlotService.selectMaxIdx(sn);
    }

    /**
     * 校验唯一
     */
    @PostMapping("/checkUnique")
    @ResponseBody
    public String checkUnique(TimeSlot timeSlot)
    {
        return timeSlotService.checkUnique(timeSlot);
    }

}
