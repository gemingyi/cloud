package com.example.ddddomain.stateProcessor.event;

public enum OrderStateEnum {

    WAIT_PAY(1, "待支付"),

    PAYED(2, "已支付"),
    ;


    private Integer state;

    private String stateDesc;

    OrderStateEnum(Integer state, String stateDesc) {
        this.state = state;
        this.stateDesc = stateDesc;
    }


    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

}
