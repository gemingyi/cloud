package com.example.userserver;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

public class BloomFilterTest {


    public static void main(String[] args) {
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 100000000, 0.0001);
        bloomFilter.put("121");
        bloomFilter.put("122");
        bloomFilter.put("123");
        System.out.println(bloomFilter.mightContain("121"));
    }

}
