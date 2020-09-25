package com.example.commons.label;

import java.io.Serializable;

/**
 * 枚举Data
 */
public class EnumsData implements Serializable {

    private static final long serialVersionUID = -4047302542887710079L;

    /** 名称 */
    private String label;
    /** 值 */
    private Object value;

    public EnumsData() {
    }

    public EnumsData(String name, Object value) {
        this.label = name;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EnumsData{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
