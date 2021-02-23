package com.snk.framework.aspectj;

import com.snk.common.annotation.DataScopeSnk;
import com.snk.common.core.domain.BaseEntity;
import com.snk.common.utils.StringUtils;
import com.snk.framework.util.ShiroUtils;
import com.snk.system.domain.SysRole;
import com.snk.system.domain.SysUser;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据过滤处理
 * 
 * @author snk
 */
@Aspect
@Component
public class DataScopeSnkAspect
{

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE_SNK = "dataScopeSnk";

    // 配置织入点
    @Pointcut("@annotation(com.snk.common.annotation.DataScopeSnk)")
    public void dataScopePointCut()
    {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point)
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        DataScopeSnk controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null)
        {
            return;
        }
        // 获取当前的用户
        SysUser currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
            // 如果是管理员，则不过滤数据
            if (!currentUser.isSnkAdmin())
            {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.userAlias(),
                        controllerDataScope.deviceAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     * 
     * @param joinPoint 切点
     * @param user 用户
     * @param userAlias 人事别名
     * @param deviceAlias 设备别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, SysUser user, String userAlias, String deviceAlias)
    {
        StringBuilder sqlString = new StringBuilder();
        // 人事权限
        if (StringUtils.isNotEmpty(userAlias)) {
            for (SysRole role : user.getRoles())
            {
                String dataScope = role.getDataScope();
                if (DataScopeAspect.DATA_SCOPE_ALL.equals(dataScope))
                {
                    sqlString = new StringBuilder();
                    break;
                }
                else if (DataScopeAspect.DATA_SCOPE_CUSTOM.equals(dataScope))
                {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_role_dept WHERE role_id = {} ) ", userAlias,
                            role.getRoleId()));
                }
                else if (DataScopeAspect.DATA_SCOPE_DEPT.equals(dataScope))
                {
                    sqlString.append(StringUtils.format(" OR {}.dept_id = {} ", userAlias, user.getDeptId()));
                }
                else if (DataScopeAspect.DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope))
                {
                    sqlString.append(StringUtils.format(
                            " OR {}.dept_id IN ( SELECT dept_id FROM sys_dept WHERE dept_id = {} or find_in_set( {} , ancestors ) )",
                            userAlias, user.getDeptId(), user.getDeptId()));
                }

            }
        }
        // 设备权限
        if (StringUtils.isNotEmpty(deviceAlias)) {
            sqlString.append(StringUtils.format(
                    " AND {}.id IN ( SELECT device_id FROM snk_device_manager WHERE del_flag = '0' and user_id = {} ) ", deviceAlias,
                    user.getUserId()));
        }

        if (StringUtils.isNotBlank(sqlString.toString()))
        {
            BaseEntity baseEntity = (BaseEntity) joinPoint.getArgs()[0];
            baseEntity.getParams().put(DATA_SCOPE_SNK, " AND " + sqlString.substring(4));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScopeSnk getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DataScopeSnk.class);
        }
        return null;
    }
}
