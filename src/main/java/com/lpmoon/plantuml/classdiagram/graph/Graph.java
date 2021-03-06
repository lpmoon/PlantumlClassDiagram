package com.lpmoon.plantuml.classdiagram.graph;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Graph<T> {
    private Map<T, GraphNode> graphNodes;

    public Graph() {
        this.graphNodes = new HashMap<>();
    }

    public void add(T data) {
        if (!graphNodes.containsKey(data)) {
            GraphNode<T> node = new GraphNode<>(data);
            graphNodes.put(data, node);
        }
    }

    public void connect(T from, T to) {
        GraphNode<T> fromNode = graphNodes.get(from);
        GraphNode<T> toNode = graphNodes.get(to);

        if (fromNode == null) {
            fromNode = new GraphNode<>(from);
            graphNodes.put(from, fromNode);
        }

        if (toNode == null) {
            toNode = new GraphNode<>(to);
            graphNodes.put(to, toNode);
        }

        fromNode.getOutComings().add(toNode);
        toNode.getInComings().add(fromNode);
    }

    public List<T> findConnectedNodes(T dst, boolean in, boolean out) {
        List<T> datas = new ArrayList<>();

        GraphNode<T> node = graphNodes.get(dst);

        Set<T> processed = new HashSet<>();

        if (node == null) {
            return datas;
        }

        Queue<GraphNode<T>> queue = new LinkedBlockingQueue<>();
        queue.add(node);
        datas.add(dst);
        processed.add(dst);

        while (!queue.isEmpty()) {
            GraphNode<T> currentNode = queue.poll();
            if (in) {
                for (GraphNode<T> n : currentNode.getInComings()) {
                    if (!processed.contains(n.getData())) {
                        queue.add(n);
                        datas.add(n.getData());
                        processed.add(n.getData());
                    }
                }
            }

            if (out) {
                for (GraphNode<T> n : currentNode.getOutComings()) {
                    if (!processed.contains(n.getData())) {
                        queue.add(n);
                        datas.add(n.getData());
                        processed.add(n.getData());
                    }
                }
            }
        }

        return datas;
    }
}
