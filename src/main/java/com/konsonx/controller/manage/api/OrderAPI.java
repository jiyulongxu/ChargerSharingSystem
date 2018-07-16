package com.konsonx.controller.manage.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.konsonx.po.Order;
import com.konsonx.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/api/manage")
@Controller
@CrossOrigin
public class OrderAPI {
    @Resource(name = "OrderService")
    private OrderService orderService;
    private Integer pageSize = 14;

    @GetMapping("/orders")
    @ResponseBody
    public JSONObject getOrders(@RequestParam("pageNum") Integer pageNum,@RequestParam(value = "order_user_id",required = false)Integer userId){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (pageNum != null && pageNum > 0){
            List<Order> list = null;
            if (userId != null && userId >0){
                //用户ID查询订单
                list = orderService.selectByOrder_lent_location_idAndOrder_revert_location_idAndOrder_user_id(null,null,userId,pageNum,pageSize);
            }else{
                //无条件分页查询
                list = orderService.selectByPage(pageNum,pageSize);
            }
            code = 1;
            msg = "查询成功。";
            data = JSONArray.toJSON(list);
        }else{
            code = -1;
            msg = "参数有误";
            data = null;
        }

        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }
    @GetMapping("/orders/{order_id}")
    @ResponseBody
    public JSONObject getOrders(@PathVariable("order_id")Integer orderId){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (orderId != null && orderId >0){
            Order order = orderService.selectById(orderId);
            if (order != null){
                code = 1;
                msg = "查询成功。";
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(JSONObject.toJSON(order));
                data = jsonArray;
            }else{
                code = 0;
                msg = "不存在订单。";
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误";
            data = null;
        }

        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }
}
