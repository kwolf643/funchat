package com.shixun.funchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shixun.funchat.entity.ChatGroup;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.GroupService;
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
public class GroupController {
    private static Logger log = LoggerFactory.getLogger(GroupController.class);

    private enum MyExceptionType {
        Empty, NotExist, ParamError, Success, Failed,
        DefaultError
    }

    static class MyException extends Exception {
        private MyExceptionType type;

        public MyException() {
            super();
        }

        public MyException(MyExceptionType type) {
            this.type = type;
        }

        public MyExceptionType getType() {
            return type;
        }
    }


    @Autowired
    private GroupService groupService;

    @GetMapping("/testGroup")
    public String toTestGroup() {
        return "test_group";
    }

    @GetMapping("/index_qiaofeng")
    public String indexQiaoFeng() {
        return "index_qiaofeng";
    }

    @PostMapping("/GroupControl")
    @ResponseBody
    public String testGroup(@RequestBody String jsonString, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", "error");
        map.put("error_info", "Default error!");

        //获取用户信息
        User user = (User) session.getAttribute("USER_SESSION");

        //接收json数据
        log.info("GroupControl" + jsonString);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String json_func = jsonObject.getString("func");

        try {
            if (StringUtils.isBlank(json_func)) {
                throw new MyException(MyExceptionType.Empty);
            }
            //函数选择
            switch (json_func) {
                case "searchGroup": {
                    String groupName = jsonObject.getString("groupName");
                    if (StringUtils.isNotBlank(groupName)) {
                        List<ChatGroup> chatGroups = groupService.searchChatGroup(groupName);
                        map.put("return", chatGroups);
                        throw new MyException(MyExceptionType.Success);
                    } else
                    throw new MyException(MyExceptionType.ParamError);
                }
                case "joinGroup": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (groupId != null) {
                        if (groupService.joinChatGroup(user.getId(), groupId))
                            throw new MyException(MyExceptionType.Success);
                        else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "leaveGroup": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (groupId != null) {
                        if (groupService.leaveChatGroup(user.getId(), groupId))
                            throw new MyException(MyExceptionType.Success);
                        else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "createGroup": {
                    String groupName = jsonObject.getString("groupName");
                    if (StringUtils.isNotBlank(groupName)) {
                        Integer rs_groupId = groupService.createChatGroup(user.getId(), groupName, "group");
                        if (rs_groupId != null) {
                            map.put("return", rs_groupId);
                            throw new MyException(MyExceptionType.Success);
                        } else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "displayGroup": {
                    List<ChatGroup> chatGroups = groupService.displayChatGroup(user.getId());
                    map.put("return", chatGroups);
                    throw new MyException(MyExceptionType.Success);
                }
                case "updateGroupInfo": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    String groupName = jsonObject.getString("groupName");
                    if (groupId != null && StringUtils.isNotBlank(groupName)) {
                        ChatGroup up_group = new ChatGroup();
                        up_group.setGropId(groupId);
                        up_group.setGropName(groupName);
                        if (groupService.updateChatGroupInfo(user.getId(), up_group))
                            throw new MyException(MyExceptionType.Success);
                        else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "getGroupMember": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (groupId != null) {
                        if (!groupService.hasMember(groupId, user.getId()))
                            throw new MyException(MyExceptionType.Failed);
                        List<User> users = groupService.displayChatGroupMember(groupId);
                        map.put("return", users);
                        throw new MyException(MyExceptionType.Success);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "isOwner": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (groupId != null) {
                        if (groupService.isOwner(user.getId(), groupId))
                            throw new MyException(MyExceptionType.Success);
                        else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "deleteGroup": {
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (groupId != null) {
                        if (groupService.deleteChatGroup(user.getId(), groupId))
                            throw new MyException(MyExceptionType.Success);
                        else
                            throw new MyException(MyExceptionType.Failed);
                    } else
                        throw new MyException(MyExceptionType.ParamError);
                }
                case "kickMember": {
                    Integer removeId = jsonObject.getInteger("removeId");
                    Integer groupId = jsonObject.getInteger("groupId");
                    if (removeId != null && groupId != null) {
                        if (groupService.removeFromChatGroupByOwner(user.getId(), removeId, groupId))
                            throw new MyException(MyExceptionType.Success);
                        else
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

        JSONObject rs_json = (JSONObject) JSON.toJSON(map);
        log.info("rs_json = " + rs_json.toJSONString());
        return rs_json.toJSONString();
    }
}
