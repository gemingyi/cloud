package com.example.killserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commons.result.RestResult;
import com.example.killserver.dao.mapper.KillGoodsMapper;
import com.example.killserver.dao.mapper.KillOrderMapper;
import com.example.killserver.dao.entity.KillGoods;
import com.example.killserver.dao.entity.KillOrder;
import com.example.killserver.mq.constans.ExchangeConstant;
import com.example.killserver.mq.constans.RoutingKeyConstant;
import com.example.killserver.service.IKillOrderService;
import com.example.pluginredis.util.RedisLock;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class KillOrderServiceImpl implements IKillOrderService {

    @Autowired
    private KillGoodsMapper killGoodsMapper;
    @Autowired
    private KillOrderMapper killOrderMapper;

    @Autowired
    private RedisLock redisLock;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public RestResult<Object> submitOrder(Integer killGoodsId, Integer userId, Integer businessId) {
        // 获取redis锁
        String identifier = redisLock.acquireLock("kill:" + killGoodsId, 1);
        if (!StringUtils.isEmpty(identifier)) {
            //获取redis秒杀商品库存
            KillGoods killGoods = (KillGoods) redisTemplate.boundHashOps("kill:goods").get(String.valueOf(killGoodsId));
            if (killGoods == null) {
                return RestResult.failure("商品秒杀完结！" + userId);
            }
            if (killGoods.getCount() == 0) {
                return RestResult.failure("商品库存不足！" + userId);
            }
            //更新redis中秒杀商品库存
            killGoods.setCount(killGoods.getCount() - 1);
            killGoods.setSale(killGoods.getSale() + 1);
            redisTemplate.boundHashOps("kill:goods").put(String.valueOf(killGoodsId), killGoods);

            //保存订单信息
            this.createOrder(killGoodsId, userId, businessId);

            //释放redis锁
            redisLock.releaseLock("kill:" + killGoodsId, identifier);
            return RestResult.success("用户编号:" + userId + "抢到了");
        }
        return RestResult.failure("用户编号:" + userId + "没抢到");
    }

    @Override
    public void overTimeCloseOrder(String orderNumber) {
        KillOrder killOrder = (KillOrder) redisTemplate.opsForHash().get("kill:order", orderNumber);
        if (killOrder != null) {
            //删除redis中秒杀订单
            redisTemplate.opsForHash().delete("kill:order", orderNumber);
            //更新用户订单状态(超时关闭)
            KillOrder updateKillOrder = new KillOrder();
            updateKillOrder.setId(killOrder.getId());
            updateKillOrder.setStatus("close");
            killOrderMapper.updateById(updateKillOrder);

            //回退redis中秒杀商品库存
            KillGoods killGoods = (KillGoods) redisTemplate.boundHashOps("kill:goods").get(String.valueOf(killOrder.getKillGoodsId()));
            if (killGoods != null) {
                killGoods.setCount(killGoods.getCount() + 1);
                killGoods.setSale(killGoods.getSale() - 1);
                redisTemplate.boundHashOps("kill:goods").put(String.valueOf(killOrder.getKillGoodsId()), killGoods);
            }
        }
    }

    @Override
    public void PaidSaveOrder(String orderNumber) {
        KillOrder killOrder = (KillOrder) redisTemplate.opsForHash().get("kill:order", orderNumber);
        if (killOrder != null) {
            // 删除redis中订单信息
            redisTemplate.opsForHash().delete("kill:order", orderNumber);
            // 更新订单状态
            KillOrder updateKillOrder = new KillOrder();
            updateKillOrder.setId(killOrder.getId());
            updateKillOrder.setStatus("paid");
            updateKillOrder.setPayTime(new Date());
            killOrderMapper.updateById(updateKillOrder);

            // 更新数据库中秒杀商品库存（可异步保存）
            KillGoods killGoods = killGoodsMapper.selectById(killOrder.getKillGoodsId());
            killGoods.setCount(killGoods.getCount() - 1);
            killGoods.setSale(killGoods.getSale() + 1);
            killGoodsMapper.updateById(killGoods);
        }
    }

    @Override
    public List<KillOrder> findKillOrderByUser(Integer userId) {
        QueryWrapper<KillOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        return killOrderMapper.selectList(wrapper);
    }

    @Override
    public KillOrder getOrderByNumber(String orderNumber) {
        KillOrder killOrder = (KillOrder) redisTemplate.opsForHash().get("kill:order", orderNumber);
        if (killOrder == null) {
            QueryWrapper<KillOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_number", orderNumber);
            killOrder = killOrderMapper.selectOne(queryWrapper);
            redisTemplate.opsForHash().put("kill:order", killOrder.getOrderNumber(), killOrder);
        }
        return killOrder;
    }


    //----------------------private------------------------

    /**
     * 保存秒杀订单
     */
    private void createOrder(Integer killGoodsId, Integer userId, Integer businessId) {
        KillOrder killOrder = new KillOrder();
        killOrder.setUserId(userId);
        killOrder.setKillGoodsId(killGoodsId);
        killOrder.setBusinessId(businessId);
        killOrder.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        killOrder.setOrderMoney(BigDecimal.valueOf(100.00));
        killOrder.setStatus("new");
        killOrder.setAdder("测试收货地址111");
        killOrder.setCreateTime(new Date());
        //数据库保存订单信息 可异步保存
        killOrderMapper.insert(killOrder);

        //redis保存订单信息
        redisTemplate.opsForHash().put("kill:order", killOrder.getOrderNumber(), killOrder);

        //
        rabbitTemplate.convertAndSend(ExchangeConstant.TIMEOUT_ORDER_EXCHANGE, RoutingKeyConstant.TIMEOUT_ORDER_KEY, killOrder, message -> message);
    }

}
