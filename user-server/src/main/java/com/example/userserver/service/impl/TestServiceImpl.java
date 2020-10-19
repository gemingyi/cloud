package com.example.userserver.service.impl;

import com.example.commons.IAbstractService;
import com.example.userserver.client.TestClient;
import com.example.userserver.dao.TestMapper;
import com.example.userserver.model.Test;
import com.example.userserver.service.ITestService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TestServiceImpl implements IAbstractService<Test>, ITestService {

    @Autowired
    private TestMapper testMapper;
    @Autowired
    private TestClient testClient;


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
        return null;
    }

    @Override
    public Test getById(Integer id) {
        return null;
    }


    @Override
    @GlobalTransactional(name = "seata-test",rollbackFor = RuntimeException.class)
    public void seataTest() {

        Test test = new Test();
        test.setName("userTest");
        test.setPrice(BigDecimal.ZERO);
        this.testMapper.insert(test);

        testClient.seataTest();
    }
}
