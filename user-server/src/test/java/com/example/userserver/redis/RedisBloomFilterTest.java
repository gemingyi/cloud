package com.example.userserver.redis;

import com.example.pluginredis.util.BloomFilterHelper;
import com.example.pluginredis.util.RedisBloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBloomFilterTest {

    @Autowired
    RedisBloomFilter redisBloomFilter;


    @Test
    public void test() {
        BloomFilterHelper<CharSequence> bloomFilterHelper = new BloomFilterHelper<>(Funnels.stringFunnel(Charset.defaultCharset()), 10000, 0.01);
        int j = 0;
        for (int i = 0; i < 100; i++) {
            redisBloomFilter.addByBloomFilter(bloomFilterHelper, "bloom", i+"");
        }
        for (int i = 0; i < 1000; i++) {
            boolean result = redisBloomFilter.includeByBloomFilter(bloomFilterHelper, "bloom", i+"");
            if (!result) {
                j++;
            }
        }
        System.out.println("漏掉了" + j + "个");
    }
}
