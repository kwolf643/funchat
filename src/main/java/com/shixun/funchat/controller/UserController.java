package com.shixun.funchat.controller;

import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

//    跳转登录页面
    @GetMapping("/login")
    public String tologin(){ return "login";}

//    根据用户名和密码登录
    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody User user, HttpSession session){
        Map<String, String> map;
        map = userService.login(user,session);
        return map;
    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute("USER_SESSION");
        return "redirect:/login";
    }

    //跳转注册页面
    @GetMapping("/register")
    public String toregister(){return "register";}

    // 注册，上传数据库
    @PostMapping("/register")
    @ResponseBody
    public Map<String, String> register(@RequestBody User user, HttpSession session){
        Map<String, String> map;
        map = userService.register(user,session);
        return map;
    }

    //查看个人资料
    @GetMapping("/userinfo")
    @ResponseBody
    public User userinfo(HttpServletRequest request){
        HttpSession session=request.getSession();
        User user = (User) session.getAttribute("USER_SESSION");
        user = userService.userinfo(user);
        return user;
    }

    //修改个人资料
    @PostMapping("/edituser")
    @ResponseBody
    public  Map<String, String> edituser(@RequestBody User user){
        Map<String, String> map;
        map = userService.edituser(user);
        return map;
    }

}
