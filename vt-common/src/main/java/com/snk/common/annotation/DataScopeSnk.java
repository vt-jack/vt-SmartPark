package com.snk.common.annotation;

import java.lang.annotation.*;

/**
 * SNK数据权限过滤注解
 * 
 * @author snk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopeSnk
{
    /**
     * 人事信息表的别名
     */
    String userAlias() default "";

    /**
     * 设备信息表的别名
     */
    String deviceAlias() default "";
}
