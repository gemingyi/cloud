package com.example.commons.tree;

import java.io.Serializable;
import java.util.List;

/**
 * @description:    树形结构 model
 * @author: mingyi ge
 * @date: 2021/9/15 15:24
 */
public class BaseTreeNode implements Serializable {

    /**
     * 节点ID
     */
    private Integer id;

    /**
     * 节点父ID
     */
    private Integer parentId;

    /**
     * 节点的子节点集合
     */
    private List<BaseTreeNode> childNodeList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<BaseTreeNode> getChildNodeList() {
        return childNodeList;
    }

    public void setChildNodeList(List<BaseTreeNode> childNodeList) {
        this.childNodeList = childNodeList;
    }
}
