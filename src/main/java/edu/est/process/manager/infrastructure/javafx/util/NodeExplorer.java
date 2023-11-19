package edu.est.process.manager.infrastructure.javafx.util;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;

public class NodeExplorer {

    public static List<Node> findNodes(Node node, NodeCondition condition) {
        List<Node> matchingNodes = new ArrayList<>();
        traverseNodes(node, condition, matchingNodes);
        return matchingNodes;
    }

    /*
    *   Encontrar todos los nodos con una clase específica
        List<Node> nodesWithClass = NodeExplorer.findNodes(root, node -> node.getStyleClass().contains("mi-clase-especifica"));

        Encontrar todos los nodos con un ID específico
        List<Node> nodesWithId = NodeExplorer.findNodes(root, node -> "mi-id-especifico".equals(node.getId()));
    * */

    private static void traverseNodes(Node node, NodeCondition condition, List<Node> matchingNodes) {
        if (condition.test(node)) {
            matchingNodes.add(node);
        }

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            for (Node child : parent.getChildrenUnmodifiable()) {
                traverseNodes(child, condition, matchingNodes);
            }
        }
    }
}
