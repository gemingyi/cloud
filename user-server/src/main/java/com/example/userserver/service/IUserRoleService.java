package com.example.userserver.service;

import java.util.List;

public interface IUserRoleService {

    /**
     * 用户绑定角色
     * @param userId
     * @param roleIds
     */
    int saveUserRelRole(Integer userId, List<Integer> roleIds);
}
