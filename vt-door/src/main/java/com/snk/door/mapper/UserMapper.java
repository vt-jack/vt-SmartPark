package com.snk.door.mapper;

import com.snk.door.domain.AutoUser;
import com.snk.door.domain.User;

import java.util.List;

/**
 * 人员信息Mapper接口
 * 
 * @author snk
 * @date 2020-08-03
 */
public interface UserMapper 
{
    /**
     * 查询人员信息
     * 
     * @param id 人员信息ID
     * @return 人员信息
     */
    User selectUserById(Long id);

    /**
     * 查询人员信息
     *
     * @param cardNo 卡号
     * @return 人员信息
     */
    User selectUserByCardNo(String cardNo);

    /**
     * 查询人员信息列表
     * 
     * @param user 人员信息
     * @return 人员信息集合
     */
    List<User> selectUserList(User user);

    /**
     * 新增人员信息
     * 
     * @param user 人员信息
     * @return 结果
     */
    int insertUser(User user);

    /**
     * 修改人员信息
     * 
     * @param user 人员信息
     * @return 结果
     */
    int updateUser(User user);

    /**
     * 删除人员信息
     * 
     * @param id 人员信息ID
     * @return 结果
     */
    int deleteUserById(Long id);

    /**
     * 批量删除人员信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserByIds(String[] ids);

}
