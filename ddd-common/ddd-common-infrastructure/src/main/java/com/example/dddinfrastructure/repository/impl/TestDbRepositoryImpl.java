package com.example.dddinfrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.repository.TestDbRepository;
import com.example.dddinfrastructure.eo.TestDb;
import com.example.dddinfrastructure.mapper.TestDbMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
@Repository
public class TestDbRepositoryImpl extends ServiceImpl<TestDbMapper, TestDb> implements TestDbRepository {

    @Autowired
    private TestDbMapper testDbMapper;

    @Override
    public int save(TestDbModel model) {
        TestDb testDb = new TestDb();
        BeanUtils.copyProperties(model, testDb);
        return testDbMapper.insert(testDb);
    }

    @Override
    public int delete(Integer id) {
        return testDbMapper.deleteById(id);
    }

    @Override
    public List<TestDbModel> findList() {
        List<TestDb> testDbs = testDbMapper.selectList(null);
        List<TestDbModel> testDbModels = new ArrayList<>(testDbs.size());
        for (TestDb testDb : testDbs) {
            TestDbModel testDbModel = new TestDbModel();
            BeanUtils.copyProperties(testDb, testDbModel);
            testDbModels.add(testDbModel);
        }
        return testDbModels;
    }
}
