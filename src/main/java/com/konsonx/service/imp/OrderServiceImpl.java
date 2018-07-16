package com.konsonx.service.imp;

import com.github.pagehelper.PageHelper;
import com.konsonx.dao.OrderMapper;
import com.konsonx.po.*;
import com.konsonx.service.LocationService;
import com.konsonx.service.OrderService;
import com.konsonx.service.PobkService;
import com.konsonx.service.UserService;
import com.konsonx.utils.CountCost;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service(value = "OrderService")
public class OrderServiceImpl implements OrderService {
    @Resource(name = "OrderMapper")
    private OrderMapper orderMapper;
    @Resource(name = "LocationService")
    private LocationService locationService;
    @Resource(name = "UserService")
    private UserService userService;
    @Resource(name = "PobkService")
    private PobkService pobkService;
    @Resource(name = "CountCost")
    private CountCost countCost;
    public boolean insert(Order order) {
        if (order == null) return false;
        return orderMapper.insertSelective(order)>0?true:false;
    }

    public boolean delete(Integer orderId) {
        return orderMapper.deleteByPrimaryKey(orderId)>0?true:false;
    }

    public boolean update(Order order) {
        if (order == null )return false;
        return orderMapper.updateByPrimaryKeySelective(order)>0?true:false;
    }

    public Order selectById(Integer orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    public List<Order> selectByOrder_lent_location_idAndOrder_revert_location_idAndOrder_user_id(Integer lentLocationId, Integer revertLocationId, Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return orderMapper.findbyOrder_lent_location_idandorder_revert_location_idandorder_user_idOrderbyOrder_create_timedesc(lentLocationId,revertLocationId,userId);
    }

    public List<Order> selectByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return orderMapper.findorderByOrder_create_timedesc();
    }

    public boolean hasUnfinishedOrder(Integer userId) {
        return orderMapper.findFirstbyOrder_user_idandorder_has_finished(userId, 0) != null ? true : false;
    }

    /**
     * 查用户是否完成上一单订单
     * 查询用户余额是否大于零
     * 查该地点是否有可用充电宝
     * 获取一个可用充电宝
     * Location 同步数据-1
     * YunTu同步数据-1
     * pobk 设置出租状态
     * order正式insert
     * @param
     * @return
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean borrow(Integer userId, Integer locationId) throws Exception{
        Order order = new Order();
        order.setOrder_user_id(userId);
        order.setOrder_lent_location_id(locationId);

        /**
         * 查询用户余额状态，订单状态
         */
        User user = userService.selectById(userId);
        if (user == null) throw new Exception("用户不存在。");
        if (hasUnfinishedOrder(user.getUser_id()))throw new Exception("存在未完成的订单。");
        if (user.getUser_balance() <= 0)throw new Exception("余额低于零元。");
        /**
         * 查询投放点可用充电宝数量
         */
        Location location = locationService.selectByIdForUpdate(order.getOrder_lent_location_id());
        if (location == null) throw new Exception("投放点不存在。");
        if (location.getLocation_available()<=0) throw new Exception("投放点没有可用充电宝。");
        /**
         * 找出可用充电宝一台
         */
        Pobk pobk = pobkService.selectOneAvailableByLocationId(location.getLocation_id());
        if (pobk == null)  throw new Exception("充电宝不可用。");
        pobk = pobkService.selectByIdForUpdate(pobk.getPobk_id());
        /**
         * 执行租借充电宝的业务
         * 投放点可用数-1
         * 云图-1
         * 充电宝出租Lent状态
         * 订单写入。
         */
        if (location.getLocation_available() - 1 >=0){
            pobk.setPobk_status("lent");
            if (!pobkService.update(pobk))throw new Exception("充电宝租借失败。");
            order.setOrder_pobk_id(pobk.getPobk_id());
            order.setOrder_create_time(new Date());
            order.setOrder_has_finished(0);
            order.setOrder_revert_location_id(location.getLocation_id());
            if (insert(order)){
                location.setLocation_available(location.getLocation_available() - 1);
                NavLocation navLocation = new NavLocation();
                navLocation.setId(location.getLocation_yun_id());
                navLocation.setAvailable(location.getLocation_available());
                if (!locationService.update(location,navLocation)) {
                    throw new Exception("投放点出错。");
                }else {
                    return true;
                }
            }else{
                throw new Exception("生成订单失败。");
            }
        }else
           throw new Exception("投放点充电宝不足");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean revert(Integer orderId,Integer revertLocationId) throws Exception {
        /**
         * 检查订单，保证订单处于未完成状态。
         */
        Order order = selectById(orderId);
        if (order == null) throw new Exception("订单不存在。");
        if (order.getOrder_has_finished() == 1 ) throw new Exception("订单已完成。");
        /**
         * 检查归还点状态
         */
        order.setOrder_revert_location_id(revertLocationId);
        Location location = locationService.selectByIdForUpdate(revertLocationId);
        if (location == null) throw new Exception("归还点不存在。");
        if (location.getLocation_available() == location.getLocation_amount()) throw new Exception("归还点已满。");
        /**
         * 检查用户状态
         */
        User user = userService.selectById(order.getOrder_user_id());
        if (user == null) throw new Exception("用户不存在。");
        order.setOrder_finish_time(new Date());
        float cost = countCost.count(order.getOrder_create_time(),order.getOrder_finish_time());
        order.setOrder_cost(cost);
        if (user.getUser_balance() - cost <0){
            throw new Exception("余额不足。");
        }

        /**
         * 扣钱
         */
        user.setUser_balance(user.getUser_balance() - cost);
        if (!userService.update(user))throw new Exception("扣款失败。");
        /**
         * 设置充电宝状态
         */
        Pobk pobk = pobkService.selectById(order.getOrder_pobk_id());
        if (pobk == null) throw new Exception("充电宝不存在。");
        pobk = pobkService.selectByIdForUpdate(pobk.getPobk_id());
        pobk.setPobk_status("available");
        if (!pobkService.update(pobk))throw new Exception("无法归还充电宝。");
        /**
         * 完成订单
         */
        order.setOrder_has_finished(1);
        if (!update(order))throw new Exception("无法完成订单。");
        /**
         * 投放点信息更新
         */
        location.setLocation_available(location.getLocation_available() + 1);
        NavLocation navLocation = new NavLocation();
        navLocation.setId(location.getLocation_yun_id());
        navLocation.setAvailable(location.getLocation_available());
        if (!locationService.update(location,navLocation))throw new Exception("无法归还至投放点。");

        /**
         * 成功。
         */
        return true;
    }


}
