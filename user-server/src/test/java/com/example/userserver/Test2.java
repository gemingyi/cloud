package com.example.userserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/11/2 15:20
 */
public class Test2 {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        int threadNum = 5;
        int threadOperation = list.size() / threadNum;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            Task task = new Task(countDownLatch, list, i * threadOperation, (i + 1) * threadOperation);
            executorService.submit(task);
        }
        countDownLatch.await();
        executorService.shutdown();
    }
}


class Task implements Runnable {
    CountDownLatch countDownLatch;
    private List<Integer> list;
    private int startNum;
    private int endNum;

    public Task(CountDownLatch countDownLatch, List<Integer> list, int startNum, int endNum) {
        this.countDownLatch = countDownLatch;
        this.list = list;
        this.startNum = startNum;
        this.endNum = endNum;
    }

    @Override
    public void run() {
        List<Integer> integers = list.subList(startNum, endNum);
        int pageSize = 10;
        int currentPage = 1;

        while (currentPage <= integers.size() / pageSize) {
            List<Integer> pageList = integers.subList((currentPage - 1) * pageSize, currentPage  * pageSize);
            System.out.println(pageList);
            currentPage ++;
        }
        countDownLatch.countDown();
    }

}