package edu.est.process.manager.domain.models;


/**
 * Represents a task within a process or activity.
 */
public class Task {
    private String description;
    private TaskStatus status;
    private int durationMinutes;

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


    @Override
    public String toString() {
        return "Task{" +
                "description='" + description + '\'' +
                ", status=" + status +
                ", durationMinutes=" + durationMinutes +
                '}';
    }
}


