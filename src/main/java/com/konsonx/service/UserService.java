package com.konsonx.service;

import com.konsonx.po.User;

import java.util.List;

public interface UserService {
    public boolean insert(User user);
    public boolean delete(Integer userId);
    public boolean update(User user);
    public User selectById(Integer userId);
    public User selectByPhone(String userPhone);
    public boolean login(String phoneNumber,String password);
    public List<User> selectByPhone(String phoneNumber,Integer pageNum,Integer pageSize);
    public List<User> selectByAlias(String phoneAlias,Integer pageNum,Integer pageSize);
    /**
     *
     * @param pageNum 从1开始
     * @param pageSize 如果pageSize 为0 则不分页
     * @return list
     */
    public List<User> selectByPage(Integer pageNum,Integer pageSize);

    public boolean recharge(Integer userId,Float money);
    public boolean deduct(Integer userId,Float cost);
}
