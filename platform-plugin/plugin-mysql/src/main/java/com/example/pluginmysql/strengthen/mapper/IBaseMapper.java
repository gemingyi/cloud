package com.example.pluginmysql.strengthen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * mybatis-plus 增强IBaseMapper
 */
public interface IBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量保存
     *
     * @param list 实体对象集合
     * @return 修改的条数
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     * 批量更新
     * @param list  实体对象集合
     * @return  修改的条数
     */
    int updateBatch(@Param("list") List<T> list);

}
