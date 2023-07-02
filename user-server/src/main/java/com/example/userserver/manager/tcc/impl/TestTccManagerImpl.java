package com.example.userserver.manager.tcc.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.userserver.dao.mapper.TestMapper;
import com.example.userserver.manager.tcc.ITestTccManager;
import com.example.userserver.dao.entity.Test;
import io.seata.rm.tcc.api.BusinessActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class TestTccManagerImpl implements ITestTccManager {

    @Autowired
    private TestMapper testMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean prepareSaveTest(BusinessActionContext businessActionContext, String tradeNo, String name, BigDecimal price) {
        Test test = new Test();
        test.setTradeNo(tradeNo);
        test.setName(name);
        test.setPrice(price);
        int result = testMapper.insert(test);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean commitSaveTest(BusinessActionContext businessActionContext) {
        String tradeNo = (String) businessActionContext.getActionContext("tradeNo");
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean cancelSaveTest(BusinessActionContext businessActionContext) {
        String tradeNo = (String) businessActionContext.getActionContext("tradeNo");
        QueryWrapper<Test> wrapper = new QueryWrapper<>();
        wrapper.eq("trade_no", tradeNo);
        int result = testMapper.delete(wrapper);
        return result > 0;
    }

}
