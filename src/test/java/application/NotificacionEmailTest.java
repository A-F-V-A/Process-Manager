package application;

import edu.est.process.manager.infrastructure.javafx.util.NotificacionEmail;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotificacionEmailTest {

    @Test
    void testNotifyEmail_TaskExpired() {
        Task task = new Task("Tarea 1",TaskStatus.PENDING, 5); // Tarea con duración de 5 minutos
        NotificacionEmail notification = new NotificacionEmail("xxsigfridxxx@gmail.com", "Recordatorio");

        // Avanzar el tiempo para que la tarea expire
        advanceTime(task, 6);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertFalse(notification.isActive()); // La notificación debe estar inactiva
        assertEquals(TaskStatus.DELAYED, task.getStatus()); // El estado de la tarea debe ser retrasada
    }

    @Test
    void testNotifyEmail_TaskAlmostExpired() {
        Task task = new Task("Tarea 2",TaskStatus.PENDING, 10); // Tarea con duración de 10 minutos
        NotificacionEmail notification = new NotificacionEmail("xxsigfridxxx@gmail.com", "Recordatorio");

        // Avanzar el tiempo para estar cerca del 20% del tiempo
        advanceTime(task, 2);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertTrue(notification.isActive()); // La notificación debe estar activa
        assertEquals(TaskStatus.PENDING, task.getStatus()); // El estado de la tarea no debe haber cambiado aún
    }

    @Test
    void testNotifyEmail_TaskWithinNotificationThreshold() {
        Task task = new Task("Tarea 3",TaskStatus.PENDING, 10); // Tarea con duración de 10 minutos
        NotificacionEmail notification = new NotificacionEmail("xxsigfridxxx@gmail.com", "Recordatorio");

        // Avanzar el tiempo al 20% del tiempo para notificar
        advanceTime(task, 2);

        notification.notifyEmail(20); // Notificar al 20% del tiempo

        assertTrue(notification.isActive()); // La notificación debe estar activa
        assertEquals(TaskStatus.PENDING, task.getStatus()); // El estado de la tarea no debe haber cambiado aún
    }

    @Test
    void testSendEmail() {
        Task task = new Task("Tarea de prueba", TaskStatus.PENDING, 15);
        NotificacionEmail notification = new NotificacionEmail("correonotificacionprocesos@gmail.com", "Prueba de correo");

        // Envía el correo y maneja posibles excepciones
        try {
            notification.sendEmail("xxsigfridxxx@gmail.com", task, "Este es un correo de prueba.", "");
        } catch (Exception e) {
            fail("Error al enviar el correo electrónico: " + e.getMessage());
        }

        // Espera un tiempo suficiente para que el envío del correo se complete
        try {
            Thread.sleep(10000); // Espera 10 segundos para permitir el envío del correo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verifica si la notificación está inactiva después del envío del correo
        assertFalse(notification.isActive(), "La notificación debería estar inactiva después del envío del correo.");

        // Agrega más aserciones aquí para verificar el contenido del correo enviado o realiza otras verificaciones necesarias
    }


    // Método para avanzar el tiempo simulando la duración de la tarea
    private void advanceTime(Task task, int minutes) {
        for (int i = 0; i < minutes; i++) {
            task.setDurationMinutes(task.getDurationMinutes() - 1);
        }
    }
}
