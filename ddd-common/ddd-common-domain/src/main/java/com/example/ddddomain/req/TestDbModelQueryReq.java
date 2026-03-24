package com.example.ddddomain.req;

import com.example.pluginmysql.model.page.PageQO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TestDbModelQueryReq extends PageQO implements Serializable {

    private String name;

}
