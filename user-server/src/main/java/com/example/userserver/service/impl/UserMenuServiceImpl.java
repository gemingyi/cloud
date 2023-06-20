package com.example.userserver.service.impl;

import com.example.commons.IAbstractService;
import com.example.userserver.dao.mapper.UserMenuMapper;
import com.example.userserver.dao.mapper.UserRelRoleMapper;
import com.example.userserver.dao.entity.UserMenu;
import com.example.userserver.service.IUserMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMenuServiceImpl implements IAbstractService<UserMenu>, IUserMenuService {

    @Autowired
    private UserMenuMapper userMenuMapper;
    @Autowired
    private UserRelRoleMapper userRelRoleMapper;


    @Override
    public int create(UserMenu userMenu) {
        return 0;
    }

    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int updateById(UserMenu userMenu) {
        return 0;
    }

    @Override
    public List<UserMenu> listAll(UserMenu userMenu) {
        return null;
    }

    @Override
    public UserMenu getById(Integer id) {
        return null;
    }


    @Override
    public int saveRoleRelMenu(Integer roleId, List<Integer> menuIds) {
        return 0;
    }

}
