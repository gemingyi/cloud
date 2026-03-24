package com.example.ddddomain.model;


import lombok.Getter;
import lombok.Setter;
import org.example.plugindesensitize.annotation.SensitiveInfo;
import org.example.plugindesensitize.enums.SensitiveTypeEnum;

/**
 * <p>
 * 
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
@Getter
@Setter
public class TestDbModel {

    private static final long serialVersionUID=1L;

    private Integer id;

    private Long userId;

    @SensitiveInfo(SensitiveTypeEnum.REAL_NAME)
    private String name;

}
