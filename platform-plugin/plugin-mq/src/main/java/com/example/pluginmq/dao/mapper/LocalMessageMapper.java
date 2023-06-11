package com.example.pluginmq.dao.mapper;

import com.example.pluginmq.dao.entity.LocalMessage;
import com.example.pluginmysql.strengthen.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocalMessageMapper extends IBaseMapper<LocalMessage> {

    List<LocalMessage> selectPage(@Param("startIndex") Long startIndex, @Param("endIndex") Long endIndex);
}