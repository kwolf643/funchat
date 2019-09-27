package com.shixun.funchat.service.impl;

import com.shixun.funchat.dao.ChatGroupMapper;
import com.shixun.funchat.dao.UserMapper;
import com.shixun.funchat.entity.ChatGroup;
import com.shixun.funchat.dao.GroupStructureMapper;
import com.shixun.funchat.entity.GroupStructure;
import com.shixun.funchat.entity.GroupStructureKey;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    private static Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    private GroupStructureMapper groupStructureMapper; //群结构表接口
    @Autowired
    private ChatGroupMapper chatGroupMapper; //群表接口
    @Autowired
    private UserMapper userMapper; //用户表接口

    /**
     * 搜索群聊
     *
     * @param groupName 群聊名称
     * @return 群聊数组
     */
    @Override
    public List<ChatGroup> searchChatGroup(String groupName) {
        return chatGroupMapper.selectChatGroupByGroupName(groupName);
    }

    /**
     * 加入群聊
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public boolean joinChatGroup(Integer userId, Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectByPrimaryKey(groupId);
        if (chatGroup == null) //群聊不存在
            return false;
        //群聊人数+1
        chatGroup.setSum(chatGroup.getSum() + 1);
        chatGroupMapper.updateByPrimaryKey(chatGroup);
        //新的群结构记录
        GroupStructure groupStructure = new GroupStructure();
        groupStructure.setId(userId);
        groupStructure.setGropId(groupId);
        groupStructure.setGropJob("member");
        groupStructureMapper.insert(groupStructure);
        return true;
    }

    /**
     * 退出群聊
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public boolean leaveChatGroup(Integer userId, Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectByPrimaryKey(groupId);
        if (chatGroup == null) //群聊不存在
            return false;
        if (!hasMember(groupId, userId)) //不是成员
            return false;
        if (isOwner(userId, groupId)) //是群主
            return false;
        //群聊人数-1
        chatGroup.setSum(chatGroup.getSum() - 1);
        chatGroupMapper.updateByPrimaryKey(chatGroup);
        //删除群结构记录
        GroupStructureKey groupStructureKey = new GroupStructureKey();
        groupStructureKey.setId(userId);
        groupStructureKey.setGropId(groupId);
        groupStructureMapper.deleteByPrimaryKey(groupStructureKey);
        return true;
    }

    /**
     * 创建群聊
     *
     * @param userId    用户ID
     * @param groupName 群聊名称
     * @param groupType 群聊类型 group 多对多 double 一对一
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public Integer createChatGroup(Integer userId, String groupName, String groupType) {
        //插入群表记录
        ChatGroup chatGroup = new ChatGroup();
        chatGroup.setGropName(groupName);
        chatGroup.setGropType(groupType);
        chatGroup.setSum(1);
        chatGroupMapper.insert(chatGroup);
        //新的群结构记录
        GroupStructure groupStructure = new GroupStructure();
        groupStructure.setId(userId);
        groupStructure.setGropId(chatGroup.getGropId());
        groupStructure.setGropJob("owner");
        groupStructureMapper.insert(groupStructure);
        return chatGroup.getGropId();
    }

    /**
     * 显示群聊
     *
     * @param userId 用户ID
     * @return 群聊数组
     */
    @Override
    public List<ChatGroup> displayChatGroup(Integer userId) {
        return chatGroupMapper.selectChatGroupsByUserId(userId);
    }

    /**
     * 查看群成员
     *
     * @param groupId 群聊ID
     * @return 用户数组
     */
    @Override
    public List<User> displayChatGroupMember(Integer groupId) {
        return userMapper.selectUserByGroupId(groupId);
    }

    /**
     * 验证群主
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     * @return true 是群主 false 其他
     */
    @Override
    @Transactional
    public boolean isOwner(Integer userId, Integer groupId) {
        //验证群主
        GroupStructureKey groupStructureKey = new GroupStructureKey();
        groupStructureKey.setGropId(groupId);
        groupStructureKey.setId(userId);
        GroupStructure groupStructure = groupStructureMapper.selectByPrimaryKey(groupStructureKey);
        if (groupStructure == null)
            return false;
        return groupStructure.getGropJob().equals("owner");
    }

    /**
     * 验证群员
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     * @return true 是群员 false 其他
     */
    @Override
    public boolean isMember(Integer userId, Integer groupId) {
        //验证群员
        GroupStructureKey groupStructureKey = new GroupStructureKey();
        groupStructureKey.setGropId(groupId);
        groupStructureKey.setId(userId);
        GroupStructure groupStructure = groupStructureMapper.selectByPrimaryKey(groupStructureKey);
        if (groupStructure == null)
            return false;
        return groupStructure.getGropJob().equals("member");
    }

    /**
     * 含有成员
     *
     * @param groupId 群聊ID
     * @param userId  用户ID
     * @return true 含有 false 其他
     */
    @Override
    public boolean hasMember(Integer groupId, Integer userId) {
        //验证含有
        GroupStructureKey groupStructureKey = new GroupStructureKey();
        groupStructureKey.setGropId(groupId);
        groupStructureKey.setId(userId);
        GroupStructure groupStructure = groupStructureMapper.selectByPrimaryKey(groupStructureKey);
        return groupStructure != null;
    }

    /**
     * 解散群聊
     *
     * @param userId  用户ID
     * @param groupId 群聊ID
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public boolean deleteChatGroup(Integer userId, Integer groupId) {
        ChatGroup chatGroup = chatGroupMapper.selectByPrimaryKey(groupId);
        if (chatGroup == null) //群聊不存在
            return false;
        if (!isOwner(userId, groupId)) //不是群主
            return false;
        //删除所有成员
        GroupStructure groupStructure = new GroupStructure();
        groupStructure.setGropId(groupId);
        groupStructureMapper.deleteSelective(groupStructure);
        //删除群
        chatGroupMapper.deleteByPrimaryKey(groupId);
        return true;
    }

    /**
     * 踢出群聊
     *
     * @param ownerId  群主ID
     * @param removeId 被踢用户ID
     * @param groupId  群聊ID
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public boolean removeFromChatGroupByOwner(Integer ownerId, Integer removeId, Integer groupId) {
        if(ownerId.equals(removeId))
            return false;
        ChatGroup chatGroup = chatGroupMapper.selectByPrimaryKey(groupId);
        if (chatGroup == null) //群聊不存在
            return false;
        if (!isOwner(ownerId, groupId)) //不是群主
            return false;
        if (!hasMember(groupId, removeId)) //不是成员
            return false;
        //群聊人数-1
        chatGroup.setSum(chatGroup.getSum() - 1);
        chatGroupMapper.updateByPrimaryKey(chatGroup);
        //删除群结构记录
        GroupStructureKey groupStructureKey = new GroupStructureKey();
        groupStructureKey.setId(removeId);
        groupStructureKey.setGropId(groupId);
        groupStructureMapper.deleteByPrimaryKey(groupStructureKey);
        return true;
    }

    /**
     * 更新群聊信息
     *
     * @param ownerId   群主ID
     * @param chatGroup 新群聊信息
     * @return true 成功 false 失败
     */
    @Override
    @Transactional
    public boolean updateChatGroupInfo(Integer ownerId, ChatGroup chatGroup) {
        ChatGroup group = chatGroupMapper.selectByPrimaryKey(chatGroup.getGropId());
        if (group == null) //群聊不存在
            return false;
        if (!isOwner(ownerId, chatGroup.getGropId())) //不是群主
            return false;
        //更新群表
        chatGroupMapper.updateByPrimaryKeySelective(chatGroup);
        return true;
    }

    //查找群
    @Override
    public List<ChatGroup> search(ChatGroup group) {
        List<ChatGroup> groups = chatGroupMapper.selectByIdOrName(group);
        return groups;
    }

}
