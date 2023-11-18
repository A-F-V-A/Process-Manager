package edu.est.process.manager.domain.models;

import java.util.concurrent.Delayed;
import java.io.Serializable;
/**
 * Enumeration for task status options.
 */
public enum TaskStatus {
    MANDATORY("Mandatory"),
    OPTIONAL("Optional"),
    DELAYED("Delayed"),
    COMPLETED("Completed"),
    PENDING("Pending");


    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }


}
