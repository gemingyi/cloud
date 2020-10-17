package com.example.userserver.service.impl;

import com.example.commons.IAbstractService;
import com.example.userserver.dao.TestMapper;
import com.example.userserver.model.Test;
import com.example.userserver.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
