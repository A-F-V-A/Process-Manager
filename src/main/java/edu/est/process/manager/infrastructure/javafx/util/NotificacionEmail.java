package edu.est.process.manager.infrastructure.javafx.util;

import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
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
     */
    public NotificacionEmail(String emailAddress, String message) {
        this.emailAddress = emailAddress;
        this.message = "";
        this.active = true;
    }

    /**
     * Método para enviar un correo electrónico con respecto a la tarea.
     *
     * @param emailAddress Dirección de correo electrónico a la que se enviará el correo.
     * @param task          Tarea asociada al correo electrónico.
     * @param m             Mensaje del correo electrónico.
     */
    public void sendEmail(String emailAddress, Task task, String m, String asunto) {
        try {
            Email email = new HtmlEmail();
            email.setHostName("smtp.gmail.com");
            email.setSmtpPort(587);
            email.setAuthenticator(new DefaultAuthenticator("correonotificacionprocesos@gmail.com", "bnsadbhfuyfjotuo"));
            email.setSSLOnConnect(true);
            email.setFrom(emailAddress);
            email.setSubject(asunto);
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
            if (notificationThreshold < 0 || notificationThreshold > 100) {
                System.out.println("El porcentaje debe estar entre 0 y 100");
                return; //Salimos si el porcentaje no es está en un rango válido
            }

            double remainingTime = task.getDurationMinutes();
            double timeToNotify = (remainingTime * notificationThreshold) /100;

            if (remainingTime <= 0) {
                sendEmail(emailAddress, task, "La tarea " + task.getId() + "ha expirado", "Finalización de tarea");
                task.setStatus(TaskStatus.DELAYED);
                active = false;
            } else if (remainingTime <= timeToNotify && remainingTime > 0) {
                sendEmail(emailAddress, task, "La tarea " + task.getDescription() + "Esta por finalizar" , "Recordatorio de tarea");
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
