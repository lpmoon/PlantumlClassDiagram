package com.lpmoon.plantuml.classdiagram.clazz;

import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;

public class TypeUtil {
    public static String getType(Tree tree) {
        String type;
        if (tree instanceof ParameterizedTypeTree) {
            ParameterizedTypeTree typeTree = (ParameterizedTypeTree) tree;
            type = typeTree.getType().toString();
        } else {
            type = tree.toString();
        }

        return type;
    }
}
