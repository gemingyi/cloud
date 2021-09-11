package com.example.killserver.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.pluginmysql.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KillGoods extends BaseModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     *
     */
    private String title;

    /**
     * 原价格
     */
    private BigDecimal price;

    /**
     * 秒杀价格
     */
    private BigDecimal costPrice;

    /**
     * 秒杀数量
     */
    private Integer count;

    /**
     * 已售数量
     */
    private Integer sale;

    /**
     * 版本号
     */
    private Integer version;

}