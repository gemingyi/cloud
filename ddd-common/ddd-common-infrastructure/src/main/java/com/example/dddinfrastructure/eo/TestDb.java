package com.example.dddinfrastructure.eo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.pluginmysql.model.BaseModel;
import lombok.Getter;
import lombok.Setter;

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
public class TestDb extends BaseModel {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long userId;

    private String name;

}
