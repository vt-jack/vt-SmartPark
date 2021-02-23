package com.snk.door.domain;

import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 来访登记对象 snk_visit_reg
 * 
 * @author snk
 * @date 2020-11-18
 */
@Data
public class VisitReg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 拍摄照片 */
    private String photo;

    /** 被访问企业ID */
    private Long companyId;

    /** 被访问企业 */
    private String companyName;

    /** 被访问者 */
    private String companyUser;

    /** 访问权限ID */
    private Long authId;

    /** 身份证照片 */
    private String idPhoto;

    /** 姓名 */
    private String name;

    /** 性别 */
    private String sex;

    /** 手机号 */
    private String phone;

    /** 证件类型 */
    private String idType;

    /** 证件号 */
    private String idNo;

    /** 来访事由 */
    private String visitReason;

    /** 凭证类型 1 卡号  2 二维码 */
    private String proofType;

    /** 凭证 */
    private String proofValue;

    /** 来访时间 */
    private String visitTime;

    /** 是否注销 */
    private String cancel;

    /** 删除标志 */
    private String delFlag;

}
