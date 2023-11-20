package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.infrastructure.javafx.util.NotificacionEmail;
import edu.est.process.manager.infrastructure.javafx.util.Notification;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CNotification {
    private CheckBox appNotificationCheckbox;
    private CheckBox emailNotificationCheckbox;
    private Label emailLabel;
    private TextField emailField;
    private Label percentageLabel;
    private TextField percentageField;

    private  Notification notificationApp;
    private  NotificacionEmail notificacionEmail;

    public CNotification(Notification notificationApp) {
        this.notificationApp = notificationApp;
        this.notificacionEmail = new NotificacionEmail(notificacionEmail.getEmailAddress(), " ");
    }

    public CNotification() {
    }

    public VBox render() {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(10.0);
        card.getStyleClass().add("notification-card");
        card.setPadding(new Insets(10, 10, 10, 10));
        double cardWidth = 300;
        card.setPrefWidth(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setPrefHeight(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setMinWidth(cardWidth);

        Text title = new Text("How would you like to be notified");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        appNotificationCheckbox = new CheckBox("App notifications");
        emailNotificationCheckbox = new CheckBox("Email notifications");

        emailLabel = new Label("Enter your email:");
        emailField = new TextField();
        emailField.setPromptText("Email");

        percentageLabel = new Label("At what % of time would you like to be notified:");
        percentageField = new TextField();
        percentageField.setPromptText("Enter % (numbers only)");
        percentageField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));



        emailNotificationCheckbox.setOnAction(event -> handleCheckboxAction());
        appNotificationCheckbox.setOnAction(event -> handleCheckboxAction());

        Button saveButton = new Button("Save");
        saveButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setOnAction(event -> handleSaveAction());

        emailLabel.setVisible(false);
        emailField.setVisible(false);
        percentageLabel.setVisible(false);
        percentageField.setVisible(false);

        card.getChildren().addAll(
                title,
                appNotificationCheckbox,
                emailNotificationCheckbox,
                emailLabel,
                emailField,
                percentageLabel,
                percentageField,
                saveButton
        );

        return card;
    }

    private void handleSaveAction() {
        boolean appNotificationSelected = appNotificationCheckbox.isSelected();
        boolean emailNotificationSelected = emailNotificationCheckbox.isSelected();
        String emailAddress = emailField.getText();

        int percentage = 0;
        try {
            percentage = Integer.parseInt(percentageField.getText());
            if (percentage < 0 || percentage > 100) {
                System.out.println("El porcentaje debe estar entre 0 y 100.");
                return; // Salir si el porcentaje no está en el rango válido
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: El porcentaje debe ser un número entero.");
            return; // Salir si hay un error al parsear el porcentaje
        }

        System.out.println("Notificaciones de app: " + appNotificationSelected);
        System.out.println("Notificaciones por correo: " + emailNotificationSelected);
        System.out.println("Correo electrónico: " + emailAddress);
        System.out.println("Porcentaje de notificación: " + percentage + "%");

        // Limpiar los campos y deseleccionar los checkboxes
        clearFieldsAndCheckboxes();

        if (appNotificationSelected) {
            Task taskApp = new Task();
            notificationApp.subscribe(taskApp);
        }

        if (emailNotificationSelected) {
            Task taskEmail = new Task();
            notificacionEmail.sendEmail(emailAddress, taskEmail, "Mensaje de notificacion");
        }
    }

    private void clearFieldsAndCheckboxes() {
        appNotificationCheckbox.setSelected(false);
        emailNotificationCheckbox.setSelected(false);
        emailField.clear();
        percentageField.clear();
        emailLabel.setVisible(false);
        emailField.setVisible(false);
        percentageLabel.setVisible(false);
        percentageField.setVisible(false);
    }

    private void handleCheckboxAction() {
        boolean email = emailNotificationCheckbox.isSelected();
        boolean app = appNotificationCheckbox.isSelected();
        if(email){
            emailLabel.setVisible(true);
            emailField.setVisible(true);
            percentageLabel.setVisible(true);
            percentageField.setVisible(true);
            appNotificationCheckbox.setSelected(false);
        }else if(app){
            emailLabel.setVisible(false);
            emailField.setVisible(false);
            percentageLabel.setVisible(true);
            percentageField.setVisible(true);
            emailNotificationCheckbox.setSelected(false);
        }else{
            emailLabel.setVisible(false);
            emailField.setVisible(false);
            percentageLabel.setVisible(false);
            percentageField.setVisible(false);
            appNotificationCheckbox.setSelected(false);
            emailNotificationCheckbox.setSelected(false);
        }
    }
}
