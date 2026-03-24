package com.example.userserver;

public class Demo2 {

    public static void main(String[] args) {
        // 给定一个数组nums编写一个函数将所有0移动到数组的末尾,同时保持非零元素的相对顺序。
        // 请注意,必须在不复制数组的情况下原地对数组进行操作。
        // 输入: nums = [0,1,0,3,12]输出: [1,3,12,0,0]
        int [] arr = {0,1,0,3,12};
//        move(arr);
        move2(arr);
    }

    public static void move(int[] arr) {
        int left = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                arr[left++] = arr[i];
            }
        }
        while (left < arr.length) {
            arr[left] = 0;
            left ++;
        }
        StringBuffer result = new StringBuffer();
        for (int i : arr) {
            result.append(i).append(",");
        }
        System.out.println(result.toString());
    }

    public static void move2(int[] arr) {
        int left = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) {
                swap(arr, left, i);
                left ++;
            }
        }
        StringBuffer result = new StringBuffer();
        for (int i : arr) {
            result.append(i).append(",");
        }
        System.out.println(result.toString());
    }

    public static void swap(int[] nums, int left, int right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }

    public static void move3(int[] arr) {
        int left = 0;
        int right = 0;
        for (; right < arr.length ;) {
            if (arr[right] != 0) {
                arr[left++] = arr[right];
            }
            right++;
        }
        while (left < arr.length) {
            arr[left] = 0;
            left ++;
        }
        StringBuffer result = new StringBuffer();
        for (int i : arr) {
            result.append(i).append(",");
        }
        System.out.println(result.toString());
    }

}
