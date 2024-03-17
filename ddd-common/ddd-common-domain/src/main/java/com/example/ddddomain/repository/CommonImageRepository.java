package com.example.ddddomain.repository;

/**
 * <p>
 * 图片oss Mapper 接口
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-24
 */
public interface CommonImageRepository {

    /**
     * 逻辑删除
     */
    int updateIsDeleteById(Integer imageId);

}
