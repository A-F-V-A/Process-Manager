package edu.est.process.manager.domain.models;

import java.util.concurrent.Delayed;
import java.io.Serializable;
/**
 * Enumeración para las opciones de estado de una tarea.
 */
public enum TaskStatus {
    MANDATORY("Mandatory"),
    OPTIONAL("Optional"),
    DELAYED("Delayed"),
    COMPLETED("Completed"),
    PENDING("Pending");


    private final String status;

    /**
     * Constructor de TaskStatus.
     *
     * @param status El estado de la tarea.
     */
    TaskStatus(String status) {
        this.status = status;
    }

    /**
     * Devuelve el estado como una cadena.
     *
     * @return El estado en forma de cadena.
     */
    @Override
    public String toString() {
        return this.status;
    }


}
