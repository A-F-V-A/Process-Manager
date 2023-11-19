package edu.est.process.manager.infrastructure.javafx.util;

import javafx.scene.Node;

import java.util.function.Predicate;

public interface NodeCondition extends Predicate<Node> {
    boolean test(Node node);
}