package com.example.userserver;

import com.example.platformboot.QueryCallable;
import com.example.userserver.service.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallableTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<FutureTask<Object>> taskList = new ArrayList<>();

        QueryCallable testCallable = new QueryCallable(
                ITestService.class, "getTestById", new Object[]{5}, new Class[]{Integer.class});
        FutureTask<Object> orderTask = new FutureTask<>(testCallable);
        taskList.add(orderTask);
        Future<Object> submit = executorService.submit(testCallable);
        Object o1 = submit.get();
        System.out.println(o1);

        for (FutureTask<Object> taskF : taskList) {
            executorService.submit(taskF);
        }
        for (FutureTask<Object> taskF : taskList) {
            Object o = taskF.get();
            System.out.println(o);
        }
        executorService.shutdown();
    }
}
