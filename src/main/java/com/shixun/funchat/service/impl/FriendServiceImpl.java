package com.shixun.funchat.service.impl;

import com.shixun.funchat.dao.FriendMapper;
import com.shixun.funchat.dao.UserMapper;
import com.shixun.funchat.entity.Friend;
import com.shixun.funchat.entity.FriendKey;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    private static Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户已接受好友
     *
     * @param userId 用户ID
     * @return 已接受好友数组
     */
    @Override
    public List<User> getUserFriendsAccept(Integer userId) {
        return friendMapper.selectUserFriendsAccept(userId);
    }

    /**
     * 获取用户未接受好友
     *
     * @param userId 用户ID
     * @return 未接受好友数组
     */
    @Override
    public List<User> getUserFriendsNotAccept(Integer userId) {
        return friendMapper.selectUserFriendsNotAccept(userId);
    }

    /**
     * 获取用户收到的好友请求
     *
     * @param userId 用户ID
     * @return 好友请求用户数组
     */
    @Override
    public List<User> getUserWantFriends(Integer userId) {
        return friendMapper.selectUserWantFriends(userId);
    }

    /**
     * 添加好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     * @return true 成功 false 失败
     */
    @Override
    public User addFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null)
            return null;
        if (findFriendRecord(userId, friendId) != null) //已有好友记录
            return null;
        Friend friend = new Friend();
        friend.setFriendaId(userId);
        friend.setFriendbId(friendId);
        friend.setAccept(false);
        friendMapper.insert(friend);
        return userMapper.selectByPrimaryKey(friendId);
    }

    /**
     * 删除好友
     *
     * @param userId   用户ID
     * @param friendId 好友ID
     * @return true 成功 false 失败
     */
    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
        if (userId == null || friendId == null)
            return false;
        if (!isFriend(userId, friendId)) //不是好友
            return false;
        FriendKey friendKey = new FriendKey();
        friendKey.setFriendaId(userId);
        friendKey.setFriendbId(friendId);
        friendMapper.deleteByPrimaryKey(friendKey);
        friendKey.setFriendaId(userId);
        friendKey.setFriendbId(friendId);
        friendMapper.deleteByPrimaryKey(friendKey);
        return true;
    }

    /**
     * 是否有好友记录（好友或好友申请）
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return null 无记录 其他 找到的好友记录
     */
    @Override
    public Friend findFriendRecord(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null)
            return null;
        FriendKey friendKey = new FriendKey();
        friendKey.setFriendaId(userId1);
        friendKey.setFriendbId(userId2);
        Friend friend = friendMapper.selectByPrimaryKey(friendKey);
        if (friend != null)
            return friend;
        friendKey.setFriendaId(userId2);
        friendKey.setFriendbId(userId1);
        friend = friendMapper.selectByPrimaryKey(friendKey);
        return friend;
    }

    /**
     * 判断是否是好友
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return true 是好友 false 不是好友
     */
    @Override
    public boolean isFriend(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null)
            return false;
        Friend friend = findFriendRecord(userId1, userId2);
        if (friend == null)
            return false;
        return friend.getAccept();
    }

    /**
     * 判断是否发送了好友申请
     *
     * @param userId1 用户ID
     * @param userId2 用户ID
     * @return true 已发送申请 false 没有发送申请
     */
    @Override
    public boolean isWantFriend(Integer userId1, Integer userId2) {
        if (userId1 == null || userId2 == null)
            return false;
        Friend friend = findFriendRecord(userId1, userId2);
        if (friend == null)
            return false;
        return !friend.getAccept();
    }

}
