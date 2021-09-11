package com.example.userserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commons.IAbstractService;
import com.example.userserver.dao.mapper.UserInfoMapper;
import com.example.userserver.dao.entity.UserInfo;
import com.example.userserver.dao.entity.UserLogin;
import com.example.userserver.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    @Override
    public int testResult() {
        return 0;
    }

}
