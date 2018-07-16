package com.konsonx.controller.manage.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class Login {

    @GetMapping("/manage/login.action")
    public String login(){
        return "manage/login/index";
    }
}

