package com.example.dddinfrastructure.sharding;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.util.Collection;
import java.util.Collections;

/**
 * 分库路由策略
 */
public class DbPreciseShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

    private static final String OLD_DATA_BASE = "test1";
    private static final String NEW_DATA_BASE = "test2";


    @Override
    public Collection<String> doSharding(Collection<String> databaseNames, ComplexKeysShardingValue<Long> complexKeysShardingValue) {
        if (databaseNames.size() == 1) {
            return databaseNames;
        }
        String source = ShardingThreadLocal.getValue();
        String target = getDataSource(databaseNames);
        if ("old".equals(source)) {
            return Collections.singleton(target);
        } else if ("new".equals(source)) {
            for (String databaseName : databaseNames) {
                if (!databaseName.equals(target)) {
                    return Collections.singleton(databaseName);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private String getDataSource(Collection<String> databaseNames) {
        if (databaseNames.contains(OLD_DATA_BASE)) {
            return OLD_DATA_BASE;
        } else {
            return NEW_DATA_BASE;
        }
    }

}
