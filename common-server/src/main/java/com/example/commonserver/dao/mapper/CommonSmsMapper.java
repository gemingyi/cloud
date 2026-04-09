package com.example.commonserver.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.commonserver.dao.entity.CommonSms;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 短信 Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
public interface CommonSmsMapper extends BaseMapper<CommonSms> {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 更新短信使用状态
     */
    default int updateIsUserByPhone(String phone, Integer isUser) {
        UpdateWrapper<CommonSms> wrapper = new UpdateWrapper<>();
        wrapper.set("is_user", isUser);
        wrapper.eq("sms_phone", phone);
        return this.update(null, wrapper);
    }

    /**
     * 根据手机查询最新发送的短信
     */
    default CommonSms selectLatestByPhone(String phone, Integer smsType) {
        QueryWrapper<CommonSms> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.eq("sms_phone", phone);
        queryWrapper.eq("sms_type", smsType);
        return this.selectOne(queryWrapper);
    }

    default Integer countSameDayByPhone(String phone, Integer smsType) {
        QueryWrapper<CommonSms> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sms_phone", phone);
        queryWrapper.eq("sms_type", smsType);
        queryWrapper.eq("create_time", sdf.format(new Date()));
        return this.selectCount(queryWrapper);
    }
}
