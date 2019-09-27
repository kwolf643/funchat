package com.shixun.funchat.dao;

import com.shixun.funchat.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    /**
     * 查找群聊内所有成员
     *
     * @param grop_id 群聊ID
     * @return 成员数组
     */
    List<User> selectUserByGroupId(Integer grop_id);

    /**
     * 选择性查找
     *
     * @param user 要查找的用户信息
     * @return 找到的用户信息数组
     */
    List<User> selectBySelective(User user);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}