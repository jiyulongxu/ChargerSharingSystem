package com.konsonx.service.imp;

import com.github.pagehelper.PageHelper;
import com.konsonx.dao.PobkMapper;
import com.konsonx.po.Location;
import com.konsonx.po.Pobk;
import com.konsonx.service.LocationService;
import com.konsonx.service.PobkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service(value = "PobkService")
public class PobkServiceImpl implements PobkService {
    @Resource(name = "PobkMapper")
    private PobkMapper pobkMapper;
    @Resource(name = "LocationService")
    private LocationService locationService;
    @Transactional
    public boolean insert(Pobk pobk) throws Exception{
        if (pobk == null) return false;
        if (pobkMapper.insertSelective(pobk)>0){
            return locationService.addPowerBank(pobk.getPobk_location_id());
        }else{
            return false;
        }
    }
    @Transactional
    public boolean insert(Pobk pobk, Integer number) throws Exception {
        boolean flag = true;
        if (number == null || number <1){
            return false;
        }
        Location location = locationService.selectById(pobk.getPobk_location_id());
        if (location == null) return false;

        if (location.getLocation_amount() - location.getLocation_available() < number) return false;
        for (int i = 0; i < number && flag; i++ ){
            flag = insert(pobk);
        }
        if (!flag){
            throw new Exception("投放失败");
        }else {
            return true;
        }
    }

    @Transactional
    public boolean delete(Integer pobkId) throws Exception{
        Pobk pobk = selectById(pobkId);
        if (pobkMapper.deleteByPrimaryKey(pobkId)>0){
            return locationService.deductPowerbank(pobk.getPobk_location_id());
        }else{
            return false;
        }
    }

    public boolean update(Pobk pobk) {
        if (pobk == null) return false;
        return pobkMapper.updateByPrimaryKeySelective(pobk)>0?true:false;
    }
    @Transactional
    public boolean updatewithLocation(Pobk pobk) throws Exception {
        if (pobk == null) return false;
        Pobk oldPobk = selectById(pobk.getPobk_id());
        if (oldPobk == null) return false;
        if (oldPobk.getPobk_location_id() == pobk.getPobk_location_id()){
            return pobkMapper.updateByPrimaryKeySelective(pobk)>0?true:false;
        }else{
            if (pobkMapper.updateByPrimaryKeySelective(pobk)>0){
                if (locationService.deductPowerbank(oldPobk.getPobk_location_id())) {
                     return locationService.addPowerBank(pobk.getPobk_location_id());
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    public Pobk selectById(Integer pobkId) {
        return pobkMapper.selectByPrimaryKey(pobkId);
    }

    public Pobk selectByIdForUpdate(Integer pobkId) {
        return pobkMapper.selectByPrimaryKeyForUpdate(pobkId);
    }

    public List<Pobk> selectByLocation(Integer locationId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return pobkMapper.findbyPobk_location_id(locationId);
    }

    public Pobk selectOneAvailableByLocationId(Integer locationId) {
       List<Pobk> resultList =  pobkMapper.findbyPobk_location_idandpobk_status(locationId,"available");
       if (resultList.isEmpty()){
           return null;
       }else{
           return resultList.get(0);
       }
    }

    public List<Pobk> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return pobkMapper.findorderByPobk_iddesc();
    }
}
