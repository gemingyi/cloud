package com.example.userserver.redis;


import com.example.pluginredis.util.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReidsLockTest {

    @Autowired
    private RedisLock redisLock;


    @Test
    public void testLock() {
        //
        String id = redisLock.acquireLock("aaaaaaaa", 2);
        System.out.println(id);
        //
        String id2 = redisLock.acquireLockWithTimeOut("aaaaaaaa", 5, 3);
        System.out.println(id2);
    }

    @Test
    public void testAcquireLock() {
        //
        String id = redisLock.acquireLock("aaaaaaaa", 10);
        System.out.println(id);
        //
        if(!StringUtils.isEmpty(id)) {
            boolean flag = redisLock.releaseLock("aaaaaaaa", id);
            if(flag) {
                System.out.println("释放锁成功!");
            }
        }
        //
        String id2 = redisLock.acquireLockWithTimeOut("aaaaaaaa", 5, 3);
        System.out.println(id2);
    }

    @Test
    public void testRelease() {
        boolean flag = redisLock.releaseLock("aaaaaaaa", "1581599386211");
        System.out.println(flag);
    }

}
