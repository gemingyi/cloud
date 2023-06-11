package com.example.commons.utils.bean.compare;

import java.util.List;

/**
 * https://github.com/dadiyang/equator  工具类网上找的（稍微修改了下）
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/5/29 16:06$
 */
public interface  Equator {

    /**
     * 两个对象是否全相等
     *
     * @param first  对象1
     * @param second 对象2
     * @return 两个对象是否全相等
     */
    boolean isEquals(Object first, Object second);

    /**
     * 获取不相等的属性
     *
     * @param first  对象1
     * @param second 对象2
     * @return 不相等的属性，键为属性名，值为属性类型
     */
    List<FieldInfo> getDiffFields(Object first, Object second);

}
