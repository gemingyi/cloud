package com.example.userserver.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/9/1 11:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissionTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;



    @Test
    public void test3() {
        redisTemplate.opsForValue().set("user:test:test", "hello world");
        System.out.println(redisTemplate.opsForValue().get("user:test:test"));
        redisTemplate.delete("user:test:test");
    }

    @Test
    public void test2() {
        String lockKey = "user:test:lock";

        RLock lock = redissonClient.getLock(lockKey);
        lock.tryLock();
    }

    @Test
    public void test() {

        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService executor = Executors.newFixedThreadPool(5);

        String lockKey = "user:test:lock";
        for (int i = 0; i < 5; i++) {
            ThreadTask threadTask = new ThreadTask(lockKey, countDownLatch);
            executor.execute(threadTask);
            countDownLatch.countDown();
        }
    }


    class ThreadTask implements Runnable {
        private String lockKey;
        private CountDownLatch countDownLatch;

        ThreadTask(String lockKey, CountDownLatch countDownLatch) {
            this.lockKey = lockKey;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RLock lock = redissonClient.getLock(lockKey);
            try {
                boolean b = lock.tryLock(1, 2, TimeUnit.SECONDS);
                Thread.sleep(1100);
                if (b) {
                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    System.out.println(Thread.currentThread().getName() + "获取锁失败");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(lock.isLocked() && lock.isHeldByCurrentThread()){
                    System.out.println(Thread.currentThread().getName() + "释放锁");
                    lock.unlock();
                }
            }
        }

    }


}
