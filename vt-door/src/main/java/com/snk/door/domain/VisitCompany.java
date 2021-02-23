package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 企业信息对象 snk_visit_company
 * 
 * @author snk
 * @date 2020-11-18
 */
@Data
public class VisitCompany extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 企业名称 */
    private String name;

    /** 负责人 */
    private String leader;

    /** 负责人手机号 */
    private String leaderPhone;

    /** 详细地址 */
    private String address;

    /** 删除标志 */
    private String delFlag;

}
