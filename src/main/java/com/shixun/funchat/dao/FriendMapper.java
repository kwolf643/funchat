package com.shixun.funchat.dao;

import com.shixun.funchat.entity.Friend;
import com.shixun.funchat.entity.FriendKey;
import com.shixun.funchat.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendMapper {
    /**
     * 查找用户所有已接受好友
     *
     * @param id 用户ID
     * @return 已接受好友数组
     */
    List<User> selectUserFriendsAccept(Integer id);

    /**
     * 查找用户所有未接受好友
     *
     * @param id 用户ID
     * @return 未接受好友数组
     */
    List<User> selectUserFriendsNotAccept(Integer id);

    /**
     * 查找用户收到的好友请求
     *
     * @param id 用户ID
     * @return 好友请求用户数组
     */
    List<User> selectUserWantFriends(Integer id);

    int deleteByPrimaryKey(FriendKey key);

    int insert(Friend record);

    int insertSelective(Friend record);

    Friend selectByPrimaryKey(FriendKey key);

    int updateByPrimaryKeySelective(Friend record);

    int updateByPrimaryKey(Friend record);
}