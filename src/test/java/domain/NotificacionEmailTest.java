package domain;

import edu.est.process.manager.domain.models.NotificacionEmail;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionEmailTest {

    @Test
    void testNotifyEmail_TaskExpired() {
        Task task = new Task("Tarea 1",TaskStatus.PENDING, 5); // Tarea con duración de 5 minutos
        NotificacionEmail notification = new NotificacionEmail("juand.lopezm@uqvirtual.edu.co", "Recordatorio", task);

        // Avanzar el tiempo para que la tarea expire
        advanceTime(task, 6);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertFalse(notification.isActive()); // La notificación debe estar inactiva
        assertEquals(TaskStatus.DELAYED, task.getStatus()); // El estado de la tarea debe ser retrasada
    }

    @Test
    void testNotifyEmail_TaskAlmostExpired() {
        Task task = new Task("Tarea 2",TaskStatus.PENDING, 10); // Tarea con duración de 10 minutos
        NotificacionEmail notification = new NotificacionEmail("user@example.com", "Recordatorio", task);

        // Avanzar el tiempo para estar cerca del 20% del tiempo
        advanceTime(task, 2);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertTrue(notification.isActive()); // La notificación debe estar activa
        assertEquals(TaskStatus.PENDING, task.getStatus()); // El estado de la tarea no debe haber cambiado aún
    }

    @Test
    void testNotifyEmail_TaskWithinNotificationThreshold() {
        Task task = new Task("Tarea 3",TaskStatus.PENDING, 10); // Tarea con duración de 10 minutos
        NotificacionEmail notification = new NotificacionEmail("user@example.com", "Recordatorio", task);

        // Avanzar el tiempo al 20% del tiempo para notificar
        advanceTime(task, 2);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertTrue(notification.isActive()); // La notificación debe estar activa
        assertEquals(TaskStatus.PENDING, task.getStatus()); // El estado de la tarea no debe haber cambiado aún
    }

    // Método para avanzar el tiempo simulando la duración de la tarea
    private void advanceTime(Task task, int minutes) {
        for (int i = 0; i < minutes; i++) {
            task.setDurationMinutes(task.getDurationMinutes() - 1);
        }
    }
}
