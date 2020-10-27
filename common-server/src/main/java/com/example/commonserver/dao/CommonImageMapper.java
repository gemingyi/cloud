package com.example.commonserver.dao;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.commonserver.model.CommonImage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.commonserver.model.CommonSms;

/**
 * <p>
 * 图片oss Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
public interface CommonImageMapper extends BaseMapper<CommonImage> {

    /**
     * 逻辑删除
     */
    default int updateIsDeleteById(Integer imageId) {
        UpdateWrapper<CommonImage> wrapper = new UpdateWrapper<>();
        wrapper.set("is_delete", 1);
        wrapper.eq("id", imageId);
        return this.update(null, wrapper);
    }

}
