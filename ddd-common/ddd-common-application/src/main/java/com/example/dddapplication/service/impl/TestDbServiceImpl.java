package com.example.dddapplication.service.impl;

import com.example.dddapplication.service.ITestDbService;
import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.repository.TestDbRepository;
import com.example.ddddomain.req.TestDbModelDetailReq;
import com.example.ddddomain.req.TestDbModelQueryReq;
import com.example.pluginmysql.model.page.PageVO;
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

    @Override
    public int saveMaster() {
        TestDbModel testDbModel = new TestDbModel();
//        testDbModel.setId(1);
        testDbModel.setUserId(1L);
        testDbModel.setName("1");
        testDbRepository.save(testDbModel);
        return 0;
    }

    @Override
    public Integer insert(TestDbModel req) {
        return testDbRepository.save(req);
    }

    @Override
    public Integer delete(TestDbModelDetailReq req) {
        return testDbRepository.delete(req.getId());
    }

    @Override
    public Integer update(TestDbModel req) {
        return testDbRepository.update(req);
    }

    @Override
    public PageVO<TestDbModel> findPage(TestDbModelQueryReq req) {
        return testDbRepository.findPage(req);
    }

    @Override
    public TestDbModel findDetail(TestDbModelDetailReq req) {
        return testDbRepository.findDetail(req.getId());
    }

}
