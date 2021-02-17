package com.example.killserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.pluginmysql.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class KillOrder extends BaseModel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 秒杀商品id
     */
    private Integer killGoodsId;

    /**
     * 商家id
     */
    private Integer businessId;

    /**
     * 流水号
     */
    private String orderNumber;

    /**
     * 订单金额
     */
    private BigDecimal orderMoney;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 收货地址
     */
    private String adder;

}