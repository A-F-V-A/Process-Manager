package application;

import edu.est.process.manager.domain.models.Notification;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTest {

    @Test
    public void testNotificationTasks() {
        Notification notification = new Notification();
        Task task1 = new Task("Tarea 1", TaskStatus.PENDING, 5); // Tarea de 5 minutos
        Task task2 = new Task("Tarea 2", TaskStatus.PENDING, 10); // Tarea de 10 minutos

        notification.subscribe(task1);
        notification.subscribe(task2);

        // Probamos la notificación con un porcentaje de alerta del 50%
        notification.notifyTasks(50);

        // Comprobamos si las tareas se completan según lo esperado
        assertEquals(TaskStatus.COMPLETED, task1.getStatus());
        assertEquals(TaskStatus.COMPLETED, task2.getStatus());
    }

    @Test
    public void testNotifyTasksExpiration() {
        Notification notification = new Notification();
        Task task1 = new Task("Tarea 1", TaskStatus.PENDING, 1); // Tarea de 2 minutos
        Task task2 = new Task("Tarea 2", TaskStatus.PENDING, 5); // Tarea de 5 minutos

        notification.subscribe(task1);
        notification.subscribe(task2);

        // Ejecutamos la notificación con un porcentaje de alerta del 50%
        notification.notifyTasks(50);

        // Comprobamos si las tareas se completan como se espera
        assertEquals(TaskStatus.COMPLETED, task1.getStatus());
        assertEquals(TaskStatus.COMPLETED, task2.getStatus());
    }

    @Test
    public void testNotifyTasksNotificationThreshold() {
        Notification notification = new Notification();
        Task task1 = new Task("Tarea 1", TaskStatus.PENDING, 10); // Tarea de 10 minutos
        Task task2 = new Task("Tarea 2", TaskStatus.PENDING, 15); // Tarea de 15 minutos

        notification.subscribe(task1);
        notification.subscribe(task2);

        // Ejecutamos la notificación con un porcentaje de alerta del 20%
        notification.notifyTasks(20);

        // Comprobamos si las tareas se notifican correctamente
        assertEquals(TaskStatus.COMPLETED, task1.getStatus()); // La tarea 1 finaliza antes del tiempo estimado
        assertEquals(TaskStatus.DELAYED, task2.getStatus()); // La tarea 2 expira y se marca como retrasada
    }

    @Test
    public void testRemoveCompletedTask() {
        Notification notification = new Notification();
        Task task = new Task("Tarea 1", TaskStatus.PENDING, 2); // Tarea de 2 minutos

        notification.subscribe(task);

        // Ejecutamos la notificación
        notification.notifyTasks(50);

        // Comprobamos si la tarea completada se elimina correctamente
        assertEquals(0, notification.getTasks().size()); // La tarea se elimina después de completarse
    }
}
