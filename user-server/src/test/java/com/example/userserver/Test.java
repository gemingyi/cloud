package com.example.userserver;

import java.util.HashMap;
import java.util.Map;

public class Test {

    private static final String ZERO16 = "0000000000000000";

    class User {
        private Integer id;
        private String userName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    @org.junit.Test
    public void test() {
        Boolean exist = true;
        if(exist == null || !exist) {
            System.out.println("111111111111111");
        }

        String random = this.getFormatNumber(4, 1);
        System.out.println(random);
    }

    private String getFormatNumber(int length, int num) {
        String strNum = String.valueOf(num);
        String prefix = ZERO16.substring(0, length - strNum.length());
        return prefix + strNum;
    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("1", 1);

//        int i = uniquePaths(6, 1);
//        System.out.println(i);
    }

    public static int uniquePaths(int m, int n) {
        if (m <= 0 || n <= 0) {
            return 0;
        }

        int[][] dp = new int[m][n]; //
        // 初始化
        for(int i = 0; i < m; i++){
            dp[i][0] = 1;
        }
        for(int i = 0; i < n; i++){
            dp[0][i] = 1;
        }
        // 推导出 dp[m-1][n-1]
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        return dp[m-1][n-1];
    }

}
