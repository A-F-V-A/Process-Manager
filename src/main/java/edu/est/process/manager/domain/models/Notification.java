package edu.est.process.manager.domain.models;

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
                int durationMinutes = task.getDurationMinutes();
                int remainingMinutes = durationMinutes;

                if (durationMinutes <= 0) {
                    System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' ha expirado.");
                    task.setStatus(TaskStatus.DELAYED);
                    iterator.remove();
                } else {
                    // Calcular el porcentaje de tiempo para recibir alertas
                    double percentage = durationMinutes * notificationThreshold / 100.0;

                    if (durationMinutes == 1) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' está por expirar en 1 minuto.");
                    } else if (remainingMinutes <= percentage && remainingMinutes > 0) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' está por expirar en " + remainingMinutes + " minutos.");
                    } else if (remainingMinutes < 1) {
                        System.out.println("Recordatorio: La tarea '" + task.getDescription() + "' ha finalizado antes del tiempo estimado.");
                        task.setStatus(TaskStatus.COMPLETED);
                        iterator.remove();
                    }
                    task.setDurationMinutes(durationMinutes - 1);
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
}
