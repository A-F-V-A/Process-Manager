package edu.est.process.manager.domain.models;


import edu.est.process.manager.domain.structures.CustomQueue;
import edu.est.process.manager.domain.util.IDGenerator;

import java.util.List;

public class Activity {
    private String id;
    private String name;
    private CustomQueue<Task> pendingTasks;
    private CustomQueue<Task> completedTasks;

    public Activity(String name) {
        this.id = IDGenerator.generateID(); // Genera un ID único para la actividad
        this.name = name;
        this.pendingTasks = new CustomQueue<>();
        this.completedTasks = new CustomQueue<>();
    }

    public void addTask(Task task) {
        pendingTasks.enqueue(task);
    }

    public Task completeTask() {
        Task completedTask = pendingTasks.dequeue();
        if (completedTask != null) {
            completedTasks.enqueue(completedTask);
            completedTask.completeTask();
        }
        return completedTask;
    }

    public CustomQueue<Task> getPendingTasks() {
        return pendingTasks;
    }

    public CustomQueue<Task> getCompletedTasks() {
        return completedTasks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Método que devuelve todas las tareas pendientes en forma de lista
    public List<Task> getPendingTasksAsList() {
        return pendingTasks.toList();
    }

    // Método que devuelve todas las tareas completadas en forma de lista
    public List<Task> getCompletedTasksAsList() {
        return completedTasks.toList();
    }

    /**
     * Calcula y devuelve la duración total de la actividad.
     *
     * @return La duración total en minutos de todas las tareas (pendientes y completadas).
     */
    public int getTotalDurationMinutes() {
        int totalDuration = 0;
        for (Task task : pendingTasks.toList()) totalDuration += task.getDurationMinutes();

        for (Task task : completedTasks.toList()) totalDuration += task.getDurationMinutes();

        return totalDuration;
    }
}

