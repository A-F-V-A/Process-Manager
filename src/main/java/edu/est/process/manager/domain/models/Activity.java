package edu.est.process.manager.domain.models;


import edu.est.process.manager.domain.structures.CustomQueue;
import edu.est.process.manager.domain.util.IDGenerator;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa una actividad en el sistema.
 */
public class Activity  {
    private String id;
    private String name;
    private String description;
    private CustomQueue<Task> pendingTasks;
    private CustomQueue<Task> completedTasks;

    /**
     * Constructor por defecto que inicializa las listas de tareas pendientes y completadas.
     */
    public Activity() {
        this.pendingTasks = new CustomQueue<>();
        this.completedTasks = new CustomQueue<>();
    }

    /**
     * Constructor que recibe el nombre y la descripción de la actividad.
     *
     * @param name        Nombre de la actividad.
     * @param description Descripción de la actividad.
     */
    public Activity(String name, String description) {
        this(name);
        this.description = description;
    }

    /**
     * Constructor que recibe solo el nombre de la actividad.
     *
     * @param name Nombre de la actividad.
     */
    public Activity(String name) {
        this.id = IDGenerator.generateID(); // Genera un ID único para la actividad
        this.name = name;
        this.pendingTasks = new CustomQueue<>();
        this.completedTasks = new CustomQueue<>();
    }

    /**
     * Agrega una tarea a la lista de tareas pendientes.
     *
     * @param task Tarea a agregar.
     */
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

    public Task completeTask(Task task) {
        pendingTasks.remove(task);
        task.setStatus(TaskStatus.COMPLETED);
        addCompletedTasks(task);
        return task;
    }

    /**
     * Calcula y devuelve la duración total de la actividad.
     *
     * @return La duración total en minutos de todas las tareas (pendientes y completadas).
     */
    public int getTotalDurationMinutes() {
        int totalDuration = 0;
        for (Task task : pendingTasks.toList()) totalDuration += task.getDuration();

        for (Task task : completedTasks.toList()) totalDuration += task.getDuration();

        return totalDuration;
    }

    /**
     * Convierte las tareas pendientes en un arreglo JSON.
     *
     * @return Arreglo JSON de tareas pendientes.
     */
    public JsonArray pendingTasksToJson(){
        JsonArray tasksArray = new JsonArray();
        for (Task task : pendingTasks.toList()) tasksArray.add(task.toJson());
        return tasksArray;
    }

    /**
     * Convierte las tareas completadas en un arreglo JSON.
     *
     * @return Arreglo JSON de tareas completadas.
     */
    public JsonArray completedTasksToJson(){
        JsonArray tasksArray = new JsonArray();
        for (Task task : completedTasks.toList()) tasksArray.add(task.toJson());
        return tasksArray;
    }

    /**
     * Convierte la actividad completa en un objeto JSON.
     *
     * @return Objeto JSON que representa la actividad.
     */
    public JsonObject toJson(){
        JsonObject activity = new JsonObject();
        activity.addProperty("id",id);
        activity.addProperty("name",name);
        activity.addProperty("description",description);
        activity.add("pendingTasks",pendingTasksToJson());
        activity.add("completedTasks",completedTasksToJson());
        return activity;
    }

    /**
     * Busca una tarea por su ID dentro de la actividad.
     *
     * @param taskId ID de la tarea a buscar.
     * @return La tarea encontrada o null si no se encuentra.
     */
    public Task findTaskById(String taskId) {
        for (Task task : pendingTasks.toList()) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        for (Task task : completedTasks.toList()) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null; // La tarea no se encontró en esta actividad
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPendingTasks(CustomQueue<Task> pendingTasks) {
        this.pendingTasks = pendingTasks;
    }

    public void setCompletedTasks(CustomQueue<Task> completedTasks) {
        this.completedTasks = completedTasks;
    }


    public void UpdateTask(Task current, Task newTask){
        pendingTasks.updateElement(current,newTask);
    }

    public void addCompletedTasks(Task task) {
        completedTasks.enqueue(task);
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

    @Override
    public String toString() {
        return "Activity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(getId(), activity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

