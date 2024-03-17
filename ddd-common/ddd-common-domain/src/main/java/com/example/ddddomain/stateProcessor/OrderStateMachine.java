package com.example.ddddomain.stateProcessor;

import com.example.ddddomain.stateProcessor.processor.IPerformProcessor;

import java.util.List;

public class OrderStateMachine {

    private String name;

    private List<StateFlowConf> stateFlowConfList;


    public void setName(String name) {
        this.name = name;
    }

    public void setStateFlowConfList(List<StateFlowConf> stateFlowConfList) {
        this.stateFlowConfList = stateFlowConfList;
    }



    public void perform(String orderId, StateContext context) {

        StateFlowConf targetStateFlowConf = null;
        for (StateFlowConf stateFlowConf : stateFlowConfList) {
            // 基于单据的初态state，和event，去找到目标状态，和目标IPerformProcessor
            // 然后带着目标状态执行IPerformProcessor，在transition中修改单据状态
            if (stateFlowConf.getSource().equals(context.getSource()) &&
                    stateFlowConf.getOnEvent().equals(context.getOnEvent())) {
                targetStateFlowConf = stateFlowConf;
            }
        }
        if (targetStateFlowConf != null) {
            context.setTarget(targetStateFlowConf.getTarget());
            IPerformProcessor<String, StateContext> performProcessor = targetStateFlowConf.getPerformProcessor();
            this.exec(orderId, performProcessor, context);
        }
    }

    public void exec(String orderId, IPerformProcessor<String, StateContext> performProcessor, StateContext context) {
        //
        boolean validate = performProcessor.validate(orderId, context);
        if (!validate) {
            return;
        }
        //
        performProcessor.action(orderId, context);
    }

}
