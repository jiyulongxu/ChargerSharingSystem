package com.konsonx.controller.manage.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.konsonx.po.Pobk;
import com.konsonx.service.PobkService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/api/manage")
@Controller
@CrossOrigin
public class powerbankAPI {
    @Resource(name = "PobkService")
    private PobkService pobkService;
    private Integer pageSize = 14;

    @GetMapping("/powerbanks")
    @ResponseBody
    public JSONObject getPowerBanks(@RequestParam("pageNum")Integer pageNum,@RequestParam(value = "pobk_location_id",required = false)Integer locationId){
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        Object data = null;
        String msg = null;
        if (pageNum != null && pageNum>0){
            List<Pobk> list = null;
            if (locationId != null && locationId > 0){
                list = pobkService.selectByLocation(locationId,pageNum,pageSize);
            }else{
               list = pobkService.selectByPage(pageNum,pageSize);
            }

            if (list != null){
                code = 1;
                msg = "获取成功。";
                data = JSONObject.toJSON(list);
            }else{
                code = 0;
                msg = "获取失败。";
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误。";
            data= null;
        }
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }

    @PutMapping("/powerbanks")
    @ResponseBody
    public JSONObject putPowerBanks(@RequestParam(value = "pobk_location_id")Integer locationId,@RequestParam("pobk_amount")Integer addAmount){
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        Object data = null;
        String msg = null;
        Pobk pobk = new Pobk();
        pobk.setPobk_status("available");
        pobk.setPobk_location_id(locationId);
        try {
            if (pobkService.insert(pobk,addAmount)) {
                code = 1;
                msg = "投放成功。";
                data = null;
            }else{
                code = 0;
                msg = "投放失败。";
                data = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            code = -1;
            msg = e.getMessage();
            data= null;
        }
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }
    @GetMapping("/powerbanks/{pobk_id}")
    @ResponseBody
    public JSONObject getPowerBank(@PathVariable("pobk_id")Integer pobkId){
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        Object data = null;
        String msg = null;
        if (pobkId != null && pobkId >0 ){
            Pobk pobk = pobkService.selectById(pobkId);
            if (pobk != null){
                code = 1;
                msg = "获取成功。";
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(pobk);
                data = jsonArray;
            }else{
                code = 0;
                msg = "充电宝不存在。";
                data = null;
            }
        }else {
            code = -1;
            msg = "参数有误。";
            data = null;
        }

        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }

    @PostMapping("/powerbanks/{pobk_id}")
    @ResponseBody
    public JSONObject updatePowerBank(@PathVariable("pobk_id")Integer pobkId,@RequestParam("pobk_location_id")Integer newLocationId){
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        Object data = null;
        String msg = null;
        if (pobkId != null && pobkId >0 && newLocationId != null && newLocationId > 0){
            Pobk pobk = pobkService.selectById(pobkId);
            if (pobk != null){
                pobk.setPobk_location_id(newLocationId);
                try {
                    if (pobkService.updatewithLocation(pobk)){
                        code = 1;
                        msg = "转移成功。";
                        data = null;
                    }else{
                        code = 0;
                        msg = "转移失败。";
                        data = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    code = 0;
                    msg = "转移失败。 " + e.getMessage();
                    data = null;
                }

            }else{
                code = 0;
                msg = "充电宝不存在。";
                data = null;
            }
        }else {
            code = -1;
            msg = "参数有误。";
            data = null;
        }

        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }

    @DeleteMapping("/powerbanks/{pobk_id}")
    @ResponseBody
    public JSONObject deletPowerBank(@PathVariable("pobk_id")Integer pobkId){
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        Object data = null;
        String msg = null;
        if (pobkId != null && pobkId >0 ){
            Pobk pobk = pobkService.selectById(pobkId);
            if (pobk != null){
                try {
                    if (pobkService.delete(pobkId)) {
                        code = 1;
                        msg = "删除成功。";
                        data = null;
                    }else{
                        code = 0;
                        msg = "删除失败。";
                        data = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    code = 0;
                    msg = "删除失败。 " + e.getMessage();
                    data = null;
                }
            }else{
                code = 0;
                msg = "充电宝不存在。";
                data = null;
            }
        }else {
            code = -1;
            msg = "参数有误。";
            data = null;
        }

        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }
}
