//package com.example.dddinfrastructure.sharding;
//
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
//import org.springframework.util.CollectionUtils;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * 分表路由策略
// */
//public class TablePreciseShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {
//
//    private static final int MAX_TABLE_INDEX = 1;
//    private static final int TABLE_COUNT = MAX_TABLE_INDEX + 1;
//
//    @Override
//    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {
//        Collection<String> result = new ArrayList<>();
//
//        Map<String, Collection<Long>> shardingValuesMap = shardingValue.getColumnNameAndShardingValuesMap();
//        Collection<Long> userIds = shardingValuesMap.get("user_id");
//        for (Long userId : userIds) {
//            String suffix = String.valueOf(userId % TABLE_COUNT);
//
//            for (String targetName : availableTargetNames) {
//                if (targetName.endsWith("_" + suffix)) {
//                    result.add(targetName);
//                }
//            }
//         }
//        if (!CollectionUtils.isEmpty(result)) {
//            return result;
//        }
//        return availableTargetNames;
//    }
//
//}
