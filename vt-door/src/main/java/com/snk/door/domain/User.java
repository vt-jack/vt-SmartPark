package com.snk.door.domain;

import com.snk.common.annotation.Excel;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 人员信息对象 snk_user
 * 
 * @author snk
 * @date 2020-08-03
 */
@Data
public class User extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @Excel(name = "用户号", type = Excel.Type.EXPORT)
    private Long id;

    /** 姓名 */
    @Excel(name = "姓名*")
    private String name;

    /** 用户号 */
    @Excel(name = "人员编号")
    private String userNo;

    /** 部门ID */
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门", combo = "dept", width = 30)
    private String deptName;

    /** 手机 */
    @Excel(name = "手机号*")
    private String phone;

    /** 卡号 */
    @Excel(name = "卡号")
    private String cardNo;

    /** 人像 */
    private String faceNum;

    /** 指纹 */
    private String fingerNum;

    /** 证件类型 */
    @Excel(name = "证件类型", dictType = "snk_id_type")
    private String idType;

    /** 证件号 */
    @Excel(name = "证件号")
    private String idNo;

    /** 岗位ID */
    private Long postId;

    /** 岗位名称 */
    @Excel(name = "岗位", combo = "post")
    private String postName;

    /** 性别 */
    @Excel(name = "性别", dictType = "sys_user_sex")
    private String sex;

    /** 民族 */
    @Excel(name = "民族", dictType = "snk_nation")
    private String nation;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 黑名单 */
    @Excel(name = "黑名单", dictType = "snk_yes_no", type = Excel.Type.EXPORT)
    private String blacklist;

    /** 照片 */
    private String photo;

    /** 管理员 */
    private String admin;

    /** 删除标志 */
    private String delFlag;

    /** 图片base64 */
    private String base64;

    /** 通行凭证 */
    private List<Proof> proofList;

}
