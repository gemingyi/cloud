package com.example.ddddomain.stateProcessor.state;

public enum  OrderEventEnum {

    PAY(1, "用户支付"),

    APPLY_FOR_REFUND(2, "申请退款"),
            ;

    private Integer event;

    private String eventDesc;


    OrderEventEnum(Integer event, String eventDesc) {
        this.event = event;
        this.eventDesc = eventDesc;
    }


    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
}
