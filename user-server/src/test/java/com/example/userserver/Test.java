package com.example.userserver;


import org.apache.commons.jexl3.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
        //期数
        Integer leaseTerm = 12;
        //租金
        BigDecimal leaseAmount = new BigDecimal(450);
        //到期买断价
        BigDecimal buyOutAmount = new BigDecimal(1000);
        //公式 	按照此公式取值：(11*月租金+到期买断价)/1.12*85%，保留两位小数
        BigDecimal formula = (new BigDecimal(leaseTerm - 1).multiply(leaseAmount).add(buyOutAmount))
                .divide(new BigDecimal("1.12"), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("0.85")).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(formula);


        //
//        ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
//        String strs = "((12-1)*450+1000)/1.12*0.85";
//        System.out.println(jse.eval(strs));


        //
        Map<String,Object> map = new HashMap<>();
        JexlEngine jexlEngine = new JexlBuilder().create();
        JexlExpression expression = jexlEngine.createExpression("((leaseTerm-1)*leaseAmount+buyOutAmount)/interestRate*rate");

        map.put("leaseTerm", 12);
        map.put("leaseAmount", new BigDecimal("450"));
        map.put("buyOutAmount", new BigDecimal("1000"));
        map.put("interestRate", new BigDecimal("1.12"));
        map.put("rate", new BigDecimal("0.85"));


        JexlContext content1 = new MapContext(map);
        BigDecimal formula2 = (BigDecimal) expression.evaluate(content1);
        System.out.println(formula2.setScale(2, BigDecimal.ROUND_HALF_UP));

    }


//    public static void main(String[] args) {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("1", 1);
//
////        int i = uniquePaths(6, 1);
////        System.out.println(i);
//    }
//
//    public static int uniquePaths(int m, int n) {
//        if (m <= 0 || n <= 0) {
//            return 0;
//        }
//
//        int[][] dp = new int[m][n]; //
//        // 初始化
//        for(int i = 0; i < m; i++){
//            dp[i][0] = 1;
//        }
//        for(int i = 0; i < n; i++){
//            dp[0][i] = 1;
//        }
//        // 推导出 dp[m-1][n-1]
//        for (int i = 1; i < m; i++) {
//            for (int j = 1; j < n; j++) {
//                dp[i][j] = dp[i-1][j] + dp[i][j-1];
//            }
//        }
//        return dp[m-1][n-1];
//    }


}
