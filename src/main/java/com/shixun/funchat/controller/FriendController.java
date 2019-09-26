package com.shixun.funchat.controller;

import com.shixun.funchat.entity.ChatGroup;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.FriendService;
import com.shixun.funchat.service.GroupService;
import com.shixun.funchat.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class FriendController {
    private static Logger log= LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    //跳转好友列表
    @GetMapping("/index")
    public String toListfriend(Model model, HttpServletRequest request){
        HttpSession session=request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");
        List<User> users=friendService.listfriend(user.getId());
        model.addAttribute("users",users);
//        return "test_listfriend";
        return "index_qiaofeng";
    }

    //好友资料
    @PostMapping("/friendinfo")
    @ResponseBody
    public User friendinfo(@RequestBody String username){
    User user= friendService.friendPersoanl(username);
    return user;
    }

    //批量删除好友
    @PostMapping("/deleteFriendSelective")
    public String deleteFriendSelective(Integer[] ids, HttpServletRequest request) {
        HttpSession session=request.getSession();
        String msg = friendService.deleteFriendSelective(ids,session);
        log.debug(msg);
        return "redirect:/listfriend";
    }

    //删除好友
    @GetMapping("/deleteFriend")
    public String deleteFriend(String username, HttpServletRequest request) {
        HttpSession session=request.getSession();
        String msg = friendService.deleteFriend(username,session);
        log.debug(msg);
        return "redirect:/listfriend";
    }

    //跳转好友搜索和群搜索页面
    @GetMapping("/search")
    public String tosearch(){return "test_search";}

    //实现添加好友搜索和群搜索
    @PostMapping("/search")
    @ResponseBody
    public List<User> Search(@RequestBody User user, Model model){
//        ChatGroup group = new ChatGroup();
//        group.setGropId(user.getId());
//        group.setGropName(user.getUsername());
        if (StringUtils.isBlank(user.getUsername())) {
            user.setUsername("_?/");
            List<User> users = userService.search(user);
            return users;
        }
        List<User> users = userService.search(user);
//        List<ChatGroup> groups =groupService.search(group);
//        model.addAttribute("users",users);
//        model.addAttribute("groups",groups);
//        return "test_search";
        return users;
    }

    //添加好友
    @GetMapping("/addfriend")
    @ResponseBody
    public String addfriend(@RequestBody int id,HttpServletRequest request){
        HttpSession session=request.getSession();
        String msg = friendService.addFriend(id,session);
        log.debug(msg);
        return msg;
    }
}
