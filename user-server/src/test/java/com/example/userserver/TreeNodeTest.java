package com.example.userserver;

import com.example.commons.tree.BaseTreeNode;
import com.example.commons.tree.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: mingyi ge
 * @date: 2021/9/15 15:36
 */
public class TreeNodeTest {

    public static void main(String[] args) {
        List<BaseTreeNode> testNodeList = new ArrayList<>(10);
        for (int i = 1; i <= 9; i++) {
            TestNode testNode = new TestNode();
            testNode.setId(i);
            if (i % 3 == 1) {
                testNode.setParentId(0);
                testNode.setName("一级分类");
            }
            if (i % 3 == 2) {
                testNode.setParentId(i - 1);
                testNode.setName("二级分类");
            }
            if (i % 3 == 0) {
                testNode.setParentId(i - 1);
                testNode.setName("三级分类");
            }
            testNodeList.add(testNode);
        }

        System.out.println(testNodeList);

        TreeNodeUtil treeNodeUtil = new TreeNodeUtil(testNodeList);
        List<BaseTreeNode> baseTreeNodes = treeNodeUtil.buildTree();
        System.out.println(baseTreeNodes);

    }
}


class TestNode extends BaseTreeNode {

    private String name ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}