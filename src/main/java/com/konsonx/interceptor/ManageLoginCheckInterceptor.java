package com.konsonx.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManageLoginCheckInterceptor extends HandlerInterceptorAdapter {

    /**
     * 拦截了/manage/**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute("admin");
        if (object == null){
            response.sendRedirect("/manage/login.action");
            return false;
        }else{
            return true;
        }
    }
}
