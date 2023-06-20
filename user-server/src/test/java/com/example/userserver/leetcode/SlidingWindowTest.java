package com.example.userserver.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/5/17 15:05
 */
public class SlidingWindowTest {

    public static void main(String[] args) {
//        char[] arr = {'a', 'b', 'c', 'a', 'b', 'c', 'b', 'b'};
//        System.out.println(test1(arr));
//
//
//        int[] arr2 = {1, 1, -3, 4, -1, 2, 1, -5, 4};
//        System.out.println(test2(arr2));
//        System.out.println(test3(arr2));
//
//
//        int[] arr3 = {-3, 3, 1, -3, 2, 4, 7};
//        System.out.println(test4(arr3, 3));

        int[] arr5 = {1, -1, 5, -2, 3};
        System.out.println(test5(arr5, 3));
    }


    /**
     * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
     * 输入: "abcab"
     * 输出: 3
     * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3
     */
    public static int test1(char[] arr) {
        if (arr.length == 0) {
            return 0;
        }

        int j = 0;
        int result = 0;
        int max = 0;
        Set<String> constant = new HashSet<>();

        for (int i = 0; i < arr.length; i++) {
            if (!constant.contains(String.valueOf(arr[i]))) {
                max++;
                constant.add(String.valueOf(arr[i]));
            } else {
                while (constant.contains(String.valueOf(arr[i]))) {
                    max--;
                    constant.remove(String.valueOf(arr[j]));
                    j++;
                }
            }

            result = Math.max(max, result);
        }
        return result;
    }


    /**
     * 输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。
     * 输入: nums = [1, 1, -3]
     * 输出: 2
     *
     * @return
     */
    public static int test2(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }

        int result = 0;

        for (int i = 0; i < arr.length; i++) {
            int maxValue = 0;
            for (int j = i; j < arr.length; j++) {
                maxValue = maxValue + arr[j];
                result = Math.max(result, maxValue);
            }
        }

        return result ;
    }

    /**
     * 输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。
     * 输入: nums = [1, 1, -3]
     * 输出: 2
     *
     * @return
     */
    public static int test3(int[] nums) {
        int res = nums[0];
        int pre = nums[0];
        int cur = 0;
        for (int i = 1; i < nums.length; i++) {
            // 更新cur，相当于更新dp[i]
            cur = nums[i] + Math.max(pre, 0);
            // 更新最大值
            res = Math.max(res, cur);
            // 更新dp[i-1]，使用pre来记录
            pre = cur;
        }
        return res;
    }


    /**
     * 给定数组，获取数组中n个连续元素，最大的和。
     * Input: [-3, 3, 1, -3, 2, 4, 7], n=3
     * Output: 13
     */
    public static int test4(int[] arr, int n) {
        if (n > arr.length) {
            return 0;
        }

        int max = 0;
        int result = 0;

        int count = 0;
        int j = 0;

        for (int i = 0; i < arr.length; i++) {
            max = max + arr[i];
            result = Math.max(result, max);
            count ++;
            while (count >= n) {
                max = max - arr[j];
                count --;
                j++;
            }
        }
        return result;
    }

    /**
     * 最大子数组之和为k
     * 输入: nums = [1, -1, 5, -2, 3], k = 3
     * 输出: 4
     * 解释:
     * 子数组[1, -1, 5, -2]的和为3，且长度最大
     */
    public static int test5(int[] arr, int k) {
        if (k > arr.length) {
            return 0;
        }

        int j = 0;
        int count = 0;

        int sum = 0;
        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            sum = sum + arr[i];
            count ++;
            while (sum == 3) {
                sum = sum - arr[j];
                result = Math.max(result, count);
                count --;
                j++;
            }
        }
        return result;
    }

}
