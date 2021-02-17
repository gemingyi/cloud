package com.example.killserver.service.impl;

import com.example.killserver.dao.KillGoodsMapper;
import com.example.killserver.model.KillGoods;
import com.example.killserver.service.IKillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class KillGoodsServiceImpl implements IKillGoodsService {

    @Autowired
    private KillGoodsMapper killGoodsMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public int addKillGoods(KillGoods killGoods) {
        // 保存秒杀商品信息
        int result = killGoodsMapper.insert(killGoods);
        // redis中保存秒杀商品信息
        if (result > 0) {
            redisTemplate.opsForHash().put("kill:goods", String.valueOf(killGoods.getId()), killGoods);
        }
        return result;
    }

    @Override
    public KillGoods getKillGoods(Integer id) {
        KillGoods killGoods = (KillGoods) redisTemplate.opsForHash().get("kill:goods", String.valueOf(id));
        if (killGoods == null) {
            killGoods = killGoodsMapper.selectById(id);
            redisTemplate.opsForHash().put("kill:goods", String.valueOf(id), killGoods);
        }
        return killGoods;
    }

}
