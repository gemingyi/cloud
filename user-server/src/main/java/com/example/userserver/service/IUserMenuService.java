package com.example.userserver.service;

import java.util.List;

public interface IUserMenuService {

    /**
     * 角色綁定菜单
     * @param roleId
     * @param menuIds
     */
    int saveRoleRelMenu(Integer roleId, List<Integer> menuIds);

}
