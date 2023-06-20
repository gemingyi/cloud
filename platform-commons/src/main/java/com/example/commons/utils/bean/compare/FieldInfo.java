package com.example.commons.utils.bean.compare;

import java.util.Objects;

/**
 * @Description:
 * @Author: mingyi ge
 * @CreateDate: 2019/5/29 16:07$
 */
public class FieldInfo {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 第一个字段的类型
     */
    private Class<?> firstFieldType;
    /**
     * 第二个字段的类型
     */
    private Class<?> secondFieldType;
    /**
     * 第一个对象的值
     */
    private Object firstVal;
    /**
     * 第二个对象的值
     */
    private Object secondVal;

    public FieldInfo() {
    }

    public FieldInfo(String fieldName, Class<?> firstFieldType, Class<?> secondFieldType) {
        this.fieldName = fieldName;
        this.firstFieldType = firstFieldType;
        this.secondFieldType = secondFieldType;
    }

    public FieldInfo(String fieldName, Class<?> fieldType, Object firstVal, Object secondVal) {
        this.fieldName = fieldName;
        this.firstFieldType = fieldType;
        this.secondFieldType = fieldType;
        this.firstVal = firstVal;
        this.secondVal = secondVal;
    }

    public FieldInfo(String fieldName, Class<?> firstFieldType, Class<?> secondFieldType, Object firstVal, Object secondVal) {
        this.fieldName = fieldName;
        this.firstFieldType = firstFieldType;
        this.secondFieldType = secondFieldType;
        this.firstVal = firstVal;
        this.secondVal = secondVal;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Class<?> getFirstFieldType() {
        return firstFieldType;
    }

    public void setFirstFieldType(Class<?> firstFieldType) {
        this.firstFieldType = firstFieldType;
    }

    public Object getFirstVal() {
        return firstVal;
    }

    public void setFirstVal(Object firstVal) {
        this.firstVal = firstVal;
    }

    public void setSecondFieldType(Class<?> secondFieldType) {
        this.secondFieldType = secondFieldType;
    }

    public Class<?> getSecondFieldType() {
        return secondFieldType;
    }

    public Object getSecondVal() {
        return secondVal;
    }

    public void setSecondVal(Object secondVal) {
        this.secondVal = secondVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(fieldName, fieldInfo.fieldName) &&
                Objects.equals(firstFieldType, fieldInfo.firstFieldType) &&
                Objects.equals(secondFieldType, fieldInfo.secondFieldType) &&
                Objects.equals(firstVal, fieldInfo.firstVal) &&
                Objects.equals(secondVal, fieldInfo.secondVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, firstFieldType, secondFieldType, firstVal, secondVal);
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "fieldName='" + fieldName + '\'' +
                ", firstFieldType=" + firstFieldType +
                ", secondFieldType=" + secondFieldType +
                ", firstVal=" + firstVal +
                ", secondVal=" + secondVal +
                '}';
    }
}
