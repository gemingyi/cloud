package com.example.dddinfrastructure.repository.impl;

import com.example.ddddomain.model.CommonSmsModel;
import com.example.ddddomain.repository.CommonSmsRepository;
import com.example.dddinfrastructure.eo.CommonSms;
import com.example.dddinfrastructure.mapper.CommonSmsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 短信 Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
@Repository
public class CommonSmsRepositoryImpl implements CommonSmsRepository {

    @Autowired
    private CommonSmsMapper commonImageMapper;

    /**
     * 更新短信使用状态
     */
    @Override
    public int updateIsUserByPhone(String phone, Integer isUser) {
        return commonImageMapper.updateIsUserByPhone(phone, isUser);
    }

    /**
     * 根据手机查询z最新发送的短信
     */
    @Override
    public CommonSmsModel selectLatestByPhone(String phone) {
        CommonSms commonSms = commonImageMapper.selectLatestByPhone(phone);
        CommonSmsModel commonSmsModel = new CommonSmsModel();
        BeanUtils.copyProperties(commonSms, commonSmsModel);
        return commonSmsModel;
    }

}
