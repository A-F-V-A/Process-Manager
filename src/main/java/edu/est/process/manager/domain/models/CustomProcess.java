package edu.est.process.manager.domain.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.domain.util.IDGenerator;
import java.util.Iterator;


/**
 * Clase que representa un proceso personalizado en el sistema.
 */
public class CustomProcess {
    private String id;
    private String name;
    private String description;
    private CustomDoublyLinkedList<Activity> activities;
    private int totalDurationMinutes = 0;

    /**
     * Constructor por defecto que inicializa la lista de actividades.
     */
    public CustomProcess(){
        this.activities = new CustomDoublyLinkedList<>();
    }

    /**
     * Constructor que recibe el nombre y la descripción del proceso.
     *
     * @param name        Nombre del proceso.
     * @param description Descripción del proceso.
     */
    public CustomProcess(String name,String description) {
        this(name);
        this.description = description;
    }

    /**
     * Constructor que recibe solo el nombre del proceso.
     *
     * @param name Nombre del proceso.
     */
    public CustomProcess(String name) {
        this.id = IDGenerator.generateID();
        this.name = name;
        this.activities = new CustomDoublyLinkedList<>();
    }

    /**
     * Agrega una actividad al proceso y actualiza la duración total del proceso.
     *
     * @param activity Actividad a agregar.
     */
    public void addActivity(Activity activity) {
        activities.addLast(activity);
        updateTotalDuration();
    }

    /**
     * Genera un objeto JSON representando el proceso y sus actividades.
     *
     * @return Objeto JSON representando el proceso.
     */
    public JsonObject toJson(){
        JsonObject process = new JsonObject();
        process.addProperty("id",id);
        process.addProperty("name",name);
        process.addProperty("description",description);

        JsonArray activityArray = new JsonArray();
        activities.forEach(act -> activityArray.add(act.toJson()),false);
        process.add("activities",activityArray);
        process.addProperty("totalDurationMinutes",totalDurationMinutes);
        return process;
    }

    //Buscamos una tarea desde el inicio del proceso
    public Task findTaskFromStart(String taskId) {
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            Task foundTask = activity.findTaskById(taskId);
            if (foundTask != null) {
                return foundTask;
            }
        }
        return null; // La tarea no se encontró en el proceso
    }

    //Buscamos una tarea desde la actividad actual
    public Task findTaskFromCurrentActivity(String currentActivityId, String taskId) {
        Activity currentActivity = getActivity(currentActivityId);
        if (currentActivity != null) {
            Task foundTask = currentActivity.findTaskById(taskId);
            if (foundTask != null) {
                return foundTask;
            }
        }
        return null; // La tarea no se encontró en la actividad actual
    }

    //Buscamos una tarea desde una actividad dada su nombre
    public Task findTaskFromActivityByName(String activityName, String taskId) {
        Iterator<Activity> iterator = activities.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getName().equals(activityName)) {
                Task foundTask = activity.findTaskById(taskId);
                if (foundTask != null) {
                    return foundTask;
                }
            }
        }
        return null; // La tarea no se encontró en la actividad con el nombre dado
    }

    public Activity getActivity(String activityId) {
        return activities.findFirst(activity -> activity.getId().equals(activityId));
    }

    public boolean removeActivity(String activityId) {
        boolean isRemoved = activities.removeIf(activity -> activity.getId().equals(activityId));
        if (isRemoved) {
            updateTotalDuration();
        }
        return isRemoved;
    }

    private void updateTotalDuration(int totalDurationMinutes) {
        this.totalDurationMinutes += totalDurationMinutes;
    }

    public void updateTotalDuration() {
        this.totalDurationMinutes = 0;
        activities.forEach(duration ->
                        updateTotalDuration(duration.getTotalDurationMinutes())
                ,false);
    }

    public int getTotalDurationMinutes(){
        return this.totalDurationMinutes;
    }

    public CustomDoublyLinkedList<Activity> getActivities() {
        return activities;
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

    public void setActivities(CustomDoublyLinkedList<Activity> activities) {
        this.activities = activities;
    }

    public void setTotalDurationMinutes(int totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
