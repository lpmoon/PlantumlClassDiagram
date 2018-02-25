package com.lpmoon.plantuml.classdiagram.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphNode<T> {

    private T data;

    private List<GraphNode<T>> inComings;

    private List<GraphNode<T>> outComings;

    public GraphNode(T data) {
        this.data = data;
        this.inComings = new ArrayList<>();
        this.outComings = new ArrayList<>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<GraphNode<T>> getInComings() {
        return inComings;
    }

    public void setInComings(List<GraphNode<T>> inComings) {
        this.inComings = inComings;
    }

    public List<GraphNode<T>> getOutComings() {
        return outComings;
    }

    public void setOutComings(List<GraphNode<T>> outComings) {
        this.outComings = outComings;
    }

    @Override
    public boolean equals(Object obj) {
        return data.equals(((GraphNode) obj).data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
