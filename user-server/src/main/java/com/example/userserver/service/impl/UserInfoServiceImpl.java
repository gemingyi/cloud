package com.example.userserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commons.IAbstractService;
import com.example.userserver.dao.UserInfoMapper;
import com.example.userserver.model.UserInfo;
import com.example.userserver.model.UserLogin;
import com.example.userserver.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserInfoServiceImpl implements IAbstractService<UserInfo>, IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public int create(UserInfo userInfo) {
        return userInfoMapper.insert(userInfo);
    }

    @Override
    public int deleteById(Integer id) {
        return userInfoMapper.deleteById(id);
    }

    @Override
    public int updateById(UserInfo userInfo) {
        return userInfoMapper.updateById(userInfo);
    }

    @Override
    public List<UserInfo> listAll(UserInfo userInfo) {
        return null;
    }

    @Override
    public UserInfo getById(Integer id) {
        return userInfoMapper.selectById(id);
    }


    @Override
    public int saveUserInfoAndLogin(UserInfo userInfo, UserLogin userLogin) {
        return 0;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> page, UserInfo userInfo) {
        return null;
    }

}
