package com.example.dddinfrastructure.sharding;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@PropertySource("classpath:table-rule.properties")
@ConfigurationProperties(prefix = "table.rule")
public class TableRuleProperties {

    /**
     * table.rule.tableRuleList[0].tableName=test_db
     * table.rule.tableRuleList[0].readRule=old
     * table.rule.tableRuleList[0].writeRule=all
     */
    private List<TableRule> tableRuleList;

    public List<TableRule> getTableRuleList() {
        return tableRuleList;
    }

    public void setTableRuleList(List<TableRule> tableRuleList) {
        this.tableRuleList = tableRuleList;
    }

}
