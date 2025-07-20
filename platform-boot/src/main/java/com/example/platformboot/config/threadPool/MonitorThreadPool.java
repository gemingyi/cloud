package com.example.platformboot.config.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MonitorThreadPool extends ThreadPoolExecutor {

    /**
     * 默认拒绝策略
     */
    private static final RejectedExecutionHandler DEFAULT_HANDLER = new AbortPolicy();

    /**
     * 线程池名称，一般以业务名称命名，方便区分
     */
    private final String poolName;

    /**
     * 最短执行时间
     */
    private Long minCostTime = 0L;

    /**
     * 最长执行时间
     */
    private Long maxCostTime = 0L;
    /**
     * 总的耗时
     */
    private AtomicLong totalCostTime = new AtomicLong();
    /**
     * 用于暂存任务执行起始时间戳
     */
    private ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();


    /**
     * 初始化
     */
    public MonitorThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new MonitorThreadFactory(poolName), DEFAULT_HANDLER);
        this.poolName = poolName;
    }

    /**
     * 线程池延迟关闭时（等待线程池里的任务都执行完毕），统计线程池情况
     */
    @Override
    public void shutdown() {
        // 统计已执行任务、正在执行任务、未执行任务数量
        log.info("{} 关闭线程池， 已执行任务: {}, 正在执行任务: {}, 未执行任务数量: {}",
                this.poolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size());
        super.shutdown();
    }

    /**
     * 线程池立即关闭时，统计线程池情况
     */
    @Override
    public List<Runnable> shutdownNow() {
        // 统计已执行任务、正在执行任务、未执行任务数量
        log.info("{} 立即关闭线程池，已执行任务: {}, 正在执行任务: {}, 未执行任务数量: {}",
                this.poolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size());
        return super.shutdownNow();
    }

    /**
     * 任务执行之前，记录任务开始时间
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        //记录任务开始时间
        startTimeThreadLocal.set(System.currentTimeMillis());
        super.beforeExecute(t, r);
    }

    /**
     * 任务执行之后，计算任务结束时间
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // 当前任务耗时
        long costTime = System.currentTimeMillis() - startTimeThreadLocal.get();
        startTimeThreadLocal.remove();
        // 更新最大耗时时长
        maxCostTime = maxCostTime > costTime ? maxCostTime : costTime;
        if (getCompletedTaskCount() == 0) {
            minCostTime = costTime;
        }
        // 更新最小耗时时长
        minCostTime = minCostTime < costTime ? minCostTime : costTime;
        // 统计总耗时
        totalCostTime.addAndGet(costTime);
//        log.info("{}-monitor: " +
//                        "任务耗时: {} ms, 初始线程数: {}, 核心线程数: {}, 执行的任务数量: {}, " +
//                        "已完成任务数量: {}, 任务总数: {}, 队列里缓存的任务数量: {}, 池中存在的最大线程数: {}, " +
//                        "最大允许的线程数: {},  线程空闲时间: {}, 线程池是否关闭: {}, 线程池是否终止: {}",
//                this.poolName,
//                costTime, this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(),
//                this.getCompletedTaskCount(), this.getTaskCount(), this.getQueue().size(), this.getLargestPoolSize(),
//                this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS), this.isShutdown(), this.isTerminated());
        super.afterExecute(r, t);
    }

    // 获取最小耗时时长
    public Long getMinCostTime() {
        return minCostTime;
    }

    // 获取最大耗时时长
    public Long getMaxCostTime() {
        return maxCostTime;
    }

    // 计算平均耗时时长
    public long getAverageCostTime() {
        if (getCompletedTaskCount() == 0 || totalCostTime.get() == 0) {
            return 0;
        }
        // 总完成耗时除以总完成任务数
        return totalCostTime.get() / getCompletedTaskCount();
    }

    /**
     * 生成线程池所用的线程，改写了线程池默认的线程工厂，传入线程池名称，便于问题追踪
     */
    static class MonitorThreadFactory implements ThreadFactory {
        private static AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        /**
         * 初始化线程工厂
         *
         * @param poolName 线程池名称
         */
        MonitorThreadFactory(String poolName) {
            SecurityManager s = System.getSecurityManager();
            group = Objects.nonNull(s) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = poolName + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        /**
         * 新增线程
         *
         * @param r a runnable to be executed by new thread instance
         * @return
         */
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + THREAD_NUMBER.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}