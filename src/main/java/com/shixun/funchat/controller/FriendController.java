package com.shixun.funchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.FriendService;
import com.shixun.funchat.service.GroupService;
import com.shixun.funchat.service.UserService;
import com.shixun.funchat.utils.MyException;
import com.shixun.funchat.utils.MyExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class FriendController {
    private static Logger log = LoggerFactory.getLogger(FriendController.class);

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;

    /**
     * 好友管理控制
     *
     * @param jsonString JSON字符串
     * @param session    HTTP 会话
     * @return JSON返回数据
     */
    @PostMapping("/FriendControl")
    @ResponseBody
    public Map<String, Object> groupControl(@RequestBody String jsonString, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        map.put("result", "error");
        map.put("error_info", "Default error!");

        //获取用户信息
        User user = (User) session.getAttribute("USER_SESSION");

        //接收json数据
        log.info("GroupControl: " + jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String json_func = jsonObject.getString("func");

        try {
            if (StringUtils.isBlank(json_func)) {
                throw new MyException(MyExceptionType.Empty);
            }
            //函数选择
            switch (json_func) {
                case "getUserFriendsAccept": {
                    List<User> users = friendService.getUserFriendsAccept(user.getId());
                    map.put("return", users);
                    throw new MyException(MyExceptionType.Success);
                }
                case "getUserFriendsNotAccept": {
                    List<User> users = friendService.getUserFriendsNotAccept(user.getId());
                    map.put("return", users);
                    throw new MyException(MyExceptionType.Success);
                }
                case "getUserWantFriends": {
                    List<User> users = friendService.getUserWantFriends(user.getId());
                    map.put("return", users);
                    throw new MyException(MyExceptionType.Success);
                }
                case "addFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId != null) {
                        User rs_user = friendService.addFriend(user.getId(), friendId);
                        if (rs_user != null) {
                            map.put("return", rs_user);
                            throw new MyException(MyExceptionType.Success);
                        } else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "deleteFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId != null) {
                        if (friendService.deleteFriend(user.getId(), friendId)) {
                            throw new MyException(MyExceptionType.Success);
                        } else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "acceptFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId == null)
                        throw new MyException(MyExceptionType.ParamError);
                    if (friendService.acceptFriend(user.getId(), friendId)) {
                        throw new MyException(MyExceptionType.Success);
                    } else
                        throw new MyException(MyExceptionType.Failed);
                }
                case "rejectFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId == null)
                        throw new MyException(MyExceptionType.ParamError);
                    if (friendService.rejectFriend(user.getId(), friendId)) {
                        throw new MyException(MyExceptionType.Success);
                    } else
                        throw new MyException(MyExceptionType.Failed);
                }
                case "isFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId != null) {
                        if (friendService.isFriend(user.getId(), friendId)) {
                            throw new MyException(MyExceptionType.Success);
                        } else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "isWantFriend": {
                    Integer friendId = jsonObject.getInteger("friendId");
                    if (friendId != null) {
                        if (friendService.isWantFriend(user.getId(), friendId)) {
                            throw new MyException(MyExceptionType.Success);
                        } else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                default:
                    throw new MyException(MyExceptionType.NotExist);
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
