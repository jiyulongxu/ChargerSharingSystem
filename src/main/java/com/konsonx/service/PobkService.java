package com.konsonx.service;

import com.konsonx.po.Pobk;

import java.util.List;

public interface PobkService {
    public boolean insert(Pobk pobk)throws Exception;
    public boolean insert(Pobk pobk,Integer number) throws Exception;
    public boolean delete(Integer pobkId) throws Exception;
    public boolean update(Pobk pobk);

    /**
     * 此方法包含充电宝调动时location中available的改动。
     * @param pobk
     * @return
     * @throws Exception
     */
    public boolean updatewithLocation(Pobk pobk) throws Exception;
    public Pobk selectById(Integer pobkId);
    public Pobk selectByIdForUpdate(Integer pobkId);
    public List<Pobk> selectByLocation(Integer locationId, Integer pageNum, Integer pageSize);
    public Pobk selectOneAvailableByLocationId(Integer locationId);
    public List<Pobk> selectByPage(Integer pageNum, Integer pageSize);
}
