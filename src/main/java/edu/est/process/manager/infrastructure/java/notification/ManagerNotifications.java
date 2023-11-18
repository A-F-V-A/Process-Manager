package edu.est.process.manager.infrastructure.java.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ManagerNotifications {

    private List<Activity> activity;
    public boolean active;

    public ManagerNotifications() {
        this.activity = new ArrayList<>();
        this.active = true;
    }

    public synchronized void subscribe (Activity activity) {
        this.activity.add(activity);
    }

    public synchronized void notification() {
        while (active) {
            Iterator<Activity> iterator = activity.iterator();
            while (iterator.hasNext()) {
                Activity activity1 = iterator.next();
                long timeLeft = activity1.getTiempoRestante();

                if (timeLeft <= 0) {
                    System.out.println("Recordatorio: la actividad " + activity1.getNombre() + "' ha expirado.");
                    iterator.remove();
                } else {
                    if (timeLeft < 60000) {
                        System.out.println("Recordatorio: La actividad '" + activity1.getNombre() + "' está por expirar.");
                    }
                }
                activity1.disminuirTiempo(1000);
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public synchronized void stopNotifications() {
        this.active = false;
    }

    public List<Activity> getActivity() {
        return activity;
    }

    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
