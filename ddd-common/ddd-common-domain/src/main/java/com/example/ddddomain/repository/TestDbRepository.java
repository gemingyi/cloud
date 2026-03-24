package com.example.ddddomain.repository;

import com.example.ddddomain.model.TestDbModel;
import com.example.ddddomain.req.TestDbModelQueryReq;
import com.example.pluginmysql.model.page.PageVO;

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

    int update(TestDbModel model);

    List<TestDbModel> findList();

    PageVO<TestDbModel> findPage(TestDbModelQueryReq req);

    TestDbModel findDetail(Integer id);
}
