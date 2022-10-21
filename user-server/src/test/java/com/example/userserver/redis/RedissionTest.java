package com.example.userserver.redis;

import com.example.pluginredis.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
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

    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void test5() {
        // https://wnhyang.gitee.io/article/1e9afcf1.html
        // https://blog.csdn.net/weixin_44335140/article/details/115287767

        ScanOptions scanOptions = ScanOptions.NONE;
        Cursor<Map.Entry<Object, Object>> cursor = redisUtil.hScan("key", scanOptions);

        while(cursor.hasNext()) {
            Map.Entry<Object, Object> next = cursor.next();
            redisUtil.hDelete("key", String.valueOf(next.getKey()));
        }

        try {
            cursor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (redisUtil.hSize("key") <= 0) {
            redisUtil.delete("key");
        }
    }

    @Test
    public void test4() {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("aa");
        rateLimiter.trySetRate(RateType.OVERALL, 1, 10, RateIntervalUnit.SECONDS);

        for (int i = 0; i < 20; i++) {
            if (rateLimiter.tryAcquire(1)) {
                System.out.println(1111);
            } else {
                System.out.println(2222);
            }
        }
    }

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
