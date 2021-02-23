package com.snk.web.controller.door;

import com.github.pagehelper.PageHelper;
import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.door.domain.CwaRule;
import com.snk.door.domain.CwaUser;
import com.snk.door.domain.User;
import com.snk.door.service.ICwaRuleService;
import com.snk.door.service.ICwaUserService;
import com.snk.door.service.IUserService;
import com.snk.framework.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考勤人员Controller
 * 
 * @author snk
 * @date 2020-10-28
 */
@Controller
@RequestMapping("/door/cwa/user")
public class CwaUserController extends BaseController
{
    private String prefix = "door/cwa/user";

    @Autowired
    private ICwaUserService cwaUserService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICwaRuleService cwaRuleService;

    @RequiresPermissions("door:cwaUser:view")
    @GetMapping()
    public String user(ModelMap mmap)
    {
        mmap.put("rules", cwaRuleService.selectCwaRuleList(new CwaRule()));
        return prefix + "/user";
    }

    /**
     * 查询设备信息
     */
    @RequiresPermissions("door:cwaUser:list")
    @PostMapping("/userList")
    @ResponseBody
    public TableDataInfo userList(User user)
    {
        PageHelper.clearPage();
        List<User> list = userService.selectUserList(user);
        List<Long> userIdList = cwaUserService.selectCwaUserList(new CwaUser()).stream().map(CwaUser::getUserId).collect(Collectors.toList());
        List<User> filterList = list.stream().filter(item -> !userIdList.contains(item.getId())).collect(Collectors.toList());
        startPage();
        return getDataTableCustom(filterList);
    }

    /**
     * 查询人员信息列表
     */
    @GetMapping("/autoList")
    @ResponseBody
    public AjaxResult autoList()
    {
        AjaxResult ajax = new AjaxResult();
        ajax.put("code", 200);
        ajax.put("value", cwaUserService.selectUserAutoList());
        return ajax;
    }

    /**
     * 查询考勤规则列表
     */
    @RequiresPermissions("door:cwaUser:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CwaUser CwaUser)
    {
        startPage();
        List<CwaUser> list = cwaUserService.selectCwaUserList(CwaUser);
        return getDataTable(list);
    }

    /**
     * 根据人员ID查规则
     * @param userId
     * @return
     */
    @PostMapping("/{userId}")
    @ResponseBody
    public CwaUser cwaUser(@PathVariable("userId") Long userId)
    {
        return cwaUserService.selectCwaUserByUserId(userId);
    }

    /**
     * 新增保存考勤人员
     */
    @RequiresPermissions("door:cwaUser:add")
    @Log(title = "考勤人员", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody List<CwaUser> cwaUserList)
    {
        cwaUserList.stream().forEach(cwaUser -> {
            cwaUser.setCreateBy(ShiroUtils.getLoginName());
            cwaUserService.insertCwaUser(cwaUser);
        });
        return AjaxResult.success();
    }

    /**
     * 删除考勤人员
     */
    @RequiresPermissions("door:cwaUser:remove")
    @Log(title = "考勤人员", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cwaUserService.deleteCwaUserByIds(ids));
    }

}
