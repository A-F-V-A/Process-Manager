package edu.est.process.manager.domain.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.domain.util.IDGenerator;

import java.io.Serializable;
import java.util.PrimitiveIterator;

public class CustomProcess {
    private String id;
    private String name;
    private String description;
    private CustomDoublyLinkedList<Activity> activities;
    private int totalDurationMinutes = 0;

    public CustomProcess(){
        this.activities = new CustomDoublyLinkedList<>();
    }

    public CustomProcess(String name,String description) {
        this(name);
        this.description = description;
    }

    public CustomProcess(String name) {
        this.id = IDGenerator.generateID();
        this.name = name;
        this.activities = new CustomDoublyLinkedList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addActivity(Activity activity) {
        activities.addLast(activity);
        updateTotalDuration();
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

    private void updateTotalDuration() {
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
}
