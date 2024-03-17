package com.example.dddinfrastructure.repository.impl;

import com.example.ddddomain.repository.CommonImageRepository;
import com.example.dddinfrastructure.mapper.CommonImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 图片oss Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
@Repository
public class CommonImageRepositoryImpl implements CommonImageRepository {

    @Autowired
    private CommonImageMapper commonImageMapper;

    /**
     * 逻辑删除
     */
    @Override
    public int updateIsDeleteById(Integer imageId) {
        return commonImageMapper.updateIsDeleteById(imageId);
    }

}
