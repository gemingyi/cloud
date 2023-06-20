package com.example.userserver.service.impl;

import com.example.commons.IAbstractService;
import com.example.userserver.dao.mapper.UserRelRoleMenuMapper;
import com.example.userserver.dao.mapper.UserRoleMapper;
import com.example.userserver.dao.entity.UserRole;
import com.example.userserver.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements IAbstractService<UserRole>, IUserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserRelRoleMenuMapper userRelRoleMenuMapper;


    @Override
    public int create(UserRole userRole) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int updateById(UserRole userRole) {
        return 0;
    }

    @Override
    public List<UserRole> listAll(UserRole userRole) {
        return null;
    }

    @Override
    public UserRole getById(Integer id) {
        return null;
    }


    @Override
    public int saveUserRelRole(Integer userId, List<Integer> roleIds) {
        return 0;
    }

}
