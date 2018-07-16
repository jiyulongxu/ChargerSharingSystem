package com.konsonx.service;

import com.konsonx.po.Order;

import java.util.List;

public interface OrderService {
    public boolean insert(Order order) ;
    public boolean delete(Integer orderId);
    public boolean update(Order order);
    public Order selectById(Integer orderId);
    public List<Order> selectByOrder_lent_location_idAndOrder_revert_location_idAndOrder_user_id(Integer lentLocationId,Integer revertLocationId,Integer userId, Integer pageNum, Integer pageSize);
    public List<Order> selectByPage( Integer pageNum, Integer pageSize);
    public boolean hasUnfinishedOrder(Integer userId);
    public boolean borrow(Integer userId,Integer locationId)throws Exception;
    public boolean revert(Integer orderId,Integer revertLocationId) throws Exception;
}
