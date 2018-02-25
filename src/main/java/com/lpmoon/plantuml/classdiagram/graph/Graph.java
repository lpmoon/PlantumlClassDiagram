package com.lpmoon.plantuml.classdiagram.graph;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Graph<T> {
    private Map<T, GraphNode> graphNodes;

    public Graph() {
        this.graphNodes = new HashMap<>();
    }

    public void add(T data) {
        GraphNode<T> node = new GraphNode<>(data);
        graphNodes.put(data, node);
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

    public List<T> findConnectedNodes(T dst) {
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
            for (GraphNode<T> n : currentNode.getInComings()) {
                if (!processed.contains(n.getData())) {
                    queue.add(n);
                    datas.add(n.getData());
                    processed.add(n.getData());
                }
            }

            for (GraphNode<T> n : currentNode.getOutComings()) {
                if (!processed.contains(n.getData())) {
                    queue.add(n);
                    datas.add(n.getData());
                    processed.add(n.getData());
                }
            }
        }

        return datas;
    }

//    public static void com.lpmoon.com.lpmoon.plantuml.classdiagram.plantuml.classdiagram.main(String[] args) {
//        Graph<String> g = new Graph<>();
//        g.add("a");
//        g.add("b");
//        g.add("c");
//        g.add("d");
//
//        g.connect("a", "b");
//        g.connect("a", "c");
//
//        System.out.println(g.findConnectedNodes("a"));
//    }
}
