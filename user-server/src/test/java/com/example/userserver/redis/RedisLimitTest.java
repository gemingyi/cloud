package com.example.userserver.redis;

import com.example.pluginredis.RedisKeyBuildUtil;
import com.example.pluginredis.util.RedisRateLimit;
import com.example.userserver.model.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLimitTest {

    @Autowired
    private RedisRateLimit redisRateLimit;


    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            boolean flag = this.redisRateLimit.limit("test", 5, 1);
            if(!flag) {
                System.out.println(Thread.currentThread() +  "限流");
            } else {
                System.out.println(Thread.currentThread() +  "通过");
            }
        }
    }

    @Test
    public void testKeyBuild() {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("小明");
        userInfo.setGender(1);
        userInfo.setPhone("123123");

        String str = RedisKeyBuildUtil.keyBuilder("user", "list_user", userInfo);
        System.out.println(str);

        String str2 = RedisKeyBuildUtil.keyBuilder("user", "get_user", String.valueOf(1));
        System.out.println(str2);


        String str3 = RedisKeyBuildUtil.keyBuilder("user", "get_user", null);
        System.out.println(str3);
    }

}
