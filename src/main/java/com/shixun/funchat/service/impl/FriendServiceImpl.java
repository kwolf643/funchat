package com.shixun.funchat.service.impl;

import com.shixun.funchat.dao.FriendMapper;
import com.shixun.funchat.dao.UserMapper;
import com.shixun.funchat.entity.Friend;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.FriendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FriendServiceImpl implements FriendService {
    private static Logger log= LoggerFactory.getLogger(FriendServiceImpl.class);

    @Autowired
    private FriendMapper friendMapper;

    @Autowired
    private UserMapper userMapper;

    //好友列表
    @Override
    public List<User> listfriend(int id) {
        List<User> users=friendMapper.selectByFriendId(id);
        return users;
    }

    //删除好友
    @Override
    public String deleteFriend(String username, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        User user1=userMapper.CheckByName(username);
        Friend friend = new Friend();
        friend.setFriendaId(user1.getId());
        friend.setFriendbId(user.getId());
        int state=friendMapper.deleteByPrimaryKey(friend);
        if (state !=0) {
            return "删除了id为" + user1.getId() + "的好友！！";
        }
        else  return "id为" + user1.getId() + "的好友,删除失败！";
    }

    //批量删除好友
    @Override
    public String deleteFriendSelective(Integer[] ids, HttpSession session) {
        int num=0;
        User user = (User) session.getAttribute("USER_SESSION");
        if(ids !=null){
            for (Integer id : ids) {
                Friend friend = new Friend();
                friend.setFriendaId(id);
                friend.setFriendbId(user.getId());
                int state=friendMapper.deleteByPrimaryKey(friend);
                if (state !=0) {
                    num+=1;
                    log.debug("删除了id为" + id + "的好友！");
                }
                else  log.debug("id为" + id + "的好友,删除失败！");
            }
            return "成功删除了"+num+"位好友！";
        }else{
            log.debug("ids=null");
            return "选择为空！";
        }
    }

    //添加好友
    @Override
    public String addFriend(Integer id, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        Friend friend = new Friend();
        friend.setFriendaId(user.getId());
        friend.setFriendbId(id);
        Friend friend1 = friendMapper.selectByPrimaryKey(friend);
        if (friend1 == null) {
            int state = friendMapper.insert(friend);
            if (state != 0) {
                return "添加好友成功！";
            } else return "添加好友失败 ！";
        }
        else return "该用户已经是你的好友";
    }

    //好友资料
    @Override
    public User friendPersoanl(String username) {
        User user = userMapper.CheckByName(username);
        return user;
    }

}
