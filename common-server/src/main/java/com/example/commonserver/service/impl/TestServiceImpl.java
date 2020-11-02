package com.example.commonserver.service.impl;

import com.example.commons.IAbstractService;
import com.example.commons.exceptionHandle.exceptions.IllegalParameterException;
import com.example.commons.exceptionHandle.exceptions.InternalServerException;
import com.example.commonserver.dao.TestMapper;
import com.example.commonserver.model.Test;
import com.example.commonserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceImpl implements IAbstractService<Test>, ITestService {

    @Autowired
    private TestMapper testMapper;


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
    @Transactional
    public void seataTest() {
        Test test = new Test();
//        int i = 1 % 0;
        test.setName("userTest");
        this.testMapper.insert(test);
        throw new InternalServerException();
//        throw new RuntimeException("aaaaaaaaaaaaaa");
    }
}
