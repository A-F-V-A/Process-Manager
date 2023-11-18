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
    private long starTime;
    private int durationMinutes;
    private boolean notification;


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
        this.starTime = System.currentTimeMillis();
        this.notification = false;
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
        long elapsedTime = System.currentTimeMillis() - starTime;
        int remainingTime = (int) ((durationMinutes * 60 * 1000 - elapsedTime) / (60 * 1000));
        return remainingTime;
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
     * Marks the task as completed.
     */
    public void completeTask() {
        Notification notification = new Notification();
        setStatus(TaskStatus.COMPLETED);
        notification.removeCompletedTask(this);
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
                ", notification= " + notification +
                '}';
    }
}


