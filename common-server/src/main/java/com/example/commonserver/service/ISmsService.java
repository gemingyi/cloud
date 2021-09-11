package com.example.commonserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commonserver.dao.entity.CommonSms;

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
    IPage<CommonSms> findPageSms(Page<CommonSms> page);

    /**
     * 根据手机号 获取最新的短信信息
     */
    CommonSms getLatestSmsByPhone(String phone);
}
