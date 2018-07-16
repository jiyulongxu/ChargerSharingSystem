package com.konsonx.controller.manage.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.konsonx.po.User;
import com.konsonx.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/api/manage/")
@Controller
@CrossOrigin
public class UsersAPI {
    @Resource(name = "UserService")
    private UserService userService;
    private Integer pageSize = 14;

    @GetMapping("/users")
    @ResponseBody
    public JSONObject getUserList(@RequestParam("pageNum") Integer pageNum ,@RequestParam(name = "userPhone",required = false)String userPhone,@RequestParam(name = "userAlias",required = false)String userAlias){

        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (pageNum != null){

            /**
             * 故有接口参数写法，转发至新接口
             */
            if (userPhone != null && !"".equals(userPhone)){
                return getUserListByPhone(userPhone,pageNum);
            }else if (userAlias != null && !"".equals(userAlias)){
                return getUserListByAlias(userAlias,pageNum);
            }
            code = 1;
            List<User> userList = userService.selectByPage(pageNum,pageSize);
            data = JSONObject.toJSON(userList);
        }
        else {
            code = 0;
            msg = "获取失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }

    @GetMapping("/users/phone/{userPhone}")
    @ResponseBody
    public JSONObject getUserListByPhone(@PathVariable(name = "userPhone") String userPhone,@RequestParam(name = "pageNum",required = false) Integer pageNum){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userPhone != null && userPhone !="") {
            if (pageNum != null){
                code = 1;
                List<User> userList = userService.selectByPhone(userPhone,pageNum,pageSize);
                data = JSONObject.toJSON(userList);
                System.out.println(data);
            }else{
                code = 1;
                User user= userService.selectByPhone(userPhone);
                user.setUser_password("");
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(JSONObject.toJSON(user));
                data = jsonArray;
                System.out.println(data);
            }
        }else {
            code = 0;
            msg = "获取失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }

    @GetMapping("/users/alias/{userAlias}")
    @ResponseBody
    public JSONObject getUserListByAlias(@PathVariable(name = "userAlias") String userAlias,@RequestParam(name = "pageNum",required = false) Integer pageNum){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userAlias != null && userAlias !="") {
            if (pageNum != null){
                code = 1;
                List<User> userList = userService.selectByAlias(userAlias,pageNum,pageSize);
                data = JSONObject.toJSON(userList);
            }else{
                code = 1;
                List<User> userList = userService.selectByAlias(userAlias,1,pageSize);
                data = JSONObject.toJSON(userList);
            }
        }else {
            code = 0;
            msg = "获取失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }


    @GetMapping("/users/{userId}")
    @ResponseBody
    public JSONObject getUser(@PathVariable("userId") Integer userId){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userId != null){
            User user = userService.selectById(userId);
           if (user != null){
               code = 1;
               msg = "获取成功";
               JSONArray jsonArray = new JSONArray();
               jsonArray.add(JSONObject.toJSON(user));
               data = jsonArray;
               System.out.println(data);
           }else{
               code = 0;
               msg = "不存在此用户。";
               data = null;
           }
        }else {
            code = 0;
            msg = "获取失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }

    @PostMapping("/users/{userId}/balance")
    @ResponseBody
    public JSONObject recharge(@PathVariable("userId") Integer userId,@RequestParam("money")Float money){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userId != null && money != null){
            User user = userService.selectById(userId);
            if (user != null){
                user.setUser_balance(user.getUser_balance() + money);
                if (userService.update(user)) {
                    code = 1;
                    msg = "操作成功。";
                    user = userService.selectById(userId);
                    user.setUser_password("");
                    data = user;
                }else{
                    code = 0;
                    msg = "操作失败。";
                    data = null;
                }
            }else{
                code = 0;
                msg = "不存在此用户。";
                data = null;
            }
        }else {
            code = 0;
            msg = "参数有误，充值失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }

    @PostMapping("/users/{userId}/phone")
    @ResponseBody
    public JSONObject resetPhone(@PathVariable("userId") Integer userId,@RequestParam("phone")String phone){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userId != null && phone != null && phone != ""){
            User user = userService.selectById(userId);
            if (user != null){
                user.setUser_phone(phone);
                if (userService.update(user)) {
                    code = 1;
                    msg = "操作成功。";
                    user = userService.selectById(userId);
                    user.setUser_password("");
                    data = user;
                }else{
                    code = 0;
                    msg = "操作失败。";
                    data = null;
                }
            }else{
                code = 0;
                msg = "不存在此用户。";
                data = null;
            }
        }else {
            code = 0;
            msg = "参数有误，操作失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }

    @DeleteMapping("/users/{userId}")
    @ResponseBody
    public JSONObject delete(@PathVariable("userId") Integer userId){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (userId != null){
            User user = userService.selectById(userId);
            if (user != null){
                if (userService.delete(userId)) {
                    code = 1;
                    msg = "操作成功。";
                    data = null;
                }else{
                    code = 0;
                    msg = "操作失败。";
                    data = null;
                }
            }else{
                code = 0;
                msg = "不存在此用户。";
                data = null;
            }
        }else {
            code = 0;
            msg = "参数有误，充值失败。";
            data = null;
        }
        resultJsonObject.put("code",code);
        resultJsonObject.put("msg",msg);
        resultJsonObject.put("data",data);
        return resultJsonObject;
    }
}
