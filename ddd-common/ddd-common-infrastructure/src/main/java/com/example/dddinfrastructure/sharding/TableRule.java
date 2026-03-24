package com.example.dddinfrastructure.sharding;


public class TableRule {
    public static final String NEW = "new";

    public static final String OLD = "old";

    public static final String BOTH = "all";

    //-------------------

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
