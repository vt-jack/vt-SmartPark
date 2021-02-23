package com.snk.door.domain;

import com.snk.common.annotation.Excel;
import com.snk.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 凭证对象 snk_user_proof
 * 
 * @author snk
 * @date 2020-08-06
 */
@Data
public class Proof extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 凭证类型 1 人脸  2 IC卡  3 指纹 */
    private String type;

    /** 手指 1-5 左手 小指-拇指 6-10 右手 拇指-小指 */
    private String finger;

    /** 通行凭证 */
    private String value;

    /** 删除标志 */
    private String delFlag;

    public Proof() {

    }

    public Proof(Long userId) {
        this.userId = userId;
    }

    public Proof(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Proof(Long userId, String type, String finger, String value) {
        this.userId = userId;
        this.type = type;
        this.finger = finger;
        this.value = value;
    }
}
