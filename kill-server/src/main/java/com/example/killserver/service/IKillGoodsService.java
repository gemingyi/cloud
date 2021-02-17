package com.example.killserver.service;


import com.example.killserver.model.KillGoods;

public interface IKillGoodsService {

    /**
     * 添加秒杀商品
     * @return
     */
    int addKillGoods(KillGoods killGoods) ;

    /**
     * 获取商品详情
     * @param id
     * @return
     */
    KillGoods getKillGoods(Integer id);

}
