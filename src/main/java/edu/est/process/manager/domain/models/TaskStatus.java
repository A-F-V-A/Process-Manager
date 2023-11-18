package edu.est.process.manager.domain.models;

import java.io.Serializable;

/**
 * Enumeration for task status options.
 */
public enum TaskStatus {
    MANDATORY("Mandatory"),
    OPTIONAL("Optional"),
    COMPLETED("Completed");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
