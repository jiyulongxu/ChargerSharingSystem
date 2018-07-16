package com.konsonx.controller.manage.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Manage {
    @GetMapping("/manage/manage.action")
    public String manage(){
        return "manage/manage/index";
    }
}
