package com.shixun.funchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    //主页面
    @RequestMapping(value = "/")
    public String index(Model model, HttpSession session) {
        return "index";
    }
}
