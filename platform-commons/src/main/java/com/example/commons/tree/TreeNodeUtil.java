package com.example.commons.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:    树形结构工具类
 * @author: mingyi ge
 * @date: 2021/9/15 15:27
 */
public class TreeNodeUtil {

    /**
     *
     */
    private List<BaseTreeNode> treeNodeList;


    public TreeNodeUtil(List<BaseTreeNode> treeNodes) {
        this.treeNodeList = treeNodes;
    }


    /**
     * 构建树形结构
     */
    public List<BaseTreeNode> buildTree() {
        List<BaseTreeNode> rootNodeList = getRootNodeList();
        List<BaseTreeNode> resultTreeList = new ArrayList<>(rootNodeList.size());
        for (BaseTreeNode treeNode : rootNodeList) {
            buildChildTree(treeNode);
            resultTreeList.add(treeNode);
        }
        return resultTreeList;
    }

//    /**
//     * 构建 指定ID 树形结构
//     * @param id    指定节点ID
//     */
//    public BaseTreeNode buildTreeById(Integer id) {
//        Optional<BaseTreeNode> first = treeNodeList.stream().filter(e -> e.getId().equals(id)).findFirst();
//        if (!first.isPresent()) {
//            return null;
//        }
//        BaseTreeNode treeNode = first.get();
//        buildChildTree(treeNode);
//        return treeNode;
//    }


    /**
     * 递归，建立子树形结构
     * @param parentNode 父节点
     */
    private BaseTreeNode buildChildTree(BaseTreeNode parentNode) {
        List<BaseTreeNode> childTreeNodeList = new ArrayList<>();
        for (BaseTreeNode treeNode : treeNodeList) {
            if (parentNode.getId().equals(treeNode.getParentId())) {
                childTreeNodeList.add(buildChildTree(treeNode));
            }
        }
        parentNode.setChildNodeList(childTreeNodeList);
        return parentNode;
    }

    /**
     * 获取根节点
     */
    private List<BaseTreeNode> getRootNodeList() {
        List<BaseTreeNode> rootNodeList = new ArrayList<>();
        for (BaseTreeNode treeNode : treeNodeList) {
            if (treeNode.getParentId().equals(0) || null == treeNode.getParentId()) {
                rootNodeList.add(treeNode);
            }
        }
        return rootNodeList;
    }

}
