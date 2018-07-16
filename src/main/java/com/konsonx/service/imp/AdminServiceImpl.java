package com.konsonx.service.imp;

import com.github.pagehelper.PageHelper;
import com.konsonx.dao.AdminMapper;
import com.konsonx.po.Admin;
import com.konsonx.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service(value = "AdminService")
public class AdminServiceImpl implements AdminService {

    @Resource(name = "AdminMapper")
    private AdminMapper adminMapper;

    public boolean insert(Admin admin) {
        return adminMapper.insertSelective(admin)>0?true:false;
    }

    public boolean delete(Integer id) {
        return adminMapper.deleteByPrimaryKey(id)>0?true:false;
    }

    public boolean update(Admin admin) {
        return adminMapper.updateByPrimaryKeySelective(admin)>0?true:false;
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public Admin selectByAccount(String account){
        return  adminMapper.findByAdmin_account(account);
    }

    public boolean login(String account, String password) {
        if (account == null || password == null)return false;
        Admin admin = adminMapper.findByAdmin_account(account);
        if(admin == null)return false;
        return admin.getAdmin_password().equals(password);
    }

    public List<Admin> selectByAccount(String account, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return adminMapper.findByadmin_accountcontaining(account);
    }

    public List<Admin> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return adminMapper.findorderByAdmin_iddesc();
    }
}
