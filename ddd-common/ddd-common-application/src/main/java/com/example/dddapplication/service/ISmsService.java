package com.example.dddapplication.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ddddomain.model.CommonSmsModel;

public interface ISmsService {

    /**
     * 发送短信
     */
    String sendSMS(String phone, Integer smsType);

    /**
     * 更新短信适用状态
     */
    int updateSmsIsUserByPhone(String phone);

    /**
     * 分页查询短信信息
     */
    IPage<CommonSmsModel> findPageSms(Page<CommonSmsModel> page);

    /**
     * 根据手机号 获取最新的短信信息
     */
    CommonSmsModel getLatestSmsByPhone(String phone);
}
