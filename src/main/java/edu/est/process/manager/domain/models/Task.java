package edu.est.process.manager.domain.models;


import com.google.gson.JsonObject;
import edu.est.process.manager.domain.util.IDGenerator;

import java.io.Serializable;

/**
 * Represents a task within a process or activity.
 */
public class Task {
    private String description;
    private String id;
    private TaskStatus status;
    private int durationMinutes;


    public Task(){}


    /**
     * Constructor for creating a new Task instance.
     *
     * @param description     A string describing the task.
     * @param status          The status of the task, using the TaskStatus enum.
     * @param durationMinutes The estimated duration of the task in minutes.
     */
    public Task(String description, TaskStatus status, int durationMinutes) {
        this.description = description;
        this.status = status;
        this.durationMinutes = durationMinutes;
        this.id = IDGenerator.generateID();
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

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * Marks the task as completed.
     */
    public void completeTask() {
        setStatus(TaskStatus.COMPLETED);
    }


    public JsonObject toJson(){
        JsonObject task = new JsonObject();
        task.addProperty("id",id);
        task.addProperty("status",status.toString());
        task.addProperty("description",description);
        task.addProperty("durationMinutes",durationMinutes);
        return task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", status=" + status +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}


