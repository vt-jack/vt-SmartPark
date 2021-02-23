package com.snk.door.domain;

import java.util.Date;
import com.snk.common.annotation.Excel;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 黑名单对象 snk_user_blacklist
 * 
 * @author snk
 * @date 2020-08-06
 */
@Data
public class Blacklist extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 数据源ID */
    private Long refId;

    /** 数据源ID集合 */
    private String[] refIds;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 证件号 */
    @Excel(name = "证件号")
    private String idNo;

    /** 拉黑日期 */
    @Excel(name = "拉黑日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 解禁日期 */
    @Excel(name = "解禁日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date liftTime;

    /** 删除标志 */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("refId", getRefId())
            .append("liftTime", getLiftTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
