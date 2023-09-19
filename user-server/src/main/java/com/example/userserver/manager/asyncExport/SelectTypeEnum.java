package com.example.userserver.manager.asyncExport;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 15:17
 */
public enum SelectTypeEnum {

    TEST_ONE(1,"测试导出类型1"),
    TEST_TWO(2,"测试导出类型2"),
    TEST_THREE(3,"测试导出类型3"),
    ;


    private Integer code;

    private String name;

    SelectTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public static SelectTypeEnum getExportTypeEnum(String code) {
        SelectTypeEnum[] exportTypeEnums = SelectTypeEnum.values();
        for (SelectTypeEnum exportTypeEnum : exportTypeEnums) {
            if (exportTypeEnum.getCode().equals(code)) {
                return exportTypeEnum;
            }
        }
        return null;
    }
}
