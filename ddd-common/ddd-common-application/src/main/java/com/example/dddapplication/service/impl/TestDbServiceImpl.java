package com.example.dddapplication.service.impl;

import com.example.dddapplication.service.ITestDbService;
import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.repository.TestDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TestDbServiceImpl implements ITestDbService {

    @Autowired
    private TestDbRepository testDbRepository;


    @Override
    public int save() {
        TestDbModel testDbModel = new TestDbModel();
//        testDbModel.setId(1);
        testDbModel.setUserId(1L);
        testDbModel.setName("1");
        testDbRepository.save(testDbModel);

        TestDbModel testDbMode2 = new TestDbModel();
//        testDbMode2.setId(2);
        testDbMode2.setUserId(2L);
        testDbMode2.setName("2");
        testDbRepository.save(testDbMode2);
        return 0;
    }

    @Override
    public List<TestDbModel> findList() {
        List<TestDbModel> list = testDbRepository.findList();
        return list;
    }
}
