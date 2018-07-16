package com.konsonx.controller.manage.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.konsonx.po.Location;
import com.konsonx.po.NavLocation;
import com.konsonx.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping(value = "/api/manage")
@Controller
@CrossOrigin
public class LocationAPI {
    @Resource(name = "LocationService")
    private LocationService locationService;
    private Integer pageSize = 14;

    /**
     *
     * @param pageNum 不可空
     * @param city 可空
     * @param district 可空
     * @param address   可空
     * @return
     */
    @GetMapping("/locations")
    @ResponseBody
    public JSONObject getLocations(@RequestParam("pageNum")Integer pageNum,@RequestParam(name = "locationCity",required = false)String city,@RequestParam(name = "locationDistrict",required = false)String district,@RequestParam(name = "locationAddress",required = false)String address){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (pageNum != null){
            if (city == null || "".equals(city))city = null;
            if (district == null || "".equals(district))district = null;
            if (address == null || "".equals(address))address = null;

            List<Location> locations = locationService.selectByCityAndDistrictAndAddress(city,district,address,pageNum,pageSize);
            if (locations != null){
                code = 1;
                msg = "获取成功。";
                data = JSONObject.toJSON(locations);
            }else{
                code = 0;
                msg = "获取失败。";
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误。";
            data = null;
        }
        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }

    @GetMapping("/locations/alias/{locationAlias}")
    @ResponseBody
    public JSONObject getLocations(@RequestParam("pageNum")Integer pageNum, @PathVariable("locationAlias")String alias){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (pageNum != null && alias != null && !"".equals(alias)){
            List<Location> locations = locationService.selectByAlias(alias,pageNum,pageSize);
            if (locations != null){
                code = 1;
                msg = "获取成功。";
                data = JSONObject.toJSON(locations);
            }else{
                code = 0;
                msg = "获取失败。";
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误。";
            data = null;
        }
        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }

    @GetMapping("/locations/{locationId}")
    @ResponseBody
    public JSONObject getLocation( @PathVariable("locationId")Integer locationId){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (locationId != null && locationId > 0){
            Location location = locationService.selectById(locationId);
            if (location != null){
                code = 1;
                msg = "获取成功。";
                JSONArray jsonArray = new JSONArray();
                jsonArray.add(location);
                data = JSONObject.toJSON(jsonArray);
            }else{
                code = 0;
                msg = "获取失败。";
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误。";
            data = null;
        }
        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }
    @PostMapping("/locations/{locationId}")
    @ResponseBody
    public JSONObject updateLocation( @PathVariable("locationId")Integer locationId,@RequestParam(name = "address",required = false)String address,
                                      @RequestParam(value = "alias",required = false)String alias,@RequestParam(value = "amount",required = false)Integer amount){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;
        if (locationId != null && locationId > 0){
            Location location = locationService.selectById(locationId);
            NavLocation navLocation = new NavLocation();
            navLocation.setId(location.getLocation_yun_id());
            if (address != null && !"".equals(address)){
                location.setLocation_address(address);
                navLocation.setAddress(location.getLocation_city() + location.getLocation_district() + location.getLocation_address());
            }
            if (alias != null && !"".equals(alias)){
                location.setLocation_alias(alias);
                navLocation.setName(alias);
            }
            if (amount != null &&  amount>0){
                location.setLocation_amount(amount);
            }

            try {
                if (locationService.update(location,navLocation)) {
                    code = 1;
                    msg = "更新成功。";
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(location);
                    data = JSONObject.toJSON(jsonArray);
                }else{
                    code = 0;
                    msg = "更新失败。";
                    data = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                code = 0;
                msg = e.getMessage();
                data = null;
            }
        }else{
            code = -1;
            msg = "参数有误。";
            data = null;
        }


        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }

    @PutMapping("/locations")
    @ResponseBody
    public JSONObject getLocations(@RequestParam(name = "city")String city,@RequestParam(name = "district")String district,@RequestParam(name = "address",required = false)String address,
        @RequestParam("alias")String alias,@RequestParam("amount")Integer amount,@RequestParam("bylocation")boolean bylocation,@RequestParam(value = "longitude",required = false)String longitude,@RequestParam(value = "latitude",required = false)String latitude){
        JSONObject resultJsonObject = new JSONObject();
        int code = 0;
        String msg = null;
        Object data = null;

            if (city == null || "".equals(city) || district == null || "".equals(district) ||address == null || "".equals(address) ||alias == null || "".equals(alias) || amount == null || amount ==0) {
                code = -1;
                msg = "参数有误，操作失败。";
                data = null;
            }else{
                Location location = new Location();
                location.setLocation_city(city);
                location.setLocation_district(district);
                location.setLocation_address(address);
                location.setLocation_amount(amount);
                location.setLocation_alias(alias);
                location.setLocation_available(0);
                if (!bylocation){
                    try {
                        if (locationService.insertByAddress(location)) {
                            code = 1;
                            msg = "增加成功。";
                            data = null;
                        }else{
                            code = 0;
                            msg = "增加失败。";
                            data = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        code = -1;
                        msg = e.getMessage();
                        data = null;
                    }
                }else if(longitude != null && !"".equals(longitude) && latitude != null && !"".equals(latitude)){
                    NavLocation navLocation = new NavLocation();
                    navLocation.setAvailable(location.getLocation_available());
                    navLocation.setAddress(location.getLocation_city() + location.getLocation_district() + location.getLocation_address());
                    navLocation.setLongitude(longitude);
                    navLocation.setLatitude(latitude);
                    navLocation.setName(location.getLocation_alias());
                    try {
                        if (locationService.insertByLocation(location,navLocation)) {
                            code = 1;
                            msg = "增加成功。";
                            data = null;
                        }else{
                        code = 0;
                        msg = "增加失败。";
                        data = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        code = -1;
                        msg = e.getMessage();
                        data = null;
                    }
                }
            }

        resultJsonObject.put("code", code);
        resultJsonObject.put("msg", msg);
        resultJsonObject.put("data", data);
        return resultJsonObject;
    }
}
