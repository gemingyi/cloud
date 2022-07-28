package com.example.userserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * https://www.cnblogs.com/three-fighter/p/14491256.html
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
        int batchSize = 10;

        int thread = list.size() / threadNum;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            Task task = new Task(countDownLatch, list, i * thread, (i + 1) * thread, batchSize);
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
    private int batchSize;

    public Task(CountDownLatch countDownLatch, List<Integer> list, int startNum, int endNum, int batchSize) {
        this.countDownLatch = countDownLatch;
        this.list = list;
        this.startNum = startNum;
        this.endNum = endNum;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        int subStartNum = startNum / batchSize;
        int subEndNum = endNum / batchSize;
        for (int i = subStartNum; i < subEndNum; i++) {
            List<Integer> pageList = list.subList(i * batchSize, (i + 1) * batchSize) ;
            System.out.println(Thread.currentThread().getName() + "-----" + pageList);
        }
        countDownLatch.countDown();


//        for (int i = startNum; i < endNum; i = i + batchSize) {
//            List<Integer> pageList = list.subList(i, i + batchSize);
//            System.out.println(Thread.currentThread().getName() + "-----" + pageList);
//        }
//        countDownLatch.countDown();


//        List<Integer> integers = list.subList(startNum, endNum);
//        int currentPage = 1;
//
//        while (currentPage <= integers.size() / batchSize) {
//            List<Integer> pageList = integers.subList((currentPage - 1) * batchSize, currentPage  * batchSize);
//            System.out.println(pageList);
//            currentPage ++;
//        }
//        countDownLatch.countDown();
    }

}