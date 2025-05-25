package com.example.dddinfrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.dddinfrastructure.eo.TestDb;
import com.example.dddinfrastructure.sharding.Invoke;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
@Invoke(tableName = "test_db", writeMethod = {"insert", "deleteById"})
public interface TestDbMapper extends BaseMapper<TestDb> {

}
