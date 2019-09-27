package com.shixun.funchat.interceptor;

import com.shixun.funchat.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private List<String> url = new ArrayList<>();

    /**
     * 开始进入地址请求拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("USER_SESSION") != null) {
            //已登录
            if (request.getRequestURI().equals("/login")) {
                response.sendRedirect("/");
                return false;
            }
            return true;
        } else {
            //未登录
            if (request.getRequestURI().equals("/login"))
                return true;
            response.sendRedirect("/login");
            return false;
        }
    }

    /**
     * 处理请求完成后视图渲染之前的处理操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * 视图渲染之后的操作
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * 定义排除拦截URL
     *
     * @return 排除拦截的 url
     */
    public List<String> getUrl() {
        url.add("/UserControlPublic"); //用户控制-开放
        url.add("/error");              //错误页面

        //网站静态资源
        url.add("/css/**");
        url.add("/js/**");
        url.add("/lib/**");
        url.add("/fonts/**");
        url.add("/images/**");
        url.add("/img/**");
        url.add("/icons/**");
        url.add("/media/**");
        url.add("/*.html");

        return url;
    }

}