package com.example.pluginmysql.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @description:    mybatis 数据更新 初始化值
 * @author: mingyi ge
 * @date: 2021/11/9 11:22
 */
public class InitValueMetaObjectHandler implements MetaObjectHandler {


    // 插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("isDelete", 0, metaObject);
//        this.setFieldValByName("createBy", currentUserName, metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    // 更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("createBy", currentUserName, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}