package com.snk.system.utils;

import com.snk.common.constant.SymbolConstants;
import com.snk.system.domain.SysDept;
import com.snk.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DeptUtils {

    @Autowired
    private ISysDeptService service;
    private static ISysDeptService sysDeptService;

    @PostConstruct
    public void init() {
        sysDeptService = service;
    }

    public static String selectDeptList() {
        List<SysDept> deptList = sysDeptService.selectDeptList(new SysDept());
        StringBuffer sb = new StringBuffer();
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
            String[] ancestors = item.getAncestors().split(SymbolConstants.COMMA);
            sb.append(SymbolConstants.COMMA);
            for (String deptId: ancestors) {
                if ("0".equals(deptId)) {
                    continue;
                }
                sb.append(deptMap.get(Long.valueOf(deptId))).append(SymbolConstants.VERTICAL_LINE);
            }
            sb.append(item.getDeptName());
        }
        return sb.substring(1);
    }

}
