package edu.est.process.manager.domain.models;

import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.domain.util.IDGenerator;

public class CustomProcess {
    private String id;
    private String name;
    private CustomDoublyLinkedList<Activity> activities;
    private int totalDurationMinutes;

    public CustomProcess(String name) {
        this.id = IDGenerator.generateID();
        this.name = name;
        this.activities = new CustomDoublyLinkedList<>();
        this.totalDurationMinutes = 0;
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
}
