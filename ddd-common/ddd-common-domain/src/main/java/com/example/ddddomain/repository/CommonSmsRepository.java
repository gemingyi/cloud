package com.example.ddddomain.repository;


import com.example.ddddomain.model.CommonSmsModel;

/**
 * <p>
 * 短信 Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
public interface CommonSmsRepository {


    /**
     * 更新短信使用状态
     */
    int updateIsUserByPhone(String phone, Integer isUser);

    /**
     * 根据手机查询z最新发送的短信
     */
    CommonSmsModel selectLatestByPhone(String phone);

}
