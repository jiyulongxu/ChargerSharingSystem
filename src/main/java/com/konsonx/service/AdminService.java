package com.konsonx.service;

import com.konsonx.po.Admin;

import java.util.List;

public interface AdminService {
    public boolean insert(Admin admin);
    public boolean delete(Integer id);
    public boolean update(Admin admin);
    public Admin selectById(Integer id);
    public boolean login(String account,String password);
    public List<Admin> selectByAccount(String account, Integer pageNum, Integer pageSize);
    public Admin selectByAccount(String account);
    /**
     *
     * @param pageNum 从1开始
     * @param pageSize 如果pageSize 为0 则不分页
     * @return list
     */
    public List<Admin> selectByPage(Integer pageNum,Integer pageSize);
}
