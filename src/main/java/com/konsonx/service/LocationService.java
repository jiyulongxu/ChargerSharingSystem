package com.konsonx.service;

import com.konsonx.po.Location;
import com.konsonx.po.NavLocation;

import java.util.List;

public interface LocationService {
    public boolean insertByLocation(Location location, NavLocation navLocation) throws Exception;
    public boolean insertByAddress(Location location) throws Exception;
    public boolean delete(Integer locationId) throws Exception;
    public boolean update(Location location,NavLocation navLocation)throws Exception;
    public boolean addPowerBank(Integer locationId)throws Exception;
    public boolean deductPowerbank(Integer locationId)throws Exception;
    public Location selectById(Integer locationId);
    public Location selectByYunId(Integer yunId);
    public Location selectByIdForUpdate(Integer locationId);
    public List<Location> selectByCityAndDistrictAndAddress(String city,String district,String address, Integer pageNum, Integer pageSize);
    public List<Location> selectByAlias(String alias, Integer pageNum, Integer pageSize);
    /**
     *
     * @param pageNum 从1开始
     * @param pageSize 如果pageSize 为0 则不分页
     * @return list
     */
    public List<Location> selectByPage(Integer pageNum, Integer pageSize);
}
