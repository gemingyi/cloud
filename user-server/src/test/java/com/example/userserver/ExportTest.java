package com.example.userserver;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.userserver.manager.asyncExport.ASyncExportExcelTask;
import com.example.userserver.manager.asyncExport.ExportTask;
import com.example.userserver.manager.asyncExport.SelectReq;
import com.example.userserver.manager.asyncExport.SelectTypeEnum;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 15:25
 */
@RunWith(SpringRunner.class)
//@SpringBootTest
public class ExportTest {


//    private static final Map<Long, Future<Boolean>> threadMap = new ConcurrentHashMap<>();


    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        //更新任务 开始
//        task(executorService);

        // https://blog.csdn.net/power_to_go/article/details/128792128
        // https://blog.csdn.net/fairylym/article/details/116889837
        String templateFilePath = "E:\\test\\excel\\testtemplatel.xlsx";
        File excelFile = new File("E:\\test\\excel\\testexcel_target.xlsx");
        List<ExportTask> exportTaskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ExportTask exportTask = new ExportTask();
            exportTask.setId(1L);
            exportTask.setSelectCode("1");
            exportTask.setSelectName("测试导出类型1");
            exportTaskList.add(exportTask);
        }

        ExcelWriter excelWriter = EasyExcel.write(excelFile).withTemplate(templateFilePath).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
//        excelWriter.fill(new FillWrapper("data", exportTaskList), writeSheet);
        excelWriter.fill(exportTaskList, writeSheet);
        excelWriter.finish();
    }

//    @Test
//    public void test() {
//
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//        //更新任务 开始
//        task(executorService);
//    }

    public static void task(ExecutorService executorService) {

        // 新增导出记录
        Long taskId = 1L;

        //
        SelectReq selectReq = new SelectReq();
        SelectReq.SelectReqOne selectReqOne = new SelectReq.SelectReqOne();
        selectReq.setSelectReqOne(selectReqOne);
        ASyncExportExcelTask aSyncExportExcelTask = new ASyncExportExcelTask(taskId, SelectTypeEnum.TEST_ONE, selectReq);
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
