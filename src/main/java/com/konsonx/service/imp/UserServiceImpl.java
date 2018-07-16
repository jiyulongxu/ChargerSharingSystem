package com.konsonx.service.imp;

import com.github.pagehelper.PageHelper;
import com.konsonx.dao.UserMapper;
import com.konsonx.po.User;
import com.konsonx.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service(value = "UserService")
public class UserServiceImpl implements UserService {
    @Resource(name = "UserMapper")
    private UserMapper userMapper;

    public boolean insert(User user) {
        if (user == null )return false;
        return  userMapper.insertSelective(user)>0?true:false;
    }

    public boolean delete(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId)>0?true:false;
    }

    public boolean update(User user) {
        if (user == null )return false;
        return userMapper.updateByPrimaryKeySelective(user)>0?true:false;
    }

    public User selectById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public User selectByPhone(String userPhone) {
        return userMapper.findbyuser_phone(userPhone);
    }

    public boolean login(String phoneNumber, String password) {
        if (password == null || phoneNumber == null)return false;
        User  user = userMapper.findbyuser_phone(phoneNumber);
        if (user == null) return false;
        return user.getUser_password().equals(password);
    }

    public List<User> selectByPhone(String phoneNumber, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userMapper.findbyUser_phonecontaining(phoneNumber);
    }
    public List<User> selectByAlias(String alias, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userMapper.findbyUser_aliascontaining(alias);
    }
    public List<User> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return userMapper.findorderByuser_iddesc();
    }

    public boolean recharge(Integer userId, Float money) {
        if (money<0)return false;
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null)return false;
        user.setUser_balance(user.getUser_balance() + money);
        user.setUser_password(null);
        user.setUser_phone(null);
        return userMapper.updateByPrimaryKeySelective(user)>0?true:false;
    }

    /**
     *
     * @param userId
     * @param cost 正数
     * @return
     */
    public boolean deduct(Integer userId, Float cost) {
        if (cost<0)return false;
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null)return false;
        if (user.getUser_balance() - cost < 0) return false;
        user.setUser_balance(user.getUser_balance() - cost);
        user.setUser_password(null);
        user.setUser_phone(null);
        return userMapper.updateByPrimaryKeySelective(user)>0?true:false;
    }
}
