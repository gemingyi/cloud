package com.example.commonserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.pluginmysql.model.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
public class Test extends BaseModel {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
        "id=" + id +
        ", name=" + name +
        "}";
    }
}
