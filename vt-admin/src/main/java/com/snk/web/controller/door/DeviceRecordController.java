package com.snk.web.controller.door;

import com.snk.common.annotation.Log;
import com.snk.common.core.controller.BaseController;
import com.snk.common.core.domain.AjaxResult;
import com.snk.common.core.page.TableDataInfo;
import com.snk.common.enums.BusinessType;
import com.snk.common.utils.StringUtils;
import com.snk.common.utils.poi.ExcelUtil;
import com.snk.door.api.control.read.ReadTransactionDatabaseService;
import com.snk.door.domain.Device;
import com.snk.door.mongo.entity.CurrentRecord;
import com.snk.door.mongo.service.IMongoCurrentRecord;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备记录Controller
 * 
 * @author snk
 */
@Controller
@RequestMapping("/door/device/record")
public class DeviceRecordController extends BaseController
{
    private String prefix = "door/device/record";

    @Autowired
    private IMongoCurrentRecord mongoCurrentRecord;
    @Autowired
    private ReadTransactionDatabaseService transactionDatabaseService;

    @RequiresPermissions("door:record:view")
    @GetMapping()
    public String position()
    {
        return prefix + "/record";
    }

    @RequiresPermissions("door:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CurrentRecord record)
    {
        startPage();
        if (StringUtils.isNotEmpty(record.getSn())) {
            return mongoCurrentRecord.pageList(record);
        }
        return getDataTable(new ArrayList<>());
    }

    @RequiresPermissions("door:record:collect")
    @Log(title = "设备记录", businessType = BusinessType.INSERT)
    @PostMapping("/collect")
    @ResponseBody
    public AjaxResult collect(@RequestBody Device device)
    {
        transactionDatabaseService.readTransactionDatabase(device);
        return AjaxResult.success();
    }

    /**
     * 导出设备记录
     */
    @RequiresPermissions("door:record:export")
    @Log(title = "设备记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CurrentRecord record)
    {
        List<CurrentRecord> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(record.getSn())) {
            list = mongoCurrentRecord.list(record);
        }
        ExcelUtil<CurrentRecord> util = new ExcelUtil<>(CurrentRecord.class);
        return util.exportExcel(list, "设备记录");
    }

}
