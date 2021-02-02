package com.example.commons.utils;

import com.ejlchina.okhttps.HttpCall;
import com.ejlchina.okhttps.HttpUtils;
import com.ejlchina.okhttps.OkHttps;

import java.util.HashMap;
import java.util.Map;


/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/2/1 19:25
 */
public class OkHttpUtil {

//    public static void main(String[] args) {
//        HttpCall httpCall = OkHttps.async("https://www.baidu.com/").get();
//        System.out.println(httpCall.getResult().getBody().toString());
//    }

    public static void main(String[] args) {

        Map<String, String> map = new HashMap<>();
        map.put("debtType", "1");
        map.put("debtAmt", "1");
        HttpCall httpCall = HttpUtils.async("https://flgate-uat.fujfu.com/api/v1/loan/debtApply").addBodyPara(map).bodyType(OkHttps.JSON).post();

//        HttpCall httpCall = HttpUtils.async("https://flgate-uat.fujfu.com/api/v1/loan/debtApply").setBodyPara(AliJsonUtil.objectToJsonStr(buyOutDTO)).bodyType(OkHttps.JSON).post();
//        HttpCall httpCall = HttpUtils.async("https://flgate-uat.fujfu.com/api/v1/loan/debtApply").setBodyPara(buyOutDTO).bodyType(OkHttps.JSON).post();
        System.out.println(httpCall.getResult().getBody().toString());
    }


}
