package com.shixun.funchat.service;


import com.shixun.funchat.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface FriendService {
    //好友列表
    List<User> listfriend(int id);
    //批量删除好友
    String deleteFriendSelective(Integer[] ids, HttpSession session);
//    删除好友
    String deleteFriend(String username, HttpSession session);
    //添加好友
    String addFriend(Integer id, HttpSession session);
    //
    User friendinfo(String username);
}
