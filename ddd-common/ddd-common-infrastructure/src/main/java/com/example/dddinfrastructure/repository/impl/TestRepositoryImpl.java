package com.example.dddinfrastructure.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ddddomain.model.TestModel;
import com.example.ddddomain.repository.TestRepository;
import com.example.dddinfrastructure.eo.Test;
import com.example.dddinfrastructure.mapper.TestMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
@Repository
public class TestRepositoryImpl extends ServiceImpl<TestMapper, Test> implements TestRepository {

    @Autowired
    private TestMapper testMapper;

    @Override
    public int save(TestModel model) {
        Test test = new Test();
        BeanUtils.copyProperties(model, test);
        return testMapper.insert(test);
    }
}
