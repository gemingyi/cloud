package com.example.userserver.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.pluginmysql.model.BaseModel;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
public class Test {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "test_id", type = IdType.AUTO)
    private Integer testId;

    /**
     * 交易号
     */
    private String tradeNo;

    /**
     * 名称
     */
    private String name;

    /**
     * 总价
     */
    private BigDecimal price;


    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Test{" +
        "testId=" + testId +
        ", name=" + name +
        ", price=" + price +
        "}";
    }
}
