package com.shixun.funchat.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 拦截器
 */
public class WebSocketInterceptor extends HttpSessionHandshakeInterceptor {
    private static Logger log = LoggerFactory.getLogger(WebSocketInterceptor.class);

    /**
     * 握手前
     *
     * @param request    当前请求
     * @param response   当前响应
     * @param wsHandler  目标 Websocket Handler
     * @param attributes 与 Websocket 会话关联的 HTTP 握手的属性（拷贝）
     * @return 继续握手（true）或终止（false）
     * @throws Exception 异常
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.debug("握手前");
        super.beforeHandshake(request, response, wsHandler, attributes);
        return attributes.get("USER_SESSION") != null;
        /*if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            String userID = serverHttpRequest.getServletRequest().getParameter("userID");
            if (StringUtils.isNotBlank(userID)) {
                attributes.put("userID", userID);
                log.debug("userID=" + userID);
            } else {
                log.error("userID 为空");
                return false;
            }
        } else {
            log.error("请求不能转化为 ServletServerHttpRequest");
            return false;
        }
        return true;*/
    }

    /**
     * 握手后
     *
     * @param request   当前请求
     * @param response  当前响应
     * @param wsHandler 目标 Websocket Handler
     * @param ex        握手期间的异常（或为 null）
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.debug("握手后");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
