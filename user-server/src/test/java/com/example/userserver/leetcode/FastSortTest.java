package com.example.userserver.leetcode;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/8/20 13:50
 */
public class FastSortTest {

    public static void main(String[] args) {
        int[] arr = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};

        fastSort(arr, 0, arr.length - 1);

        System.out.println(arr);
    }

    public static void fastSort(int[] arr, int left, int right) {
        if (arr.length < 0) {
            return;
        }
        if (left >= right) {
            return;
        }

        int i = left;
        int j = right;
        int base = arr[left];

        while (i < j) {
            while (i < j && arr[j] >= base) {
                j--;
            }
            while (i < j && arr[i] <= base) {
                i++;
            }

            if (i < j) {
                swap(arr, i, j);
            }
        }
        //
        arr[left] = arr[i];
        arr[i] = base;

        //
        fastSort(arr, left, i - 1);
        fastSort(arr, i + 1, right);
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
