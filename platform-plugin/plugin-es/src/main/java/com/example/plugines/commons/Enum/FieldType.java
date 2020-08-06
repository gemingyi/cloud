package com.example.plugines.commons.Enum;

/**
 *  type 类型 如：keyword,text,integer。字符串是否分词 keyword不分，text分
 *  store 是否单独设置此字段的是否存储而从_source字段中分离，默认是false，只能搜索，不能获取值
 *  search_analyzer 设置搜索时的分词器，默认跟ananlyzer是一致的
 *  analyzer 指定分词器 如：ik_max_word， ik_smart
 *  fielddata 针对分词字段，参与排序或聚合时能提高性能
 */
public enum FieldType {
    Text, Keyword, Integer, Nested, Float, Date;

    private FieldType() {}
}

