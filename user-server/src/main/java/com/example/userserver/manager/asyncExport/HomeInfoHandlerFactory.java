package com.example.userserver.manager.asyncExport;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2023/8/4 14:46
 */
@Component
public class HomeInfoHandlerFactory {

    private static ConcurrentHashMap<Integer, IExportService> concurrentHashMap = new ConcurrentHashMap<>();


    public static void register(Integer type, IExportService exportService) {
        concurrentHashMap.put(type, exportService);
    }

    public static IExportService get(Integer type) {
        return concurrentHashMap.get(type);
    }
}
