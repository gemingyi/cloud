package com.example.userserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.commons.IAbstractService;
import com.example.commons.result.RestResult;
import com.example.commons.result.ResultCode;
import com.example.userserver.feign.TestFeign;
import com.example.userserver.dao.mapper.TestMapper;
import com.example.userserver.manager.tcc.ITestTccManager;
import com.example.userserver.dao.entity.Test;
import com.example.userserver.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service("testServiceImpl")
public class TestServiceImpl implements IAbstractService<Test>, ITestService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private TestFeign testFeign;

    @Autowired
    private ITestTccManager testTccManager;


    @Override
    public int create(Test test) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int updateById(Test test) {
        return 0;
    }

    @Override
    public List<Test> listAll(Test test) {
        return this.testMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public Test getById(Integer id) {
        try {
            int i = new Random().nextInt(1);
            Thread.sleep((i + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.testMapper.selectById(id);
    }

    @Override
    public Test getTestById(Integer id) {
        try {
            int i = new Random().nextInt(1);
            Thread.sleep((i + 1) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.testMapper.selectById(id);
    }


    @GlobalTransactional(name = "seata-test", rollbackFor = RuntimeException.class)
    @Override
    public void seataTest() {

        Test test = new Test();
        test.setTradeNo(String.valueOf(System.currentTimeMillis()));
        test.setName("userTest");
        test.setPrice(BigDecimal.ZERO);
        this.testMapper.insert(test);

        RestResult<Object> restResult = testFeign.seataTest();
        if (!ResultCode.SUCCESS.code().equals(restResult.getCode())) {
            throw new RuntimeException();
        }
        System.out.println(restResult);
    }

    @GlobalTransactional(name = "seata-test2", rollbackFor = RuntimeException.class)
    @Override
    public void seataTest2() {
        String tradeNo = String.valueOf(System.currentTimeMillis());
        testTccManager.prepareSaveTest(null, tradeNo, "userTest2", BigDecimal.ZERO);
        throw new RuntimeException();
    }

}
