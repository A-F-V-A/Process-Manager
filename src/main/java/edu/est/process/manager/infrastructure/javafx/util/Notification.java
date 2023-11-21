package edu.est.process.manager.infrastructure.javafx.util;

import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import edu.est.process.manager.infrastructure.javafx.components.CAlert;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase para gestionar las notificaciones de tareas pendientes.
 */
public class Notification {

    private List<Task> tasks;
    private boolean active;

    /**
     * Constructor de la clase Notification.
     * Inicializa la lista de tareas y activa las notificaciones.
     */
    public Notification() {
        this.tasks = new ArrayList<>();
        this.active = true;
    }

    /**
     * Suscribe una tarea para notificar su estado.
     *
     * @param task Tarea a ser suscrita para notificaciones.
     */
    public synchronized void subscribe(Task task) {
        task.setStarTime(System.currentTimeMillis());
        this.tasks.add(task);
    }

    /**
     * Notifica el estado de las tareas pendientes.
     * Muestra mensajes cuando una tarea está por expirar o finaliza antes del tiempo estimado.
     *
     * @param notificationThreshold Porcentaje del tiempo de la tarea para recibir alertas.
     */
    public synchronized void notifyTasks(double notificationThreshold) {
        while (active) {
            Iterator<Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                int remainingMinutes = task.getDurationMinutes();

                if (remainingMinutes <= 0) {
                    System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' ha expirado.");
                    CAlert.Alert(Alert.AlertType.INFORMATION,"Task Completion","Saved successfully","");
                    task.setStatus(TaskStatus.DELAYED);
                    iterator.remove();

                } else {
                    double timeNotify = (remainingMinutes * notificationThreshold) / 100.0;

                    if (remainingMinutes == 1) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' está por expirar en 1 minuto.");
                        CAlert.Alert(Alert.AlertType.INFORMATION,"Task Completion","the task has timed out ","");

                    } else if (remainingMinutes <= timeNotify && remainingMinutes > 0) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' está por expirar en " + remainingMinutes + " minutos.");
                        CAlert.Alert(Alert.AlertType.INFORMATION,"Task About to Finish","Reminder: Task '" + task.getDescription() +
                                "' is about to expire in " + remainingMinutes + " minutes.", "");

                    } else if (remainingMinutes < 1) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' ha finalizado antes del tiempo estimado.");
                        CAlert.Alert(Alert.AlertType.INFORMATION,"Task About to Completed","Reminder: Task '" + task.getDescription() +
                                "' has finished before the estimated time. ", "");

                        task.setStatus(TaskStatus.COMPLETED);
                        iterator.remove();
                    }
                    task.setDurationMinutes(remainingMinutes - 1);
                }
            }

            try {
                Thread.sleep(60000); // Espera 1 minuto antes de revisar nuevamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Detiene las notificaciones de tareas pendientes.
     */
    public synchronized void stopNotifications() {
        this.active = false;
    }

    /**
     * Elimina una tarea completada de la lista de tareas pendientes.
     *
     * @param completedTask Tarea completada a ser removida de la lista.
     */
    public synchronized void removeCompletedTask(Task completedTask) {
        tasks.remove(completedTask);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
