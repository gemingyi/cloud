package com.example.ddddomain.model;


/**
 * <p>
 * 
 * </p>
 *
 * @author GMY_GENERATE
 * @since 2020-10-17
 */
public class TestModel {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Test{" +
        "id=" + id +
        ", name=" + name +
        "}";
    }
}
