package com.shixun.funchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.UserService;
import com.shixun.funchat.utils.MyException;
import com.shixun.funchat.utils.MyExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 跳转登录界面
     *
     * @return 登录界面字符串
     */
    @GetMapping("/login")
    public String tologin() {
        return "login";
    }

    /**
     * 注销用户
     *
     * @param session HTTP 会话
     * @return 重定向至登录页面
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        // 移除session
        session.removeAttribute("USER_SESSION");
        return "redirect:/login";
    }

    /**
     * 用户控制-开放
     *
     * @param jsonString JSON字符串
     * @param session    HTTP 会话
     * @return JSON返回数据
     */
    @PostMapping("/UserControlPublic")
    @ResponseBody
    public Map<String, Object> login(@RequestBody String jsonString, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", "error");
        map.put("error_info", "Default error!");

        //接收json数据
        log.info("UserControlPublic: " + jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String json_func = jsonObject.getString("func");

        try {
            if (StringUtils.isBlank(json_func)) {
                throw new MyException(MyExceptionType.Empty);
            }
            switch (json_func) {
                case "login": {
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");
                    if (StringUtils.isBlank(username) || StringUtils.isBlank(password))
                        throw new MyException(MyExceptionType.ParamError);
                    User user = userService.login(username, password);
                    if (user == null)
                        throw new MyException(MyExceptionType.Failed);
                    user.setPassword(null);
                    map.put("return", user);
                    session.setAttribute("USER_SESSION", user);
                    throw new MyException(MyExceptionType.Success);
                }
                case "regist": {
                    User param_user = new User();
                    param_user.setUsername(jsonObject.getString("username"));
                    param_user.setPassword(jsonObject.getString("password"));
                    param_user.setGeder(jsonObject.getString("geder"));
                    param_user.setBirthday(jsonObject.getDate("birthday"));
                    param_user.setTelephone(jsonObject.getLong("telephone"));
                    param_user.setAddr(jsonObject.getString("telephone"));
                    param_user.setMail(jsonObject.getString("mail"));
                    param_user.setPerSignature(jsonObject.getString("perSignature"));
                    if (StringUtils.isBlank(param_user.getUsername()) || StringUtils.isBlank(param_user.getPassword()))
                        throw new MyException(MyExceptionType.ParamError);
                    User rs_user = userService.register(param_user);
                    if (rs_user == null)
                        throw new MyException(MyExceptionType.Failed);
                    rs_user.setPassword(null);
                    map.put("return", rs_user);
                    throw new MyException(MyExceptionType.Success);
                }
            }
        } catch (MyException e) {
            switch (e.getType()) {
                case Empty:
                    map.put("error_info", "Func is empty!");
                    break;
                case NotExist:
                    map.put("error_info", "Func is not exist!");
                    break;
                case ParamError:
                    map.put("error_info", "Params is wrong!");
                    break;
                case Success:
                    map.put("result", "success");
                    break;
                case Failed:
                    map.put("result", "failed");
                    break;
            }
        }

        return map;
    }

    /**
     * 用户控制-登录后
     *
     * @param jsonString JSON字符串
     * @param session    HTTP 会话
     * @return JSON返回数据
     */
    @PostMapping("/UserControl")
    @ResponseBody
    public Map<String, Object> userControl(@RequestBody String jsonString, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", "error");
        map.put("error_info", "Default error!");

        //获取用户信息
        User user = (User) session.getAttribute("USER_SESSION");

        //接收json数据
        log.info("UserControl: " + jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String json_func = jsonObject.getString("func");

        try {
            if (StringUtils.isBlank(json_func)) {
                throw new MyException(MyExceptionType.Empty);
            }
            switch (json_func) {
                case "getUserInfo": {
                    user.setPassword(null);
                    map.put("return", user);
                    throw new MyException(MyExceptionType.Success);
                }
                case "updateUserInfo": {
                    User param_user = new User();
                    param_user.setId(user.getId());
                    param_user.setUsername(jsonObject.getString("username"));
                    param_user.setPassword(jsonObject.getString("password"));
                    param_user.setGeder(jsonObject.getString("geder"));
                    param_user.setBirthday(jsonObject.getDate("birthday"));
                    param_user.setTelephone(jsonObject.getLong("telephone"));
                    param_user.setAddr(jsonObject.getString("telephone"));
                    param_user.setMail(jsonObject.getString("mail"));
                    param_user.setPerSignature(jsonObject.getString("perSignature"));
                    User rs_user = userService.updateUserInfo(param_user);
                    if (rs_user == null)
                        throw new MyException(MyExceptionType.Failed);
                    rs_user.setPassword(null);
                    map.put("return", rs_user);
                    session.setAttribute("USER_SESSION", rs_user);
                    throw new MyException(MyExceptionType.Success);
                }
                case "searchUser": {
                    String searchIdOrName = jsonObject.getString("searchIdOrName");
                    if (StringUtils.isBlank(searchIdOrName))
                        throw new MyException(MyExceptionType.ParamError);
                    List<User> users = userService.searchUserByIdOrName(searchIdOrName);
                    map.put("return", users);
                    throw new MyException(MyExceptionType.Success);
                }
            }
        } catch (MyException e) {
            switch (e.getType()) {
                case Empty:
                    map.put("error_info", "Func is empty!");
                    break;
                case NotExist:
                    map.put("error_info", "Func is not exist!");
                    break;
                case ParamError:
                    map.put("error_info", "Params is wrong!");
                    break;
                case Success:
                    map.put("result", "success");
                    break;
                case Failed:
                    map.put("result", "failed");
                    break;
            }
        }

        return map;
    }

}
