package com.example.commons.model;

import java.io.Serializable;

/**
 * 枚举Data
 */
public class EnumsData implements Serializable {

    private static final long serialVersionUID = -4047302542887710079L;

    /** 名称 */
    private String name;
    /** 值 */
    private String value;

    public EnumsData() {
    }

    public EnumsData(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnumsData{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
