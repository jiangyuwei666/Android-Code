package com.example.a6100890.myapplication.model;

public class Node implements Comparable<Node> {
    private Node leftChild;
    private Data data;
    private Node rightChild;


    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * 根据Data的频率比较
     * @param o
     * @return
     */
    @Override
    public int compareTo(Node o) {
        return this.data.compareTo(o.getData());
    }
}
