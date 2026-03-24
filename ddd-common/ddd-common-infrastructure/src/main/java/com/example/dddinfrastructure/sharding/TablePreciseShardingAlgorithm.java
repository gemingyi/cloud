package com.example.dddinfrastructure.sharding;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分表路由策略
 */
public class TablePreciseShardingAlgorithm implements ComplexKeysShardingAlgorithm<Long> {

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
//        }
//        if (!CollectionUtils.isEmpty(result)) {
//            return result;
//        }
//        return availableTargetNames;
//    }

    private static final String UNDERSCORE = "_";
    private static final int MAX_TABLE_INDEX = 1;
    private static final int TABLE_COUNT = MAX_TABLE_INDEX + 1;


    @Override
    public Collection<String> doSharding(
            Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {

        String route = ShardingThreadLocal.getValue();

        // 路由到新表逻辑
        if (TableRule.NEW.equals(route)) {
            return routeToNewTable(availableTargetNames, shardingValue);
        } else {
            // 路由到旧表逻辑（含默认路由）
            if (StringUtils.isEmpty(route) || !TableRule.OLD.equals(route)) {
            }

            String oldTable = shardingValue.getLogicTableName();
            return Collections.singletonList(oldTable);
        }
    }

    /**
     * 路由到新表的核心逻辑
     */
    private Collection<String> routeToNewTable(
            Collection<String> availableTargetNames, ComplexKeysShardingValue<Long> shardingValue) {

        Collection<Long> userIdValues = shardingValue.getColumnNameAndShardingValuesMap().get("user_id");

        if (!CollectionUtils.isEmpty(userIdValues)) {
            Long userId = userIdValues.iterator().next();
            final String suffix = getSuffixStr(userId);
            return shardingTargets(suffix, availableTargetNames);
        }

        throw new IllegalArgumentException(
                "未找到路由表,availableTargetNames:" + availableTargetNames + ",shardingValue:" + shardingValue);
    }

    private List<String> shardingTargets(String suffix, Collection<String> availableTargetNames) {
        final List<String> shardingTargets = availableTargetNames.stream()
                .filter(x -> x.endsWith(suffix))
                .collect(Collectors.toList());
        Assert.isTrue(shardingTargets.size() == 1,
                "未找到路由表,availableTargetNames:" + availableTargetNames + ",suffix:" + suffix);
        return shardingTargets;
    }

    protected String getSuffixStr(long shardingKey) {
        return UNDERSCORE + shardingKey % TABLE_COUNT;
    }

}
