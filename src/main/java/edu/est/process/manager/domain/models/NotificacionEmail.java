package edu.est.process.manager.domain.models;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * Clase que gestiona la notificación por correo electrónico relacionada con las tareas.
 */
public class NotificacionEmail {

    private String emailAddress;
    private String message;
    private Task task;
    private boolean active;

    /**
     * Constructor para crear una instancia de notificación por correo electrónico.
     *
     * @param emailAddress Dirección de correo electrónico a la que se enviará la notificación.
     * @param message      Mensaje de la notificación por correo electrónico.
     * @param task         Tarea asociada a la notificación.
     */
    public NotificacionEmail(String emailAddress, String message, Task task) {
        this.emailAddress = emailAddress;
        this.message = message;
        this.task = task;
        this.active = true;
    }

    /**
     * Método para enviar un correo electrónico con respecto a la tarea.
     *
     * @param emailAddress Dirección de correo electrónico a la que se enviará el correo.
     * @param task          Tarea asociada al correo electrónico.
     * @param m             Mensaje del correo electrónico.
     */
    public void sendEmail(String emailAddress, Task task, String m) {
        try {
            Email email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587 );
            email.setAuthenticator(new DefaultAuthenticator("correonotificacionprocesos@gmail.com", "bnsadbhfuyfjotuo"));
            email.setSSLOnConnect(true);
            email.setFrom("xxsigfridxxx@gmail.com");
            email.setSubject("Recordatorio de tarea");
            email.setMsg(m);
            email.addTo(emailAddress);
            email.send();

            active = false;
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
    /**
     * Método para notificar por correo electrónico sobre el estado de la tarea basado en un umbral de notificación.
     *
     * @param notificationThreshold Umbral para la notificación por correo electrónico.
     */
    public synchronized void notifyEmail(double notificationThreshold) {
        if (active) {
            double remainingTime = task.getDurationMinutes();
            double timeToNotify = remainingTime * notificationThreshold / 100.0;

            if (remainingTime <= 0) {
                sendEmail(emailAddress, task, "La tarea '" + task.getDescription() + "' ha expirado.");
                task.setStatus(TaskStatus.DELAYED);
                active = false; // Desactiva la notificación después de enviar el correo por expiración
            } else if (remainingTime <= timeToNotify && remainingTime > 0) {
                sendEmail(emailAddress, task, "La tarea '" + task.getDescription() + "' está por expirar en " + remainingTime + " minutos.");
                active = false; // Desactiva la notificación después de enviar el correo por estar cerca de expirar
            }
        }
    }
    /**
     * Método para desactivar una notificación por correo electrónico cuando se completa.
     *
     * @param notificacionEmail Notificación por correo electrónico a desactivar.
     */
    public synchronized void removeCompletedNotificacionEmail(NotificacionEmail notificacionEmail) {
        // Desactiva la notificación solo si la tarea se ha completado
        if (notificacionEmail.getTask().getStatus() == TaskStatus.COMPLETED) {
            active = false;
        }
    }
    public String getEmailAddress() {
        return emailAddress;
    }

    public String getMessage() {
        return message;
    }

    public Task getTask() {
        return task;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTiempoRestante() {
        return task.getDurationMinutes();
    }

}
