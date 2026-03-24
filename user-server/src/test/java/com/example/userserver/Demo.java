package com.example.userserver;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Demo {

//    3.给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
//    请必须使用时间复杂度为 O(log n) 的算法。
//
//    示例 1:
//    输入: nums = [1,3,5,6], target = 5
//    输出: 2

    public static void main(String[] args) {
//        int[] nums = {1,3,5,6};
//        int target = 7;
//        System.out.println(target(nums, target));

//        String str = "race a car";
//        boolean check = check(str.toCharArray());
//        System.out.println(check);

//        String s = "abc";
//        String t = "ahbgdc";
//        boolean subsequence = isSubsequence(s, t);
//        System.out.println(subsequence);

//        String str = "()[]}";
//        boolean valid = isValid(str.toCharArray());
//        System.out.println(valid);

//        int[] arr = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
//        int repeat = repeat(arr);
//        System.out.println(repeat);

//        String[] strs = {"flower","flow","flight"};
////        String prefix = prefix(strs);
//        String prefix = prefixTwo(strs);
//        System.out.println(prefix);

//        int[][] arr = {{1,3},{2,6},{8,10},{15,18}};
//        List<int[]> ints = mergeArr(arr);
//        System.out.println(ints);

//        int[] arr = {0,1,2,4,5,7};
//        List<String> strings = summaryRanges(arr);
//        System.out.println(strings);

//        String haystack = "dsadbutsad", needle = "sad";
//        int i = matchStr(haystack, needle);
//        System.out.println(i);

        String str = "luffy is still joyboy";
        int lenght = lenght(str);
        System.out.println(lenght);
    }

    public static int target(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = (right + left) / 2;
            if (target == arr[mid]) {
                return mid;
            } else if (target < arr[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    public static boolean check(char[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            if (arr[left] != arr[right]) {
                return false;
            }
            left ++;
            right --;
        }
        return true;
    }

    public static boolean isSubsequence(String s, String t) {
//        String s = "abc";
//        String t = "ahbgdc";

        int n = s.length(), m = t.length();
        int i = 0, j = 0;
        while (i < n && j < m) {
            if (s.charAt(i) == t.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == n;
    }

    public static boolean isValid(char[] arr) {
        // ()[]{}
        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("[", "]");
        map.put("{", "}");
        List<String> tempList = new ArrayList<>(3);
        tempList.add("(");
        tempList.add("[");
        tempList.add("{");
        Stack<Character> stack = new Stack<>();
        for (char c : arr) {
            if (tempList.contains(String.valueOf(c))) {
                stack.push(c);
            } else {
                if (stack.isEmpty()
                        || !map.get(stack.pop().toString()).equals(String.valueOf(c))) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static int repeat(int[] arr) {
//        [0,0,1,1,1,2,2,3,3,4]
        int fast = 1;
        int slow = 1;
        for (;fast < arr.length;) {
            if (arr[fast] != arr[fast - 1]) {
                arr[slow] = arr[fast];
                slow++;
            }
            fast++;
        }
        System.out.println(arr);
        return slow;
    }


    public static String prefix(String[] arr) {
        //    输入：strs = ["flower","flow","flight"]
        //    输出："fl"
        String firstStr = arr[0];
        for (int i = 1; i < arr.length; i++) {
            firstStr = same(firstStr, arr[i]);
        }
        return firstStr;
    }

    public static String same(String prefix, String str) {
        int minLength = 0;
        if (prefix.length() > str.length()) {
            minLength = str.length();
        } else {
            minLength = prefix.length();
        }
        int count = 0;
        for (int i = 0; i < minLength; i++) {
            if (String.valueOf(prefix.charAt(i)).equals(String.valueOf(str.charAt(i)))) {
                count ++;
            }
        }
        return prefix.substring(0, count);
    }

    public static String prefixTwo(String[] arr) {
        //    输入：strs = ["flower","flow","flight"]
        //    输出："fl"
        String firstStr = arr[0];
        for (int j = 0; j < firstStr.toCharArray().length; j++) {
            char c = firstStr.charAt(j);
            for (int i = 1; i < arr.length; i++) {
                if (j == arr[i].length() || !String.valueOf(c).equals(String.valueOf(arr[i].charAt(j)))) {
                    return firstStr.substring(0, j);
                }
            }
        }
        return firstStr;
    }

    public static List<int[]> mergeArr(int[][] arr) {
        //输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
        //输出：[[1,6],[8,10],[15,18]]
//        int resultArr[][] = new int[arr.length][2];
        List<int[]> resultList = new ArrayList<>();
        Arrays.sort(arr, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        for (int i = 0; i < arr.length; i++) {
            int left = arr[i][0];
            int right = arr[i][1];
//            if (resultArr[0][1] == 0 || resultArr[i-1][1] < left) {
//                resultArr[i][0] = left;
//                resultArr[i][1] = right;
//            } else {
//                resultArr[i-1][1] = Math.max(resultArr[i-1][1], right);
//            }

            if (resultList.isEmpty() || resultList.get(resultList.size() - 1)[1] < left) {
                resultList.add(new int[]{left, right});
            } else {
                resultList.get(resultList.size() - 1)[1] = Math.max(resultList.get(resultList.size() - 1)[1], right);
//            }
            }
        }
        return resultList;
    }

    public static List<String> summaryRanges(int[] nums) {
        // {0,1,2,4,5,7};
        // 0->2, 4->5, 7
        int left = 0, right = 0;//左右边界
        List<String> list = new ArrayList<>();
        while (right < nums.length) {
            while (right < nums.length - 1 && nums[right] + 1 == nums[right + 1]) {
                right++;//找到连续区间的右边界
            }
            if (left == right) {
                //如果区间长度为0，就单独输出
                list.add(Integer.toString(nums[left]));
            } else {
                list.add(nums[left] + "->" + nums[right]);
            }
            right++;
            left = right;
        }
        return list;
    }

    public static int matchStr(String str, String str2) {
        // haystack = "badbutsad", needle = "sad"
        for (int i = 0; i < str.toCharArray().length - str2.toCharArray().length; i++) {
            boolean flag = true;
            for (int j = 0; j < str2.toCharArray().length; j++) {
                if (str.charAt(i + j) != str2.charAt(j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return i;
            }
        }
        return -1;
    }

    public static int lenght(String str) {
        // luffy is still joyboy
        int result = 0;
        int max = 0;

        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                max++;
            }
            if (i == chars.length - 1 || chars[i] == ' ') {
                result = Math.max(result, max);
                max = 0;
            }

        }
        return result;
    }

}
