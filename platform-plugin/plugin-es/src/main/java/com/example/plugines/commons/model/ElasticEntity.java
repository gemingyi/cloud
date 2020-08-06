package com.example.plugines.commons.model;

import java.io.Serializable;

/**
 * 索引model父类
 * Created by gmy on 2018/7/1.
 */
public class ElasticEntity implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
