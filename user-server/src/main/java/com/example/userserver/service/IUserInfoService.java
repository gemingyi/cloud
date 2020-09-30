package com.example.userserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.userserver.model.UserInfo;
import com.example.userserver.model.UserLogin;


public interface IUserInfoService {


    int saveUserInfoAndLogin(UserInfo userInfo, UserLogin userLogin);

    IPage<UserInfo> listPage(Page<UserInfo> page, UserInfo userInfo);

    int testResult();

}
