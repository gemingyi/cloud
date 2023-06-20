package com.example.killserver.service;

import com.example.commons.result.RestResult;
import com.example.killserver.dao.entity.KillOrder;

import java.util.List;

public interface IKillOrderService {

    /**
     * 提交秒杀订单
     * @param killGoodsId
     * @param userId
     * @return
     */
    RestResult<Object> submitOrder(Integer killGoodsId, Integer userId, Integer businessId);

    /**
     * 支付超时关闭订单,回退库存(定时任务处理)
     */
    void overTimeCloseOrder(String orderNumber);

    /**
     * 支付成功保存订单
     * @param orderNumber
     */
    void PaidSaveOrder(String orderNumber);

    /**
     * 查询订单列表
     * @param userId
     * @return
     */
    List<KillOrder> findKillOrderByUser(Integer userId);

    /**
     * 从redis获取获取订单信息
     * @param orderNumber
     * @return
     */
    KillOrder getOrderByNumber(String orderNumber);

}
