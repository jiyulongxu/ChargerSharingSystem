package com.konsonx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.konsonx.po.NavLocation;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(value = "YunTu")
public class YunTu {
    private String key = "**";
    private String tableId="**";

    public static String doPost(String url, Map<String, String> params) {
        URL u = null;
        HttpURLConnection con = null;
        // 构建请求参数
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, String> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        System.out.println("send_url:" + url);
        System.out.println("send_data:" + sb.toString());
        // 尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            //// POST 只能为大写，严格限制，post会不识别
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        // 读取返回内容
        StringBuffer buffer = new StringBuffer();
        try {
            //一定要有返回值，否则无法把请求发送给server端。
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }


    /**
     *
     * @param location
     * @return JsonObject
     * status 返回状态 取值规则：1：成功；0：失败
       info   status = 1，info返回“ok”
        _id   成功创建的数据id
     */
    public JSONObject insertByLocation(NavLocation location){
        String url="http://yuntuapi.amap.com/datamanage/data/create";
        Map<String,String> params = new HashMap<String, String>();
        params.put("key",key);
        params.put("tableid",tableId);
        /**
         * loctype 定位方式
         设置是以请求中的经纬度参数（_loca
         tion）还是地址参数（_address）
         来计算最终的坐标值。
         可选值：
         可选 1
         高德云图，零成本定制你的私有地图
         1：经纬度；格式示例：104.394729,
         31.125698
         2：地址；标准格式示例：北京市朝阳
         区望京阜通东大街6号院3号楼
         */
        params.put("loctype","1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_name",location.getName());
        jsonObject.put("_location",location.getLongitude() + "," + location.getLatitude());
        jsonObject.put("available",location.getAvailable());
        if (location.getAddress() != null && !"".equals(location.getAddress()) && location.getAddress().length()>2){
            jsonObject.put("_address",location.getAddress());
        }
        params.put("data",jsonObject.toString());
        System.out.println(params);
        JSONObject resultObject = JSON.parseObject(doPost(url,params));
        return resultObject;
    }

    /**
     *
     * @param location
     * @return JsonObject
     * status 返回状态 取值规则：1：成功；0：失败
    info   status = 1，info返回“ok”
    _id   成功创建的数据id
     */
    public JSONObject insertByAddress(NavLocation location){
        String url="http://yuntuapi.amap.com/datamanage/data/create";
        Map<String,String> params = new HashMap<String, String>();
        params.put("key",key);
        params.put("tableid",tableId);
        params.put("loctype","2");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_name",location.getName());
        jsonObject.put("_address",location.getAddress());
        jsonObject.put("available",location.getAvailable());
        params.put("data",jsonObject.toString());
        JSONObject resultObject = JSON.parseObject(doPost(url,params));
        return resultObject;
    }

    /**
     *
     * @param location
     * @return
     * status 返回状态 值为0或1 1：成功；0：失败
       info  返回的状态信息 status = 1，info返回“ok”
     */
    public JSONObject update(NavLocation location){
        String url="http://yuntuapi.amap.com/datamanage/data/update";
        Map<String,String> params = new HashMap<String, String>();
        params.put("key",key);
        params.put("tableid",tableId);
        params.put("loctype","1");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_id",location.getId());
        if (location.getAddress() != null && !"".equals(location.getAddress()) && location.getAddress().length()>2){
            jsonObject.put("_address",location.getAddress());
        }
        if (location.getAvailable() != null && location.getAvailable()>= 0){
            jsonObject.put("available",location.getAvailable());
        }
        if (location.getLongitude() != null && location.getLatitude() != null && !"".equals(location.getLongitude()) && !"".equals(location.getLatitude()) && location.getLatitude().length()>1 && location.getLongitude().length()>1){
            jsonObject.put("_location",location.getLongitude() + "," + location.getLatitude());
        }
        if (location.getName() != null && !"".equals(location.getName()) && location.getName().length()>2){
            jsonObject.put("_name",location.getName());
        }
        params.put("data",jsonObject.toString());
        JSONObject resultObject = JSON.parseObject(doPost(url,params));
        return resultObject;
    }

    /**
     *
     * @param ids
     * @return
     * {"fail":0,"success":2,"infocode":"10000","info":"OK","status":1}
     */
    public JSONObject delete(String[] ids){
        String url="http://yuntuapi.amap.com/datamanage/data/delete";
        Map<String,String> params = new HashMap<String, String>();
        params.put("key",key);
        params.put("tableid",tableId);
        if (ids.length == 1){
            params.put("ids",ids[0]);
        }else if (ids.length > 1){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ids[0]);
            for (int i = 1;i < ids.length;i++){
                stringBuffer.append(",");
                stringBuffer.append(ids[i]);
            }
            params.put("ids",stringBuffer.toString());
        }
        JSONObject resultObject = JSON.parseObject(doPost(url,params));
        return resultObject;
    }



}
