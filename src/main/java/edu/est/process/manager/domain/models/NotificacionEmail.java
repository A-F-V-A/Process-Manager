package edu.est.process.manager.domain.models;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class NotificacionEmail {

    private String emailAddress;
    private String message;
    private Task task;
    private boolean active;

    public NotificacionEmail(String emailAddress, String message, Task task) {
        this.emailAddress = emailAddress;
        this.message = message;
        this.task = task;
        this.active = true;
    }

        public void sendEmail(String emailAddress, Task task, String m) {

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");

            // Credenciales de autenticación del servidor de correo (cuenta de Gmail)
            String username = "correonotificacionprocesos@gmail.com";
            String password = "juanda29";

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                            return new javax.mail.PasswordAuthentication(username, password);
                        }
                    });

            try {
                // Crear el mensaje
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("no-reply@example.com"));
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
                message.setSubject("Recordatorio de tarea");
                message.setText(m);

                // Enviar el mensaje
                Transport.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    public synchronized void notifyEmail(double notificationThreshold) {
        if (active) {
            double remainingTime = task.getDurationMinutes();
            double timeToNotify = remainingTime * notificationThreshold / 100.0;

            if (remainingTime <= 0) {
                sendEmail(emailAddress, task, "La tarea '" + task.getDescription() + "' ha expirado.");
                task.setStatus(TaskStatus.DELAYED);
                active = false;
            } else if (remainingTime <= timeToNotify && remainingTime > 0) {
                sendEmail(emailAddress, task, "La tarea '" + task.getDescription() + "' está por expirar en " + remainingTime + " minutos.");
            }
        }
    }

    public synchronized void removeCompletedNotificacionEmail(NotificacionEmail notificacionEmail) {
        active = false;
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
