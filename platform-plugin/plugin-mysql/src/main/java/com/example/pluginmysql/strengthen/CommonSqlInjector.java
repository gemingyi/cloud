package com.example.pluginmysql.strengthen;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.example.pluginmysql.strengthen.method.InsertBatch;

import java.util.List;

/**
 * 注入自定义 mybatis命令
 */
public class CommonSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new InsertBatch());
        return methodList;
    }

}
