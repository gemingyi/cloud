package com.example.pluginredis.javascript;


import com.example.commons.result.RestResult;

@FunctionalInterface
public interface ExecuteMethod {
    RestResult<?> doExecute();
}
