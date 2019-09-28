package com.shixun.funchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shixun.funchat.entity.User;
import com.shixun.funchat.service.GroupService;
import com.shixun.funchat.utils.MyException;
import com.shixun.funchat.utils.MyExceptionType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 控制器
 */
@Component
public class WebSocketController implements WebSocketHandler {
    private static Logger log = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private GroupService groupService;

    //连接表
    private static ConcurrentHashMap<Integer, WebSocketSession> websocketList = new ConcurrentHashMap<>();

    /**
     * 消息集中处理
     *
     * @param session WebSocket 会话
     * @param message 消息
     * @throws Exception 异常
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String msgs = (String) message.getPayload();
        User user = (User) session.getAttributes().get("USER_SESSION");
        try {
            if (StringUtils.isBlank(msgs))
                throw new MyException(MyExceptionType.Empty);
            log.debug("收到消息：" + msgs);
            JSONArray json_msgs = JSON.parseArray(msgs);
            //遍历消息
            for (int i = 0; i < json_msgs.size(); i++) {
                JSONObject json_msg = json_msgs.getJSONObject(i);
                //提取消息内容
                String cmd = json_msg.getString("cmd");
                if (StringUtils.isBlank(cmd))
                    throw new MyException(MyExceptionType.NotExist);
                switch (cmd) {
                    case "updateUi": {
                        Integer toUserId = json_msg.getInteger("toUserId");
                        if (toUserId == null)
                            throw new MyException(MyExceptionType.ParamError);
                        WebSocketSession toSession = websocketList.get(toUserId);
                        if (toSession == null)
                            throw new MyException(MyExceptionType.NotOnline);
                        toSession.sendMessage(new TextMessage(JSON.toJSONString(json_msg)));
                        throw new MyException(MyExceptionType.Success);
                    }
                    case "sendMessage": {
                        Integer toGroupId = json_msg.getInteger("toGroupId");
                        String contentText = json_msg.getString("contentText");
                        if (toGroupId == null || StringUtils.isBlank(contentText))
                            throw new MyException(MyExceptionType.ParamError);
                        //获取组内成员 ID
                        List<User> group_users = groupService.displayChatGroupMember(toGroupId);
                        if (group_users.isEmpty())
                            throw new MyException(MyExceptionType.ParamError);
                        //群发在线消息
                        for (User group_user : group_users) {
                            //获取在线用户会话
                            WebSocketSession toSession = websocketList.get(group_user.getId());
                            if (toSession != null) {
                                json_msg.put("fromUserId", user.getId());
                                json_msg.put("fromUserName", user.getUsername());
                                toSession.sendMessage(new TextMessage(JSON.toJSONString(json_msg)));
                            }
                        }
                        throw new MyException(MyExceptionType.Success);
                    }
                    default:
                        throw new MyException(MyExceptionType.DefaultError);
                }
            }
        } catch (MyException e) {
            switch (e.getType()) {
                case Empty:
                    log.info("空消息");
                    break;
                case NotExist:
                    log.info("没有命令");
                    break;
                case ParamError:
                    log.info("命令不存在");
                    break;
                case Success:
                    log.info("命令执行成功");
                    break;
                case NotOnline:
                    log.info("目标不在线");
                    break;
                case DefaultError:
                    log.info("无效的命令");
                    break;
            }
        }
    }

    /**
     * 建立连接
     *
     * @param session WebSocket 会话
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer userId = getUserID(session);
        if (userId != null) {
            websocketList.put(userId, session);
            log.debug("新连接，用户ID：" + userId + "，当前人数：" + websocketList.size());
        } else {
            log.debug("userId 为空");
        }
    }

    /**
     * 连接错误
     *
     * @param session   WebSocket 会话
     * @param throwable 异常
     * @throws Exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        if (session.isOpen())
            session.close();
        Integer userId = getUserID(session);
        if (userId != null)
            websocketList.remove(userId);
        log.error("连接错误");
    }

    /**
     * 连接关闭
     *
     * @param session     WebSocket 会话
     * @param closeStatus 关闭状态
     * @throws Exception 异常
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Integer userId = getUserID(session);
        if (userId != null)
            websocketList.remove(userId);
        log.debug("连接关闭，用户ID：" + userId + "，当前人数：" + websocketList.size());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户 ID
     *
     * @param session WebSocket 会话
     * @return 用户ID
     */
    private Integer getUserID(WebSocketSession session) {
        try {
            User user = (User) session.getAttributes().get("USER_SESSION");
            if (user == null)
                return null;
            return user.getId();
        } catch (Exception e) {
            return null;
        }
    }

}
