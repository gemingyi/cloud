package com.example.userserver;

import com.example.commons.utils.bean.compare.FieldInfo;
import com.example.commons.utils.bean.compare.GetterBaseEquator;
import com.example.userserver.dao.entity.UserInfo;
import com.example.userserver.dao.entity.UserLogin;

import java.util.List;

public class ObjectCompareTest {

    public static void main(String[] args) {
        GetterBaseEquator getterBaseEquator = new GetterBaseEquator();

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setPhone("123");
        userInfo1.setNickName("qwe");
        UserLogin userLogin1 = new UserLogin();
        userLogin1.setUsername("123");
//        userInfo1.setUserLogin(userLogin1);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setPhone("1234");
        UserLogin userLogin2 = new UserLogin();
        userLogin2.setUsername("456");
//        userInfo2.setUserLogin(userLogin2);

        List<FieldInfo> diffFields = getterBaseEquator.getDiffFields(userInfo1, userInfo2);
        System.out.println(diffFields);
    }
}
