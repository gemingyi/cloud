package com.example.userserver;

import com.example.userserver.manager.ASyncExportExcelTask;
import com.example.userserver.manager.SelectReq;
import com.example.userserver.manager.SelectTypeEnum;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 15:25
 */
public class ExportTest {


//    private static final Map<Long, Future<Boolean>> threadMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //更新任务 开始
        task(executorService);

    }

    public static void task(ExecutorService executorService) {

        // 新增导出记录
        Long taskId = 0L;

        //
        SelectReq selectReq = new SelectReq();
        SelectReq.SelectReqOne selectReqOne = new SelectReq.SelectReqOne();
        selectReq.setSelectReqOne(selectReqOne);
        ASyncExportExcelTask aSyncExportExcelTask = new ASyncExportExcelTask(1L, SelectTypeEnum.TEST_ONE, selectReq);
        Future<Boolean> submit = executorService.submit(aSyncExportExcelTask);

        //
//        threadMap.put(taskId, submit);
    }

//    public static void cancel() {
//        Long taskId = 0L;
//        Future<Boolean> future = threadMap.get(taskId);
//
//        future.cancel(true);
//
//        boolean cancelled = future.isCancelled();
//        if (cancelled) {
//            // 更新任务 取消
//        }
//    }

}
