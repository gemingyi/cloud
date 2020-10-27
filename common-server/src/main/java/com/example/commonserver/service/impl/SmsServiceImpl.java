//package com.example.commonserver.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.commonserver.dao.CommonSmsMapper;
//import com.example.commonserver.model.CommonSms;
//import com.example.commonserver.service.ISmsService;
//import com.example.commonserver.sms.SMSUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SmsServiceImpl implements ISmsService {
//
//    @Autowired
//    private CommonSmsMapper commonSmsMapper;
//    @Autowired
//    private SMSUtil smsUtil;
//
//
//    @Override
//    public String sendSMS(String phone, Integer smsType) {
//        // 校验号码每天发送上限
//
//        // 发送短信验证码
//        String code = smsUtil.generateRandomNumber(4);
//        smsUtil.send(phone, String.valueOf(smsType), code);
//        //
//        CommonSms commonSms = new CommonSms();
//        commonSms.setIsUser(0);
//        commonSms.setSmsPhone(phone);
//        commonSms.setSmsType(smsType);
//        commonSmsMapper.insert(commonSms);
//        return code;
//    }
//
//    @Override
//    public int updateSmsIsUserByPhone(String phone){
//        return commonSmsMapper.updateIsUserByPhone(phone, 1);
//    }
//
//    @Override
//    public IPage<CommonSms> findPageSms(Page<CommonSms> page) {
//        QueryWrapper<CommonSms> queryWrapper = new QueryWrapper<>();
//
//        return commonSmsMapper.selectPage(page, queryWrapper);
//    }
//
//    @Override
//    public CommonSms getLatestSmsByPhone(String phone) {
//        return commonSmsMapper.selectLatestByPhone(phone);
//    }
//
//}
