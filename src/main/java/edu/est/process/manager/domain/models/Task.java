package edu.est.process.manager.domain.models;


import com.google.gson.JsonObject;
import edu.est.process.manager.domain.util.IDGenerator;
import edu.est.process.manager.infrastructure.javafx.util.Notification;

import java.util.Objects;

/**
 * Representa una tarea dentro de un proceso o actividad.
 */
public class Task {
    private String description;
    private String id;
    private TaskStatus status;
    private long starTime;
    private int durationMinutes;
    private boolean notification;

    /**
     * Constructor por defecto para Task.
     */
    public Task(){}


    /**
     * Constructor para crear una nueva instancia de Task.
     *
     * @param description     Una cadena que describe la tarea.
     * @param status          El estado de la tarea, utilizando el enum TaskStatus.
     * @param durationMinutes La duración estimada de la tarea en minutos.
     */
    public Task(String description, TaskStatus status, int durationMinutes) {
        this.description = description;
        this.status = status;
        this.durationMinutes = durationMinutes;
        this.starTime = System.currentTimeMillis();
        this.notification = false;
        this.id = IDGenerator.generateID();
    }

    /**
     * Obtiene la duración restante de la tarea en minutos.
     *
     * @return la duración restante de la tarea en minutos.
     */
    public int getDurationMinutes() {
        long elapsedTime = System.currentTimeMillis() - starTime;
        int remainingTime = (int) ((durationMinutes * 60 * 1000 - elapsedTime) / (60 * 1000));
        return remainingTime;
    }


    /**
     * Marca la tarea como completada y la elimina de las notificaciones.
     */
    public void completeTask() {
        Notification notification = new Notification();
        setStatus(TaskStatus.COMPLETED);
        notification.removeCompletedTask(this);
    }

    /**
     * Convierte la tarea a un objeto JSON.
     *
     * @return un JsonObject que representa la tarea.
     */
    public JsonObject toJson(){
        JsonObject task = new JsonObject();
        task.addProperty("id",id);
        task.addProperty("status",status.toString());
        task.addProperty("description",description);
        task.addProperty("durationMinutes",durationMinutes);
        return task;
    }

    /**
     * Obtiene el ID de la tarea.
     *
     * @return el ID de la tarea.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDuration() {
        return durationMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public long getStarTime() {
        return starTime;
    }

    public void setStarTime(long starTime) {
        this.starTime = starTime;
    }

    /**
     * Sobrescribe el método toString() para representar Task como una cadena.
     *
     * @return una representación en cadena de Task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", status=" + status +
                ", durationMinutes=" + durationMinutes +
                ", notification= " + notification +
                '}';
    }

    /**
     * Sobrescribe el método equals() para verificar la igualdad entre instancias de Task.
     *
     * @param o el objeto para comparar la igualdad.
     * @return true si son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(getId(), task.getId());
    }

    /**
     * Sobrescribe el método hashCode() para generar un código hash para instancias de Task.
     *
     * @return el código hash generado para Task.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}


