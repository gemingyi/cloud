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

//        int thread = list.size() / threadNum;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
//        for (int i = 0; i < threadNum; i++) {
//            Task task = new Task(countDownLatch, list, i * thread, (i + 1) * thread, batchSize);
//            executorService.submit(task);
//        }
//        countDownLatch.await();
//        executorService.shutdown();

        int thread = list.size() / threadNum;
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            List<Integer> subList = list.subList(i * thread, (i + 1) * thread);
            Task2 task = new Task2(countDownLatch, subList, batchSize);
            executorService.submit(task);
        }

    }
}


class Task implements Runnable {
    CountDownLatch countDownLatch;
    private List<Integer> list;
    private int startNum;
    private int endNum;
    private int batchSize;

    /**
     *
     * @param countDownLatch
     * @param list
     * @param startNum  每个线程处理的开始位置 0
     * @param endNum    每个线程处理的结束位置 20
     * @param batchSize 每批次处理大小 10
     */
    public Task(CountDownLatch countDownLatch, List<Integer> list, int startNum, int endNum, int batchSize) {
        this.countDownLatch = countDownLatch;
        this.list = list;
        this.startNum = startNum;
        this.endNum = endNum;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        // 0
        int subStartNum = startNum / batchSize;
        // 2
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


class Task2 implements Runnable {
    CountDownLatch countDownLatch;
    private List<Integer> subList;
    private int batchSize;

    /**
     *
     * @param countDownLatch
     * @param subList   0-20
     * @param batchSize 每批次处理大小 10
     */
    public Task2(CountDownLatch countDownLatch, List<Integer> subList, int batchSize) {
        this.countDownLatch = countDownLatch;
        this.subList = subList;
        this.batchSize = batchSize;
    }

    @Override
    public void run() {
        int currentPage = 1;
        while (currentPage <= subList.size() / batchSize) {
            List<Integer> pageList = subList.subList((currentPage - 1) * batchSize, currentPage  * batchSize);
            System.out.println(Thread.currentThread().getName() + "-----" + pageList);
            currentPage ++;
        }
        countDownLatch.countDown();
    }

}