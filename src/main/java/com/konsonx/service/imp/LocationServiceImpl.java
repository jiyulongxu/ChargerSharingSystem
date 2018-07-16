package com.konsonx.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.konsonx.dao.LocationMapper;
import com.konsonx.po.Location;
import com.konsonx.po.NavLocation;
import com.konsonx.service.LocationService;
import com.konsonx.utils.YunTu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service(value = "LocationService")
public class LocationServiceImpl implements LocationService {
    @Resource(name = "LocationMapper")
    private LocationMapper locationMapper;
    @Resource(name = "YunTu")
    private YunTu yunTu;


    @Transactional
    public boolean insertByLocation(Location location, NavLocation navLocation) throws Exception {
        if (location == null || navLocation == null ) return false;
        navLocation.setAddress(location.getLocation_city() + location.getLocation_district() + location.getLocation_address());
        navLocation.setName(location.getLocation_alias());
        navLocation.setAvailable(location.getLocation_available());
        JSONObject jsonObject = yunTu.insertByLocation(navLocation);
        if (!"1".equals(jsonObject.getString("status"))){
            throw new Exception("云图数据插入失败。" + jsonObject.getString("info"));
        }else{
            location.setLocation_yun_id(jsonObject.getInteger("_id"));
            return locationMapper.insertSelective(location)>0;
        }
    }
    @Transactional
    public boolean insertByAddress(Location location)throws Exception {
        if (location == null) return false;
        NavLocation navLocation = new NavLocation();
        navLocation.setAvailable(location.getLocation_available());
        navLocation.setName(location.getLocation_alias());
        navLocation.setAddress(location.getLocation_city() + location.getLocation_district() + location.getLocation_address());
        JSONObject jsonObject = yunTu.insertByAddress(navLocation);
        System.out.println(jsonObject);
        if (!"1".equals(jsonObject.getString("status"))){
            throw new Exception("云图数据插入失败。" + jsonObject.getString("info"));
        }else{
            location.setLocation_yun_id(jsonObject.getInteger("_id"));
            return locationMapper.insertSelective(location)>0;
        }
    }


    @Transactional
    public boolean delete(Integer locationId)throws Exception {
        Location location = locationMapper.selectByPrimaryKey(locationId);
        if (locationMapper.deleteByPrimaryKey(locationId)>0?true:false){
            JSONObject jsonObject = yunTu.delete(new String[]{String.valueOf(location.getLocation_yun_id())});
            if (!"1".equals(jsonObject.getString("success"))){
                throw new Exception("云图数据删除失败。" + jsonObject.getString("info"));
            }else{
                return true;
            }
        }else {
            return false;
        }
    }

    @Transactional
    public boolean update(Location location,NavLocation navLocation) throws Exception {
        if (location == null || navLocation == null )return false;
        if ( locationMapper.updateByPrimaryKeySelective(location)>0?true:false){
            JSONObject jsonObject = yunTu.update(navLocation);
            if (!"1".equals(jsonObject.getString("status"))){
                throw new Exception("云图数据更新失败。" + jsonObject.getString("info"));
            }else {
                return true;
            }
        }else {
            return false;
        }
    }
    @Transactional
    public boolean addPowerBank(Integer locationId) throws Exception {
        Location location = selectById(locationId);
        if (location.getLocation_available() + 1 <= location.getLocation_amount()){
            location.setLocation_available(location.getLocation_available() + 1);
            NavLocation navLocation = new NavLocation();
            navLocation.setAvailable(location.getLocation_available());
            navLocation.setId(location.getLocation_yun_id());
            return update(location,navLocation);
        }else{
            return false;
        }
    }

    @Transactional
    public boolean deductPowerbank(Integer locationId) throws Exception {
        Location location = selectById(locationId);
        if (location.getLocation_available() - 1 >= 0){
            location.setLocation_available(location.getLocation_available() - 1);
            NavLocation navLocation = new NavLocation();
            navLocation.setAvailable(location.getLocation_available());
            navLocation.setId(location.getLocation_yun_id());
            return update(location,navLocation);
        }else{
            return false;
        }
    }

    public Location selectById(Integer locationId) {
        return locationMapper.selectByPrimaryKey(locationId);
    }

    public Location selectByYunId(Integer yunId) {
        return locationMapper.findOnebyLocation_yun_id(yunId);
    }

    public Location selectByIdForUpdate(Integer locationId) {
        return locationMapper.findOneByLocation_idForUpdate(locationId);
    }

    /**
     *
     * @param city 可以为null
     * @param district 可以为null
     * @param address 可以为null
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<Location> selectByCityAndDistrictAndAddress(String city, String district, String address, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return locationMapper.findbyLocation_cityandlocation_districtandlocation_addresscontaining(city,district,address);
    }

    public List<Location> selectByAlias(String alias, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return locationMapper.findbylocation_aliascontaining(alias);
    }

    public List<Location> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return locationMapper.findorderBylocation_iddesc();
    }


}
