package com.example.dddinfrastructure.sharding;


public class TableRule {
    private String tableName;
    private String readRule;
    private String writeRule;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReadRule() {
        return readRule;
    }

    public void setReadRule(String readRule) {
        this.readRule = readRule;
    }

    public String getWriteRule() {
        return writeRule;
    }

    public void setWriteRule(String writeRule) {
        this.writeRule = writeRule;
    }
}
