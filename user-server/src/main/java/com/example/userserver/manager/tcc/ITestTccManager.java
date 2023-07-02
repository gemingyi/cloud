package com.example.userserver.manager.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.math.BigDecimal;

@LocalTCC
public interface ITestTccManager {


    /**
     * seata tcc prepare
     */
    @TwoPhaseBusinessAction(name = "prepareSaveTest", commitMethod = "commitSaveTest", rollbackMethod = "cancelSaveTest")
    boolean prepareSaveTest(BusinessActionContext businessActionContext,
                        @BusinessActionContextParameter(paramName = "tradeNo") String tradeNo,
                        @BusinessActionContextParameter(paramName = "name") String name,
                        @BusinessActionContextParameter(paramName = "price") BigDecimal price);

    /**
     * seata tcc commit
     */
    boolean commitSaveTest(BusinessActionContext businessActionContext);

    /**
     * seata tcc cancel
     */
    boolean cancelSaveTest(BusinessActionContext businessActionContext);

}
