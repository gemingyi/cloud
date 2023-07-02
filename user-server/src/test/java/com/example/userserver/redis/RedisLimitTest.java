package com.example.userserver.redis;

import com.example.pluginredis.util.RedisKeyBuildUtil;
import com.example.userserver.dao.entity.UserInfo;


//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RedisLimitTest {

//    @Autowired
//    private RedisRateLimit redisRateLimit;
//
//
//    @Test
//    public void test() {
//        for (int i = 0; i < 10; i++) {
//            boolean flag = this.redisRateLimit.limit("test", 5, 1);
//            if(!flag) {
//                System.out.println(Thread.currentThread() +  "限流");
//            } else {
//                System.out.println(Thread.currentThread() +  "通过");
//            }
//        }
//    }

//    @Test
//    public void testKeyBuild() {
//
//    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("aaa");
        userInfo.setGender(1);
        userInfo.setPhone("123123");

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            String str = RedisKeyBuildUtil.keyBuilder("user", "list_user", userInfo);
            System.err.println(str);
        }
        System.out.println(System.currentTimeMillis() - start);

        String str2 = RedisKeyBuildUtil.keyBuilder("user", "get_user", "aaa", "bbb", "ccc");
        System.err.println(str2);


        String str3 = RedisKeyBuildUtil.keyBuilder("user", "get_user", null);
        System.err.println(str3);
    }

}

