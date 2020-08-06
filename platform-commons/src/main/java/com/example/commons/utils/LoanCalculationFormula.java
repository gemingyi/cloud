package com.example.commons.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/flueky/article/details/77099454    贷款利息
 * https://zhidao.baidu.com/question/499415587351987804.html    日利息
 * https://baike.baidu.com/item/%E5%88%A9%E6%81%AF%E8%AE%A1%E7%AE%97%E5%85%AC%E5%BC%8F/6283661?fr=aladdin   银行计息
 * https://baike.baidu.com/item/%E5%A4%8D%E5%88%A9%E8%AE%A1%E7%AE%97%E5%85%AC%E5%BC%8F 利滚利计息
 */
public class LoanCalculationFormula {


    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    /**
     * 实现执行字符串中的运算公式
     */
    public static void main(String[] args) {
        String strs = "1+1*2+(10-(2*(5-3)*(2-1))-4)+10/(5-0)";
        try {
            System.out.println(jse.eval(strs));
        } catch (Exception t) {
        }
    }


    public static String[] calculateEqualPrincipalAndInterest(double principal, int months, double rate) {
        List<String> data = new ArrayList<>();
        double monthRate = rate / (100 * 12);//月利率
        double preLoan = (principal * monthRate * Math.pow((1 + monthRate), months)) / (Math.pow((1 + monthRate), months) - 1);//每月还款金额
        double totalMoney = preLoan * months;//还款总额
        double interest = totalMoney - principal;//还款总利息
        data.add(String.valueOf(totalMoney));//还款总额
        data.add(String.valueOf(principal));//贷款总额
        data.add(String.valueOf(interest));//还款总利息
        data.add(String.valueOf(preLoan));//每月还款金额
        data.add(String.valueOf(months));//还款期限
        return data.toArray(new String[data.size()]);
    }

    public static String[] calculateEqualPrincipal(double principal, int months, double rate) {
        List<String> data = new ArrayList<>();
        double monthRate = rate / (100 * 12);//月利率
        double prePrincipal = principal / months;//每月还款本金
        double firstMonth = prePrincipal + principal * monthRate;//第一个月还款金额
        double decreaseMonth = prePrincipal * monthRate;//每月利息递减
        double interest = (months + 1) * principal * monthRate / 2;//还款总利息
        double totalMoney = principal + interest;//还款总额
        data.add(String.valueOf(totalMoney));//还款总额
        data.add(String.valueOf(principal));//贷款总额
        data.add(String.valueOf(interest));//还款总利息
        data.add(String.valueOf(firstMonth));//首月还款金额
        data.add(String.valueOf(decreaseMonth));//每月递减利息
        data.add(String.valueOf(months));//还款期限
        return data.toArray(new String[data.size()]);
    }

//    public static void main(String[] args) {
//        calculateEqualPrincipalAndInterest(1000000, 30 * 12, 4.9);
//        System.out.println(111);
//
//
//        calculateEqualPrincipal(1000000, 30 * 12, 4.9);
//        System.out.println(222);
//    }

}
