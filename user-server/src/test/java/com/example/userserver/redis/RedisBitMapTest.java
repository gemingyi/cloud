package com.example.userserver.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis 实现签到
 * https://blog.csdn.net/qq1311256696/article/details/109473669
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBitMapTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void test1() {
//        redisTemplate.opsForValue().setBit("test:user", 1, true );
        for (int i = 1; i <= 9; i++) {
            if (i % 2 == 0) {
                redisTemplate.opsForValue().setBit("test:user", i, false);
            } else {
                redisTemplate.opsForValue().setBit("test:user", i, true);
            }
        }
    }

    @Test
    public void test2() {
        Boolean bit = redisTemplate.opsForValue().getBit("test:user", 1);
        System.out.println(bit);
    }

    @Test
    public void test3() {
        Object o1 = redisTemplate.opsForValue().get("test:user");
        System.out.println(o1);
    }

    @Test
    public void test4() {
        String key = "test:user";
        Long count = (Long) redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));
        System.out.println(count);
    }

    @Test
    public void test5() {
        String key = "test:user";
        List<Long> fields = redisTemplate.execute((RedisCallback<List<Long>>) con ->
                con.bitField(key.getBytes(),
                        BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(30)).valueAt(0)));
        System.out.println(fields);
        int count = 0;
        if (fields != null && fields.size() > 0) {
            long v = fields.get(0) == null ? 0 : fields.get(0);
            for (int i = 0; i < 30; i++) {
                if (v >> 1 << 1 == v) {
                    if (i > 0) {
                        break;
                    } else {
                        count += 1;
                    }
                }
                v >>= 1;
            }
        }
        System.out.println(count);
    }

    @Test
    public void test6() {
        Map<String, Boolean> signMap = new HashMap<>();
        String key = "test:user";
        List<Long> fields = redisTemplate.execute((RedisCallback<List<Long>>) con ->
                con.bitField(key.getBytes(),
                        BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(30)).valueAt(0)));

        if (fields != null && fields.size() > 0) {
            long v = fields.get(0) == null ? 0 : fields.get(0);
            for (int i = 30; i > 0; i--) {
                signMap.put(String.valueOf(i), v >> 1 << 1 != v);
                v >>= 1;
            }
        }
        System.out.println(signMap);
    }

    // https://blog.csdn.net/yzh_1346983557/article/details/119837981
    public void test7() {
        List<Object> pipelinedResultList = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                ValueOperations<String, Object> valueOperations = (ValueOperations<String, Object>) operations.opsForValue();

                valueOperations.set("yzh1", "hello world");
                valueOperations.set("yzh2", "hello redis");

                valueOperations.get("yzh1");
                valueOperations.get("yzh2");

                // 返回null即可，因为返回值会被管道的返回值覆盖，外层取不到这里的返回值
                return null;
            }
        });
        System.out.println("pipelinedResultList=" + pipelinedResultList);

    }

    private static BitSet fromByteArrayReverse(final byte[] bytes) {
        final BitSet bits = new BitSet();
        for (int i = 0; i < bytes.length * 8; i++) {
            if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                bits.set(i);
            }
        }
        return bits;
    }

}
