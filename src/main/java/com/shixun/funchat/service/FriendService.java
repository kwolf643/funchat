package com.shixun.funchat.service;


import com.shixun.funchat.entity.Friend;
import com.shixun.funchat.entity.User;

import java.util.List;

public interface FriendService {
    /**
     * 获取用户已接受好友
     *
     * @param userId 用户ID
     * @return 已接受好友数组
     */
    List<User> getUserFriendsAccept(Integer userId);

    /**
     * 获取用户未接受好友
     *
     * @param userId 用户ID
     * @return 未接受好友数组
     */
    List<User> getUserFriendsNotAccept(Integer userId);

    /**
     * 获取用户收到的好友请求
     *
     * @param userId 用户ID
     * @return 好友请求用户数组
     */
    List<User> getUserWantFriends(Integer userId);

    /**
     * 添加好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     * @return null 失败 其他 好友信息
     */
    User addFriend(Integer userId, Integer friendId);

    /**
     * 删除好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     * @return true 成功 false 失败
     */
    boolean deleteFriend(Integer userId, Integer friendId);

    /**
     * 是否有好友记录（好友或好友申请）
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return null 无记录 其他 找到的好友记录
     */
    Friend findFriendRecord(Integer userId1,Integer userId2);

    /**
     * 判断是否是好友
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return true 是好友 false 不是好友
     */
    boolean isFriend(Integer userId1,Integer userId2);

    /**
     * 判断是否是发送好友申请
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return true 是好友 false 不是好友
     */
    boolean isWantFriend(Integer userId1,Integer userId2);
}
