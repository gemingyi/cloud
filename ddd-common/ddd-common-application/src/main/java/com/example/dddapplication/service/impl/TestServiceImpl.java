package com.example.dddapplication.service.impl;

import com.example.commons.exceptionHandle.exceptions.InternalServerException;
import com.example.dddapplication.service.ITestService;
import com.example.ddddomain.model.TestModel;
import com.example.ddddomain.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TestServiceImpl implements ITestService {

    @Autowired
    private TestRepository testRepository;


    @Override
    @Transactional
    public void seataTest() {
        TestModel test = new TestModel();
//        int i = 1 % 0;
        test.setName("userTest");
        this.testRepository.save(test);
        throw new InternalServerException();
//        throw new RuntimeException("aaaaaaaaaaaaaa");
    }

    @Override
    public void seataTest2() {

    }
}
