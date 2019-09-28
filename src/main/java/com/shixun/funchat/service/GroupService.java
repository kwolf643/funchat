package com.shixun.funchat.service;

import com.shixun.funchat.entity.ChatGroup;
import com.shixun.funchat.entity.User;

import java.util.List;

public interface GroupService {

    /**
     * 搜索群聊
     *
     * @param s 群聊名称或ID
     * @return 群聊数组
     */
    List<ChatGroup> searchChatGroup(String s);

    /**
     * 加入群聊
     *
     * @param userId 用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    boolean joinChatGroup(Integer userId,Integer groupId);

    /**
     * 退出群聊
     *
     * @param userId 用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    boolean leaveChatGroup(Integer userId,Integer groupId);

    /**
     * 创建群聊
     *
     * @param userId 用户ID
     * @param groupName 群聊名称
     * @param groupType 群聊类型 group 多对多 double 一对一
     * @return true 成功 false 失败
     */
    Integer createChatGroup(Integer userId, String groupName, String groupType);

    /**
     * 显示群聊
     *
     * @param userId 用户ID
     * @return 群聊数组
     */
    List<ChatGroup> displayChatGroup(Integer userId);

    /**
     * 查看群成员
     *
     * @param groupId 群聊ID
     * @return 用户数组
     */
    List<User> displayChatGroupMember(Integer groupId);

    /**
     * 验证群主
     *
     * @param userId 用户ID
     * @param groupId 群聊ID
     * @return true 是群主 false 其他
     */
    boolean isOwner(Integer userId, Integer groupId);

    /**
     * 验证群员
     *
     * @param userId 用户ID
     * @param groupId 群聊ID
     * @return true 是群员 false 其他
     */
    boolean isMember(Integer userId, Integer groupId);

    /**
     * 含有成员
     *
     * @param groupId 群聊ID
     * @param userId 用户ID
     * @return true 含有 false 其他
     */
    boolean hasMember(Integer groupId, Integer userId);

    /**
     * 解散群聊
     *
     * @param userId 用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    boolean deleteChatGroup(Integer userId, Integer groupId);

    /**
     * 踢出群聊
     *
     * @param ownerId 群主ID
     * @param removeId 被踢用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    boolean removeFromChatGroupByOwner(Integer ownerId,Integer removeId,Integer groupId);

    /**
     * 更新群聊信息
     *
     * @param ownerId 群主ID
     * @param chatGroup 新群聊信息
     * @return true 成功 false 失败
     */
    boolean updateChatGroupInfo(Integer ownerId, ChatGroup chatGroup);

}
