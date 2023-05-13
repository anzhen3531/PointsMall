package xyz.ziang.common.entity;

import java.util.List;

public class TreeNode<T> {

    /**
     * data
     */
    T data;

    /**
     * id
     */
    Long id;

    /**
     * 文件名称
     */
    String fileName;

    /**
     * 子节点集合
     */
    List<TreeNode<T>> childNodes;

    public TreeNode() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<TreeNode<T>> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<TreeNode<T>> childNodes) {
        this.childNodes = childNodes;
    }
}
