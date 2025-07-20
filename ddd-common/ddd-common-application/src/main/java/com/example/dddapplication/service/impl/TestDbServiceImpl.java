package com.example.dddapplication.service.impl;

import com.example.dddapplication.service.ITestDbService;
import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.repository.TestDbRepository;
import com.example.dddinfrastructure.multiple.DataSource;
import com.example.dddinfrastructure.multiple.DataSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@DataSource(value = DataSourceType.SLAVE)
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

}
