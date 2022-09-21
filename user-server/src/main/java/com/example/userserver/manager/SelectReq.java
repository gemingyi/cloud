package com.example.userserver.manager;

import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2022/9/14 15:19
 */
@Getter
@Setter
public class SelectReq {

    private SelectReqOne selectReqOne;

    private SelectReqTwo selectReqTwo;

    private SelectReqThree selectReqThree;




    @Getter
    @Setter
    public static class SelectReqOne {

    }

    @Getter
    @Setter
    public static class SelectReqTwo {

    }

    @Getter
    @Setter
    public static class SelectReqThree {

    }
}


