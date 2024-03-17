package com.example.ddddomain.repository;

import com.example.ddddomain.model.TestDbModel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
public interface TestDbRepository {

    int save(TestDbModel model);

    int delete(Integer id);

    List<TestDbModel> findList();
}
